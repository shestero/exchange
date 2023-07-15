package model

import model.Currency.Currency
import model.Directions._

/**
 * Represent the current state of exchange
 * @param repo - the repository of clients (balances and asset amounts)
 * @param orders - the ordered list of previous orders that was not satisfied
 */
case class State(repo: Map[String, Client], orders: List[Order]) extends io.TsvWriter("result.txt")
{
  // define price of the deal here.
  // In any case it must be somewhere between o.price and oo.price (including)
  def priceLogic(o: Order, oo: Order): Currency = {
    assert(o matched oo)
    Math.min(o.price, oo.price)
  }

  // Process a new coming order
  def next(o: Order): State = {
    if (!repo.contains(o.client)) {
      System.err.println(s"Warning: new order from unknown client '${o.client}; this order is skipped.")
      return this
    }

    val (before, matchedAndAfter) = orders.span(_ notMatched o)

    val afterDeal: Option[State] = for {
      matched <- matchedAndAfter.headOption
      (client1, client2) = (o.client, matched.client)
      _ = assert(client1!=client2)
      (side1, side2) = (repo(client1), repo(client2))
      _ = assert(side1.id==client1)
      _ = assert(side2.id==client2)

      price = priceLogic(o, matched)
      amount: Currency = price*o.number

      if side1.able(o, amount)
      if side2.able(matched, amount)

      ((dBalance1, dAsset1), (dBalance2, dAsset2)) = (o.deltas, matched.deltas)
      _ = assert(dBalance1 == -dBalance2)
      _ = assert(dAsset1 == -dAsset2)

    } yield copy(
      repo = repo ++
        Map(
          client1 -> side1.copy(
            balance = side1.balance + dBalance1 * amount,
            assets = side1.assets + (o.asset -> (side1.assets(o.asset) + dAsset1 * o.number))
          ),
          client2 -> side2.copy(
            balance = side2.balance + dBalance2 * amount,
            assets = side2.assets + (o.asset -> (side2.assets(o.asset) + dAsset2 * o.number))
          )
        ),
      orders = before ++ matchedAndAfter.tail
    )

    afterDeal getOrElse copy(orders = orders :+ o) // if the order wasn't processed then it is to be stored
  }

  def write(ids: Seq[String]): Unit =
    write[Client](_.toArray)(ids.map(repo(_)))
}
