package skac.fractalizer

import examples._
import skac.miro.draw.svg._

object Main extends App {
  val group = Example18.group
  val draw = new Draw()
  draw.saveToFile(group, "example18_9.svg")
}
