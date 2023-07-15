package model

object Directions extends Enumeration {
  type Direction = Value
  val Buy, Sale = Value

  implicit class DirectionOps(direction: Direction) {
    def opposite: Direction = direction match {
      case Buy => Sale
      case Sale => Buy
    }
  }

  val fromString: String => Direction = _.trim.toLowerCase() match {
    case "b" => Buy
    case "s" => Sale
  }

  /**
   * @return  The directions of changing a balance and an asset (as -1/+1)
   */
  def deltas(direction: Direction): (Int, Int) = direction match {
    case Buy => (-1, 1)
    case Sale => (1, -1)
  }
}
