package com.github.skac112.fractalizer

import com.github.skac112.fractalizer.examples.Example15
import com.github.skac112.miro.draw.svg._

object Main extends App {
  val group = Example15.group
  val draw = new Draw()
  draw.saveToFile(group, "varia/drawings/example15_2.svg")
}
