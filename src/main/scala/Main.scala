import model._

object Main extends App {
  Orders.stream.foldLeft(Clients.start)(_ next _).write(Clients.ids)
}
