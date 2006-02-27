/*
 * EA.java
 *
 * Created on June 24, 2003, 21:00 PM
 *
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package net.sourceforge.cilib.EC.EA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Algorithm.InitialisationException;
import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.Algorithm.ParticipatingAlgorithm;
import net.sourceforge.cilib.Domain.Component;
import net.sourceforge.cilib.EC.Mutator.Mutator;
import net.sourceforge.cilib.EC.Reproducer.Reproducer;
import net.sourceforge.cilib.EC.Reproducer.UniformReproducer;
import net.sourceforge.cilib.EC.Selector.ElitismSelector;
import net.sourceforge.cilib.EC.Selector.RandomSelector;
import net.sourceforge.cilib.EC.Selector.Selector;
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationSolution;

public class EA extends Algorithm
                implements OptimisationAlgorithm, ParticipatingAlgorithm
{
  private OptimisationProblem problem = null;
  private Individual bestIndividual = null;
  private Population population = null;
  private Class geneType = DoubleGene.class;
  private int numberOfOffspring = 10;
  private Selector parentSelector = null;
  private Selector nextGenerationSelector = null;
  private Reproducer reproducer = null;
  private Mutator mutator = null;
  private double mutationProbability = 0.5;
  private double reproductionProbability = 0.5;
  /**
   * The larger the varianceStep, the smaller the change in the variance.
   */
  private double varianceStep = 10.0;

  public EA() {
    // intialise the problem to null.
    problem = null;

    // initialise the default geneClass to DoubleGene.
    geneType = DoubleGene.class;

    // initialise the best individual to null.
    bestIndividual = null;

    // create a default population of individuals.
    population = new Population();

    // initialise the selector to be null.
    parentSelector = new RandomSelector();

    // intialise the next generation selector to be Elitism.
    nextGenerationSelector = new ElitismSelector();

    // intialise the reproducer to be the Uniform reproducer.
    reproducer = new UniformReproducer();
  }

  public void initialise()
  {
    super.initialise();

    if (problem == null) {
      throw new InitialisationException("No problem has been specified");
    }

    // initialise the memory for the individuals in the population.
    population.initialise();

    // initialise the population of individuals.
    int index = 0;
    Iterator iterator = population.individuals();
    while (iterator.hasNext()) {
      Individual individual = (Individual)iterator.next();

      // we do not need to set the individuals id, as it is set in the population.
      individual.setEa(this);
      individual.setDimension(problem.getDomain().getDimension());

      // initialise the gene value for the individual.
      for (int j = 0; j < problem.getDomain().getDimension(); ++j) {
        Component domain = problem.getDomain().getComponent(j);
        individual.initialise(j, domain);
      }
      individual.setMutationProbability(mutationProbability);
      individual.reset();
    }
  }

  protected void performIteration() {
    // evaluate the fitness of each individual.
    Iterator iterator = population.individuals();
    while (iterator.hasNext()) {
      // get the individual.
      Individual individual = (Individual)iterator.next();

      // evaluate the fitness of the individual.
      Fitness fitness = evaluateFitness(individual);

      // set the individuals fitness.
      individual.setFitness(fitness);
    }

    // get the size of the population, which will be used to initialise the ID
    // for the individuals.
    int populationSize = population.getSize();

    // Add all the offspring created to a vector, which will be used to add
    // the offspring to the current population.
    Vector newIndividuals = new Vector();

    // generate the new offspring.
    while(newIndividuals.size() < numberOfOffspring) {
      // select two parents from the population.
      Vector parents = getParentSelector().select(population, 2);

      // perform crossover to generate the new offspring.
      Vector offspring = new Vector();
      if (Math.random() < getReproductionProbability()) {
        offspring = getReproducer().reproduce(parents);
      }

      // initialise the new offspring.
      Iterator offspringIterator = offspring.iterator();
      long index = population.getLastIndex();
      while(offspringIterator.hasNext()) {
        Individual child = (Individual)offspringIterator.next();

        // set the EA algorithm.
        child.setEa(this);

        // set the id of the individual.
        // child.setID(index + newIndividuals.size() + 1);

        // set the mutation probability of the child.
        child.setMutationProbability(getMutationProbability());

        // reset the childs fitness
        child.reset();

        // calculate the variance for the mutation.
        double variance = 1.0 - Math.exp(-1.0/(getVarianceStep() + getIterations()));
        variance -= getMutator().getVariance();

        // mutate the new child.
        child.mutate(variance);

        // evaluate the fitness of the child.
        Fitness fitness = evaluateFitness(child);
        child.setFitness(fitness);

        // add the child to the vector of new individuals created.
        newIndividuals.add(child);
      }
    }

//    // calculate the variance for the mutation.
//    double variance = 1.0 - Math.exp(1.0/(getVarianceStep() + getIterations()));
//    variance -= getMutator().getVariance();
//
//    // mutate the entire population.
//    iterator = population.individuals();
//    while(iterator.hasNext())
//    {
//      Individual individual = (Individual)iterator.next();
//      individual.mutate(variance);
//    }

    // add the offspring to the current population.
    Iterator newIndividualIterator = newIndividuals.iterator();
    while(newIndividualIterator.hasNext()) {
      population.addIndividual((Individual)newIndividualIterator.next());
    }

    // select the next generation using the original populationSize.
    Vector nextPopulation = getNextGenerationSelector().select(population, populationSize);

    // set the next population.
    population.setIndividuals(nextPopulation);
  }

  protected Fitness evaluateFitness(Individual individual) {

    // get the genes of the individual into a double[] to evaluate the individual.
    // this implementation is only for DoubleGene.
    Gene[] genes = (Gene[])individual.getChromosome();

    // convert the DoubleGene[] into a double[].
    double[] position = new double[genes.length];
    for (int i=0; i<genes.length; i++) {
      position[i] = genes[i].getGeneValue();
    }
    Fitness fitness = problem.getFitness(position, true
    		);
    return fitness;
  }

  public void setBestIndividual(Individual bestIndividual) {
    this.bestIndividual = bestIndividual;
  }

  public Individual getBestIndividual() {
    if (problem == null) {
      throw new RuntimeException("Optimisation problem not set - cannot find best individual");
    }
    // get the individuals from the population.
    Iterator iterator = population.individuals();

    if (getPopulation().isEmpty()) {
      
      throw new RuntimeException("Empty population - cannot find best individual");
    }

    if (bestIndividual == null) {
      // set the best individual to be the first individual in the population.
      bestIndividual = (Individual)iterator.next();
    }

    while(iterator.hasNext()) {
      Individual i = (Individual)iterator.next();
      if (i.getBestFitness().compareTo(bestIndividual.getBestFitness()) > 0) {
        bestIndividual = i;
      }
    }
    return bestIndividual;
  }

  /**
   * Sets the optimisation problem to be solved.
   *
   * @param problem The {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter} to be solved.
   */
  public void setOptimisationProblem(OptimisationProblem problem) {
    this.problem = problem;
  }

  /**
   * Accessor for the optimisation problem to be solved.
   *
   * @return The {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter} to be solved.
   */
  public OptimisationProblem getOptimisationProblem() {
    return problem;
  }

  /**
   * Returns the best solution found by the algorithm so far.
   *
   * @return A double array of length {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter#getDimension()} that represents the best solution found.
   */
  public OptimisationSolution getBestSolution() {
    bestIndividual = getBestIndividual();
    Gene[] solutionGenes = (Gene[])bestIndividual.getBestChromosome();

    // convert the DoubleGene[] into a double[].
    double[] solution = new double[solutionGenes.length];
    for (int i=0; i<solution.length; i++) {
      solution[i] = solutionGenes[i].getGeneValue();
    }

    return new OptimisationSolution(problem, solution);
  }

  /**
   * Returns the fitness of the best solution found by the algorithm.
   *
   * @return The best fitness.
   */
  public Fitness getSolutionFitness() {
    bestIndividual = getBestIndividual();
    return bestIndividual.getBestFitness();
  }

  /**
  * Returns contribution to the solution for the co-operative optimisation algorithm.
  *
  * @return The algorithm's solution contribution.
  */
  public double[] getContribution() {
    Gene[] solutionGenes =  (Gene[])getBestIndividual().getChromosome();
    double[] solution = new double[solutionGenes.length];
    for (int i=0; i<solution.length; i++) {
      solution[i] = solutionGenes[i].getGeneValue();
    }
    return solution;
  }

  /**
   * Returns the fitness of contribution to the solution.
   *
   * @return The fitness of the solution contribution.
   */
  public Fitness getContributionFitness() {
    return getBestIndividual().getBestFitness();
  }

  /**
   * Updates the new fitness for the solution contribution.
   *
   * @param fitness The new fitness of the contribution.
   */
  public void updateContributionFitness(Fitness fitness) {
    getBestIndividual().setBestFitness(fitness);
  }

  public void setGeneType(Class geneType) {
    this.geneType = geneType;
  }

  public Class getGeneType() {
    return geneType;
  }

  public void setProblem(OptimisationProblem problem) {
    this.problem = problem;
  }

  public OptimisationProblem getProblem() {
    return problem;
  }

  public void setNumberOfOffspring(int numberOfOffspring) {
    this.numberOfOffspring = numberOfOffspring;
  }

  public int getNumberOfOffspring() {
    return numberOfOffspring;
  }

  public void setParentSelector(Selector selector) {
    this.parentSelector = selector;
  }

  public Selector getParentSelector() {
    return parentSelector;
  }

  public void setReproducer(Reproducer reproducer) {
    this.reproducer = reproducer;
  }

  public Reproducer getReproducer() {
    return reproducer;
  }

  public void setMutator(Mutator mutator) {
    this.mutator = mutator;
  }

  public Mutator getMutator() {
    return mutator;
  }

  public void setNextGenerationSelector(Selector nextGenerationSelector) {
    this.nextGenerationSelector = nextGenerationSelector;
  }

  public Selector getNextGenerationSelector() {
    return nextGenerationSelector;
  }

  public void setPopulation(Population population) {
    this.population = population;
  }

  public Population getPopulation()  {
    return population;
  }

  public void setMutationProbability(double mutationProbability) {
    this.mutationProbability = mutationProbability;
  }

  public double getMutationProbability() {
    return mutationProbability;
  }

  public void setReproductionProbability(double reproductionProbability) {
    this.reproductionProbability = reproductionProbability;
  }

  public double getReproductionProbability() {
    return reproductionProbability;
  }

  public void setVarianceStep(double varianceStep) {
    this.varianceStep = varianceStep;
  }

   public double getVarianceStep() {
    return varianceStep;
  }
  
  public java.util.Collection getSolutions() {
      ArrayList solutions = new ArrayList(1);
      solutions.add(this.getBestSolution());
      return solutions;
  }
  
}
