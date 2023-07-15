package model

import model.Currency.Currency
import model.Directions._

case class Order(client: String, direction: Direction, asset: String, price: Currency, number: Int) {

  def matched(price: Currency): Boolean = direction match {
    case Buy => this.price>=price     // when I buy the price may be less than I offer
    case Sale => this.price<=price    // when I sale the price may be greater than I require
  }

  def matched(o: Order): Boolean = {
    client!=o.client &&
      asset==o.asset &&
      direction.opposite==o.direction &&
      number==o.number &&
      matched(o.price)
  }

  def notMatched(o: Order): Boolean = !matched(o)

  val deltas: (Int, Int) = Directions.deltas(direction)
}

object Order {

  val fromArray: Array[String] => Order = _ match {
    case Array(client, dir, asset, price, amount) =>
      Order(client, fromString(dir), asset, Currency(price), amount.toInt)
  }

}