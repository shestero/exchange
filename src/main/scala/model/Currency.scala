package model

object Currency {
  type Currency = Int

  def apply(s: String): Currency = s.toInt
}
