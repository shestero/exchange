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
      m <- matchedAndAfter.headOption
      (side1, side2) = (repo(o.client), repo(m.client))

      price = priceLogic(o, m)
      amount: Currency = price*o.number
      (asset, number) = (o.asset, o.number) // more sophisticated logic here in case partial order processing

      if side1.able(o, amount)
      if side2.able(m, amount)

      ((dBalance1, dAsset1), (dBalance2, dAsset2)) = (o.deltas, m.deltas)
      _ = assert(dBalance1 == -dBalance2)
      _ = assert(dAsset1 == -dAsset2)

    } yield copy(
      repo = repo ++
        Map(
          o.client -> side1.copy(
            balance = side1.balance + dBalance1 * amount,
            assets = side1.assets + (asset -> (side1.assets(asset) + dAsset1 * number))
          ),
          m.client -> side2.copy(
            balance = side2.balance + dBalance2 * amount,
            assets = side2.assets + (asset -> (side2.assets(asset) + dAsset2 * number))
          )
        ),
      orders = before ++ matchedAndAfter.tail
    )

    afterDeal getOrElse copy(orders = orders :+ o) // if the order wasn't processed then it is to be stored
  }

  def write(ids: Seq[String]): Unit =
    write[(String, Client)]{ case (id, c) => id +: c.toArray }(ids.map(id => id -> repo(id)))
}
