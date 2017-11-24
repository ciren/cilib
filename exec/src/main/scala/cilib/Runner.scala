package cilib

import scalaz._
import Scalaz._

object Runner {

  def repeat[A,B](n: Int, alg: Kleisli[Step[A,?],List[B],List[B]], collection: RVar[List[B]]): Step[A,List[B]] =
    Step.pointR(collection).flatMap(coll => (1 to n).toStream.foldLeftM[Step[A,?],List[B]](coll) { (a, _) =>
      alg.run(a).map(_.toList)
    })

  def repeatS[A,S,B](n: Int, alg: Kleisli[StepS[A,S,?],List[B],List[B]], collection: RVar[List[B]]): StepS[A,S,List[B]] =
    StepS.pointR(collection).flatMap(coll => (1 to n).toStream.foldLeftM[StepS[A,S,?],List[B]](coll) { (a, _) =>
      alg.run(a).map(_.toList)
    })
}
