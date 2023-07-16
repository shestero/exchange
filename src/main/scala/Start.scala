import model._

object Start extends App {
  Orders.stream.foldLeft(Clients.start)(_ next _).write(Clients.ids)
}
