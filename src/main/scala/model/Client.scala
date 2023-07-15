package model

import model.Currency.Currency
import model.Directions._

case class Client(balance: Currency, assets: Map[String, Int]) {

  val toList: List[String] =
    balance.toString :: Assets.all.map(assets.getOrElse(_, 0).toString)

  val toArray: Array[String] = toList.toArray

  /**
   * Check if the client is now able to make the deal.
   * @param o - the client's order that correspond to the deal
   * @param amount - as the price of the deal may vary from the o.price the total amount of the deal is provided here.
   * @return yes or no
   */
  def able(o: Order, amount: Currency): Boolean = {
    o.direction match {
      case Buy => balance>=amount
      case Sale => assets.getOrElse(o.asset, 0)>=o.number
    }
  }
}

object Client {

  val fromList: List[String] => (String, Client) = _ match {
    case id :: balance :: assets =>
      assert(assets.size == Assets.all.size)
      id -> Client(Currency(balance), (Assets.all zip assets.map(_.toInt)).toMap)
  }

  val fromArray: Array[String] => (String, Client) = fromList compose { _.toList }
}
