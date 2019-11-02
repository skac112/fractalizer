package com.github.skac112.fractalizer.examples

import com.github.skac112.miro.graphics._
import com.github.skac112.miro.attribs.colors._
import com.github.skac112.miro._
import com.github.skac112.fractalizer.stylers.colorizers._
import com.github.skac112.fractalizer.nodes.partitioners._
import com.github.skac112.fractalizer._
import com.github.skac112.fractalizer.nodes.special._
import com.github.skac112.vgutils._
import com.github.skac112.fractalizer.Palette

object Example15 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val part1 = Partition.power(0.1, 3.0, 30, 3.0)
    val part2 = Partition.random(10, -10.0)
    val base_color = Color(math.random, math.random, math.random)
    val styler = Selected(Palette.randSimHsl(base_color, 10, .1, .1, .1))
    val repl1 = PolarRad(part1, Some(styler))
    val repl2 = PolarAng(part2, Some(styler))
    val repl = repl1 + (repl2 | (DrawCut(1)|*3) | repl2) + DrawCut()
//    val repl = repl1 + DrawCut()
    val out = repl(Seq(c)).run._1
    Group(out)
  }
}
