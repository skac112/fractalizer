package com.github.skac112.fractalizer.examples

import com.github.skac112.miro.graphics._
import com.github.skac112.miro.Graphic._
import com.github.skac112.miro.draw.svg._
import com.github.skac112.miro.attribs._
import com.github.skac112.miro.attribs.colors._
import com.github.skac112.miro._
import com.github.skac112.miro.attribs.colors._
import com.github.skac112.miro.graphics.compounds._
import com.github.skac112.fractalizer.Node._
import com.github.skac112.fractalizer._
import com.github.skac112.fractalizer.nodes.partitioners.rectangles._
import stylers.colorizers._
import com.github.skac112.vgutils._


object Example29 {
  def group = {
    val rp = (Rect(100, 100).fill(MiroColor.red(0.8)), Point(0, 0))
    val styler1 = Disperse(5, 1.4, 0.3, 0.3)
    val styler2 = Disperse(5, 0.4, 0.05, 0.05)
    val node1 = Bars.uniform(5, 'SIDETYPE_WIDTH, Some(styler1))
    val node2 = Bars.uniform(5,'SIDETYPE_HEIGHT, Some(styler2))
    val node = node1 + node2
    val out = node(Seq(rp)).run._2
    Group(out)
  }
}