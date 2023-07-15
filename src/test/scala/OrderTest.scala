import model.Directions._
import model.Order
import org.scalatest.funsuite.AnyFunSuite

class OrderTest extends AnyFunSuite {
  test("Order.matched") {
    assert(Order("Client1", Buy, "Asset", 5, 2) matched Order("Client2", Sale, "Asset", 5, 2))
    assert(Order("Client1", Buy, "Asset", 7, 2) matched Order("Client2", Sale, "Asset", 6, 2))
    assert(Order("Client1", Buy, "Asset", 5, 2) notMatched Order("Client2", Sale, "Asset", 6, 2))
    assert(Order("Client1", Sale, "Asset", 5, 2) notMatched Order("Client2", Sale, "Asset", 6, 2))
  }

  test("Order.fromArray") {
    assert(Order.fromArray(Array("Abc", "b", "Asset", "123", "456")) ===
      Order("Abc", Buy, "Asset", 123, 456) )
  }
}
