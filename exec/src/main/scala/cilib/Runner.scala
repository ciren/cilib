package cilib

import scalaz._
import Scalaz._

/*final case class Execution[A,B](n: Int, alg: List[B] => Step[A,List[B]], collection: RVar[List[B]]) {
  def run(c: Comparison, e: Eval[A], r: RNG) = {
    Step.pointR(collection).flatMap(coll => (1 to n).toStream.foldLeftM[Step[A,?], List[B]](coll) { (a,c) => alg(a) })
  }
}*/

object Runner {
  def repeat[A,B](n: Int, alg: Iteration[Step[A,?],B], collection: RVar[List[B]]): Step[A,List[B]] =
    Step.pointR(collection).flatMap(coll => (1 to n).toStream.foldLeftM[Step[A,?], List[B]](coll) { (a,c) => alg.run(a) })
  
}
