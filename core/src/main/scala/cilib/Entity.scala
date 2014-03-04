package cilib

import scalaz._
import Scalaz._

final case class Entity(x: Solution, f: Fit)

object Entity {
  def mkEntity(bounds: List[Interval]) =
    bounds.traverse(b => Dist.uniform(b.lower, b.upper))

  def mkPopulation(n: Int, bounds: List[Interval]) =
    mkEntity(bounds) replicateM n
}

final class Interval(val lower: Double, val upper: Double) {
  def ^ (n: Int) =
    (1 to n).toVector.map(_ => this)
}

object Interval {
  def apply(l: Double, r: Double) =
    new Interval(l, r)
}
