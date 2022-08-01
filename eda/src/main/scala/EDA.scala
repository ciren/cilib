package cilib
package eda

import zio.prelude._

object EDA {

  def eda[M, S, A](
    sample: (M, Entity[S, A]) => RVar[Entity[S, A]],
    // selection: NonEmptyVector[Entity[S, A]] => RVar[NonEmptyVector[Entity[S, A]]],
    generateModel: NonEmptyVector[Entity[S, A]] => RVar[M]
  ): NonEmptyVector[Entity[S, A]] => Step[NonEmptyVector[Entity[S, A]]] =
    collection =>
      for {
        // selected <- Step.liftR(selection(collection))
        newModel  <- Step.liftR(generateModel(collection))
        generated <- Step.liftR(ForEach[NonEmptyVector].forEach(collection)(x => sample(newModel, x)))
        evaluated <- ForEach[NonEmptyVector].forEach(generated)(x => Step.eval(x)(identity))
      } yield evaluated

  def UDMAc[A](xs: NonEmptyVector[Position[Double]]): NonEmptyVector[RVar[Double]] = {
    val mean                                 = xs.map(p => p.sum / p.size)
    val mv: NonEmptyVector[(Double, Double)] = xs.zip(mean).map { case (vec, mean) =>
      (mean, vec.map(x => (x - mean) * (x - mean)).sum)
    }

    mv.map { case (mean, variance) =>
      Dist.gaussian(mean, variance)
    }
  }

  // private def test =
  //   eda(
  //     sample = (model: RVar[Double], entity: Entity[Unit, Double]) =>
  //       model
  //         .replicateM(entity.pos.length)
  //         .map(l =>
  //           Entity((), Position(l.toNel.getOrElse(sys.error("asdsad")), entity.pos.boundary))),
  //     selection = (l: NonEmptyList[Entity[Unit, Double]]) => RVar.pure(l),
  //     generateModel = (_: NonEmptyList[Entity[Unit, Double]]) => RVar.pure(Dist.stdUniform)
  //   )
}

/* Discrete EDA:

class EDA(numberOfIndividuals: Int, lengthOfIndividual: Int) {

  def generateInitialPopulation(): List[BitVector] = {
  // Generate a population of BitVectors where each bit is set with a probability of 0.5 - Uniform distribution of bits
    val mt: MersenneTwister = new MersenneTwister(System.currentTimeMillis().toInt)
    List.tabulate(numberOfIndividuals)(_ => BitVector.bits(for (_ <- 1 to lengthOfIndividual) yield mt.nextDouble() < 0.5))
  }

  def truncationSelection(population: List[BitVector], threshold: Double): List[BitVector] = {
    // Return the best 50% of the population. populationCount returns the number of set bits, our fitness function
    population.sortWith(_.populationCount > _.populationCount).dropRight(population.length / 2)
  }

  def probabilityVector(population: List[BitVector]): Array[Double] = {
    // Calculate the percentage of individuals with bit n set for all individuals, giving the probability vector
    val sums = Array.fill(lengthOfIndividual)(0.0)
    for (c <- population; i <- 0 until lengthOfIndividual) sums(i) += (if (c.get(i)) 1.0 else 0.0)
    sums.map(x => x / population.length)
  }

  def generateNewPopulation(p: Array[Double]): List[BitVector] = {
    // Generate a new population with bit n of each individual being set with a probability specified by element n of the probability vector
    val mt: MersenneTwister = new MersenneTwister(System.currentTimeMillis().toInt)
    List.tabulate(numberOfIndividuals)(_ => BitVector.bits(for (i <- 0 until lengthOfIndividual) yield mt.nextDouble() < p(i)))
  }
}
 */
