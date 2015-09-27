package cilib

import scalaz._
import Scalaz._

object Runner {

  def repeat[A,B](n: Int, alg: Kleisli[Step[A,?],List[B],Result[B]], collection: RVar[List[B]]): Step[A,List[B]] = {
    Step.pointR(collection).flatMap(coll => (1 to n).toStream.foldLeftM[Step[A,?],List[B]](coll) { (a, c) =>
      alg.run(a).map(_.toList)
    })
  }
}
