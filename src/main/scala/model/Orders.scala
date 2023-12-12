package model

object Orders extends io.TsvReader("orders.txt") {
  val stream: Iterator[Order] = read(Order.fromArray)
  val lazyList: LazyList[Order] = stream.to(LazyList)
}
