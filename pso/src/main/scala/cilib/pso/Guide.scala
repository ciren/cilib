package cilib
package pso

import scalaz.NonEmptyList
import scalaz.Scalaz._

import spire.math.Interval

object Guide {

  def identity[S,F[_],A]: Guide[S,A] =
    (_, x) => Step.point(x.pos)

  def pbest[S,A](implicit M: Memory[S,A]): Guide[S,A] =
    (_, x) => Step.point(M._memory.get(x.state))

  def nbest[S](selection: Selection[Particle[S,Double]])(implicit M: Memory[S,Double]): Guide[S,Double] = {
    (collection, x) => Step.withCompare(o => RVar.point {
      val selected = selection(collection, x)
      val fittest = selected.map(e => M._memory.get(e.state)).reduceLeftOption((a, c) => Comparison.compare(a, c) run (o))
      fittest.getOrElse(sys.error("Impossible: reduce on entity memory worked on empty memory member"))
    })
  }

  def dominance[S](selection: Selection[Particle[S,Double]]): Guide[S,Double] = {
    (collection, x) => Step.withCompare(o => RVar.point {
      val neighbourhood = selection(collection, x)
      val comparison = Comparison.dominance(o.opt)
      val fittest = neighbourhood.map(_.pos).reduceLeftOption((a,c) => comparison.apply(a, c))
      fittest.getOrElse(sys.error("????"))
    })
  }

  def gbest[S](implicit M: Memory[S,Double]): Guide[S,Double] =
    nbest((c, _) => c)

  def lbest[S](n: Int)(implicit M: Memory[S,Double]) =
    nbest(Selection.indexNeighbours[Particle[S,Double]](n))

  def vonNeumann[S](implicit M: Memory[S,Double]) =
    nbest((c: List[Particle[S,Double]], a: Particle[S,Double]) => {
      val np = c.length
      val index = c.indexOf(a)
      val sqSide = math.round(math.sqrt(np.toDouble)).toInt
      val nRows = math.ceil(np / sqSide.toDouble).toInt
      val row = index / sqSide
      val col = index % sqSide

      val colsInRow = (r: Int) => if (r == nRows - 1) np - r * sqSide else sqSide
      def pos(n: Int, row: Int, col: Int) = c(row * n + col)

      List(
        a,
        pos(sqSide, (row - 1 + nRows) % nRows - (if (col >= colsInRow((row - 1 + nRows) % nRows)) 1 else 0), col), // north
        pos(sqSide, (if (col >= colsInRow((row + 1) % nRows)) 0 else (row + 1) % nRows), col), // south
        pos(sqSide, row, (col + 1) % colsInRow(row)), // east
        pos(sqSide, row, (col - 1 + colsInRow(row)) % colsInRow(row)) // west
      )
    })

  def nmpc[S](prob: Double): Guide[S,Double] =
    (collection, x) => {
      val col = collection.filter(_ != x)
      val chosen = RVar.sample(3, col).run
      val crossover = Crossover.nmpc

      for {
        chos     <- Step.pointR(chosen)
        pos      <- Step.point(x.pos)
        parents   = chos.map(c => NonEmptyList.nel(pos, c.map(_.pos).toIList))
        children <- parents.map(crossover).getOrElse(Step.point(NonEmptyList(pos)))
        probs    <- Step.pointR(pos.traverse(_ => Dist.stdUniform))
        zipped    = pos zip children.head zip probs
      } yield zipped.map { case ((xi, ci), pi) => if (pi < prob) ci else xi }
    }

  def pcx[S](s1: Double, s2: Double)(implicit M: Memory[S,Double]): Guide[S,Double] =
    (collection, x) => {
      val gb = gbest
      val pb = pbest
      val pcx = Crossover.pcx(s1, s2)

      for {
        p         <- pb(collection, x)
        i         <- identity(collection, x)
        n         <- gb(collection, x)
        parents    = NonEmptyList(p, i, n)
        offspring <- pcx(parents)
      } yield offspring.head
    }

  def undx[S](s1: Double, s2: Double)(implicit M: Memory[S,Double]): Guide[S,Double] =
    (collection, x) => {
      val gb = gbest
      val pb = pbest
      val undx = Crossover.undx(s1, s2)

      for {
        p         <- pb(collection, x)
        i         <- identity(collection, x)
        n         <- gb(collection, x)
        parents    = NonEmptyList(p, i, n)
        offspring <- undx(parents)
      } yield offspring.head
    }

}
