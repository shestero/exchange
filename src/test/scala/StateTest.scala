import model.Directions._
import model._
import org.scalatest.funsuite.AnyFunSuite

class StateTest extends AnyFunSuite {

  val start = State(Map(
    "Client1" -> Client(100, Map("Asset" -> 10)),
    "Client2" -> Client(100, Map("Asset" -> 20))
  ), LazyList.empty)

  test("State.next.matched") {
    assert(
      start
        .next(Order("Client1", Buy, "Asset", 6, 2))
        .next(Order("Client2", Sale, "Asset", 5, 2))
        ===
        State(Map(
          "Client1" -> Client(90, Map("Asset" -> 12)),
          "Client2" -> Client(110, Map("Asset" -> 18))
        ), LazyList.empty)
    )
    assert(
      start
        .next(Order("Client1", Buy, "Asset", 5, 2))
        .next(Order("Client2", Sale, "Asset", 5, 2))
        ===
        State(Map(
          "Client1" -> Client(90, Map("Asset" -> 12)),
          "Client2" -> Client(110, Map("Asset" -> 18))
        ), LazyList.empty)
    )
  }

  test("State.next.unmatched.direction") {
    assert(
      start
        .next(Order("Client1", Sale, "Asset", 6, 2))
        .next(Order("Client2", Sale, "Asset", 5, 2))
        ===
        State(start.clients, LazyList(
          Order("Client1", Sale, "Asset", 6, 2),
          Order("Client2", Sale, "Asset", 5, 2)
        ))
    )
  }

  test("State.next.unmatched.assets") {
    assert(
      start
        .next(Order("Client1", Buy, "Asset1", 5, 2))
        .next(Order("Client2", Sale, "Asset2", 5, 2))
        ===
        State(start.clients, LazyList(
          Order("Client1", Buy, "Asset1", 5, 2),
          Order("Client2", Sale, "Asset2", 5, 2)
        ))
    )
  }

  test("State.next.unmatched.price") {
    assert(
      start
        .next(Order("Client1", Buy, "Asset", 5, 2))
        .next(Order("Client2", Sale, "Asset", 6, 2))
        ===
        State(start.clients, LazyList(
          Order("Client1", Buy, "Asset", 5, 2),
          Order("Client2", Sale, "Asset", 6, 2)
        ))
    )
  }
}
