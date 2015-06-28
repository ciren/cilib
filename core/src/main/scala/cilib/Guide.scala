package cilib

object Guide {

  def identity[S,F[_],A]: Guide[S,F,A] =
    (_, x) => Step.point(x.pos)

  def pbest[S,F[_],A](implicit M: Memory[S,F,A]): Guide[S,F,A] =
    (_, x) => Step.point(M._memory.get(x.state))

  def nbest[S,F[_]](selection: Selection[Particle[S,F,Double]])(implicit M: Memory[S,F,Double]): Guide[S,F,Double] = {
    (collection, x) => Step.withOpt(o => RVar.point {
      selection(collection, x).
        map(e => M._memory.get(e.state)).
        reduceLeft((a, c) => Fitness.compare(a, c) run o)
    })
  }

  def gbest[S,F[_]](implicit M: Memory[S,F,Double]): Guide[S,F,Double] =
    nbest((c, _) => c)

  def lbest[S,F[_]](n: Int)(implicit M: Memory[S,F,Double]) =
    nbest(Selection.indexNeighbours[Particle[S,F,Double]](n))

}
