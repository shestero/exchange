import model._
import model.Directions._
import org.scalatest.funsuite.AnyFunSuite

class ClientTest extends AnyFunSuite {
  test("Client.fromArray") {
    assert(Client.fromArray(Array("qw", "123", "34","56","7","8")) ===
      Client("qw", 123, Map("A" -> 34, "B" -> 56, "C" -> 7, "D" -> 8)))
  }

  test("Client.toArray") {
    assert(Client("Abc", 321, Map("A" -> 43, "C" -> 7, "D" -> 8, "B" -> 65)).toArray ===
      Array("Abc", "321", "43", "65", "7", "8"))
  }

  test("Client.able") {
    val client = Client("Abc", 321, Map("A" -> 43, "C" -> 7, "D" -> 8, "B" -> 65))
    assert(client.able(Order("", Buy, "", 0, 0), 320))
    assert(client.able(Order("", Buy, "", 0, 0), 321))
    assert(!client.able(Order("", Buy, "", 0, 0), 322))
    assert(client.able(Order("", Sale, "A", 0, 42), 320))
    assert(client.able(Order("", Sale, "A", 0, 43), 320))
    assert(!client.able(Order("", Sale, "A", 0, 44), 320))
    assert(!client.able(Order("", Sale, "E", 0, 42), 320))
  }
}
