import model.Directions._
import org.scalatest.funsuite.AnyFunSuite

class DirectionsTest extends AnyFunSuite {
  test("DirectionsTest.opposite") {
    assert( Buy.opposite !== Buy )
    assert( Sale.opposite !== Sale )
    assert( Buy.opposite.opposite === Buy )
    assert( Sale.opposite.opposite === Sale )
  }
}
