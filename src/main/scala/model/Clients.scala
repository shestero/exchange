package model

object Clients extends io.TsvReader("clients.txt") {

  val clients: Seq[(String, Client)] = read(Client.fromArray).toSeq

  // ids = ordered client id's (to preserve client's order for output)
  val ids: Seq[String] = clients.map(_._1)

  // start = initial state
  val start = State(clients.toMap, List.empty)

}
