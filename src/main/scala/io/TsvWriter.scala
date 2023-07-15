package io

import java.io.PrintWriter

class TsvWriter(fileName: String, sep: String = "\t") {

  def write[T](toStrArray: T => Array[String])(source: Iterable[T]): Unit =
    new PrintWriter(fileName){ source.map(toStrArray).map(_.mkString(sep)).foreach(println); close }

}
