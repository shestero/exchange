package io

import scala.io.Source

class TsvReader(fileName: String, sep: String = "\t") {

  def read[T](fromStrArray: Array[String] => T): Iterator[T] =
    Source.fromFile(fileName).getLines().map(_.trim.split(sep)).map(fromStrArray)

}
