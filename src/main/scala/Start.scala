import model._

import Clients.start // start state

object Start extends App {
  // Single round version:
  //Orders.stream.foldLeft(start)(_ next _).write(Clients.ids)

  // Multi-round version:
  val rounds =
    LazyList.unfold(start){ state =>
      val round = state.orders.foldLeft(state.withoutOrders)(_ next _)
      Option.when(state.orders!=round.orders)((round, round))
    }

  rounds.lastOption match {
    case Some(finalState) =>
      println(s"There were ${rounds.size} round(s).")
      finalState.write(Clients.ids)
    case None =>
      println("Nothing matched!")
  }
}
