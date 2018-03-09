# Partitioning a triangle

```scala212
pprintConfig() = pprintConfig().copy(height = 10000)
interp.load.ivy(("skac", "miro_2.12", "1.0.2-SNAPSHOT"))
interp.load.ivy(("skac", "fractalizer_2.12", "1.0.2-SNAPSHOT"))

```


```scala212
import skac.fractalizer.examples._
import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import skac.fractalizer.nodes.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer._
import skac.fractalizer.Palette._
import skac.fractalizer.nodes._
import skac.fractalizer._
import skac.fractalizer.nodes.filters._
import skac.fractalizer.nodes.stripes._
import skac.miro.graphics.compounds._
import scala.math._
```




    [32mimport [39m[36mskac.fractalizer.examples._
    [39m
    [32mimport [39m[36mskac.miro.graphics._
    [39m
    [32mimport [39m[36mskac.miro.Graphic._
    [39m
    [32mimport [39m[36mskac.miro.draw.svg._
    [39m
    [32mimport [39m[36mskac.miro.attribs._
    [39m
    [32mimport [39m[36mskac.miro.attribs.colors._
    [39m
    [32mimport [39m[36mskac.miro._
    [39m
    [32mimport [39m[36mskac.fractalizer.nodes.partitioners._
    [39m
    [32mimport [39m[36mskac.fractalizer.stylers.colorizers._
    [39m
    [32mimport [39m[36mskac.fractalizer.Node._
    [39m
    [32mimport [39m[36mskac.fractalizer._
    [39m
    [32mimport [39m[36mskac.fractalizer.Palette._
    [39m
    [32mimport [39m[36mskac.fractalizer.nodes._
    [39m
    [32mimport [39m[36mskac.fractalizer._
    [39m
    [32mimport [39m[36mskac.fractalizer.nodes.filters._
    [39m
    [32mimport [39m[36mskac.fractalizer.nodes.stripes._
    [39m
    [32mimport [39m[36mskac.miro.graphics.compounds._
    [39m
    [32mimport [39m[36mscala.math._[39m




```scala212
import skac.fractalizer.nodes.partitioners.triangles._
val color = Color.red(.4)
val pal = hueSpan(color, 4)
val styler = RandomSelected(pal, .1, .1, .1)
val node = TriangleFour(Some(styler), .45, .6, .55) * 3 + DrawCut()
val tp = (Triangle(), ori)
val out = node((Seq(tp), Nil))._2
val g = Group(out)
val d = new Draw()
d.strDoc((g, ori))

```




![svg](output_2_0.svg)




```scala212
import skac.fractalizer.nodes.partitioners.triangles._
import scala.math._
val color = Color.red(.4)
val pal = hueSpan(color, 4)
val styler = RandomSelected(pal, .1, .1, .1)
val nodes: Seq[Node] = (0 to 3) map {_ => TriangleFour(Some(styler), random, random, random)}
val node1 = nodes reduce {_ + _} 
val node = node1 + DrawCut()
val tp = (Triangle(), ori)
val out = node((Seq(tp), Nil))._2
val g = Group(out)
val d = new Draw()
d.strDoc((g, ori))
```




![svg](output_3_0.svg)




```scala212
import skac.fractalizer.nodes.partitioners.triangles._
import scala.math._

object TriangleFour1 {
  val rand = scala.util.Random
  
  def randVal = max(min(.5 + rand.nextGaussian * .05, 1), 0)
    
  def apply() = {
//       val color = Color.red(.4)
//       val pal = hueSpan(color, 4)
           
      val nodes: Seq[Node] = (0 to 5) map {i => 
          val disp_base = .1 / (i + 1)
          val styler = Disperse(4, 9 * disp_base, .4 * disp_base, disp_base)
          TriangleFour(Some(styler), randVal, randVal, randVal)}
      val node1 = nodes reduce {_ + _} 
      val node = node1 + DrawCut()
      val tp = (Triangle() fill Color.green(.6), ori)
      val out = node((Seq(tp), Nil))._2
      val g = Group(out)
      val d = new Draw()
      d.strDoc((g, ori))
  }    
}

TriangleFour1()

```




![svg](output_4_0.svg)

