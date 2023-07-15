package model

object Clients extends io.TsvReader("clients.txt") {

  // ids = ordered client id's (to preserve client's order for output)
  val (ids: Seq[String], repo0) = read(Client.fromArray).map{ case p @ (id, _) => id -> p }.toSeq.unzip

  // start = initial state
  val start = State(repo0.toMap, List.empty)

}
