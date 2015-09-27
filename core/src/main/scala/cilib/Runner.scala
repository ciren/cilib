package cilib

import scalaz._
import Scalaz._

object Runner {

  def repeat[F[_],A,B](n: Int, alg: Kleisli[Step[F,A,?],List[B],Result[B]], collection: RVar[List[B]]): Step[F,A,List[B]] = {
    Step.pointR(collection).flatMap(coll => (1 to n).toStream.foldLeftM[Step[F,A,?],List[B]](coll) { (a, c) =>
      alg.run(a).map(_.toList)
    })
  }
}
