/*
 * DeterministicCrowding.java
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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.Domain.Quantitative;
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.Problem.OptimisationSolution;
import net.sourceforge.cilib.Problem.Solution;

public class DeterministicCrowding extends EA implements OptimisationAlgorithm {
  private double nicheThreshold = 0.1;
  private double mergeThreshold = 0.1;
  private Vector niches = new Vector();

  public DeterministicCrowding() {
    super();
  }

  protected void performIteration() {
    // evaluate the fitness of each individual.
    Iterator iterator = getPopulation().individuals();
    while (iterator.hasNext()) {
      Individual individual = (Individual)iterator.next();
      Fitness fitness = evaluateFitness(individual);
      individual.setFitness(fitness);
    }

    // get the size of the population, which will be used to initialise the ID
    // for the individuals.
    int populationSize = getPopulation().getSize();

    long lastIndex = getPopulation().getLastIndex();

    // Add all the offspring created to a vector, which will be used to add
    // the offspring to the current population.
    Vector newIndividuals = new Vector();

    // generate the new offspring.
    while(newIndividuals.size() < getNumberOfOffspring()) {
      // select two parents from the population.
      Vector parents = getParentSelector().select(getPopulation(), 2);

      // perform crossover to generate the new offspring.
      Vector offspring = new Vector();
      if (Math.random() < getReproductionProbability()) {
        offspring = getReproducer().reproduce(parents);

        // deterministic crowding allows for only two children to be created
        // from the reproduction process.
        if (offspring.size() != 2) {
        	throw new RuntimeException("DC allows only 2 children to be created from the reproduction process");
        }

        // initialise the new offspring.
        Iterator offspringIterator = offspring.iterator();
        while(offspringIterator.hasNext())  {
          Individual child = (Individual)offspringIterator.next();

          // set the EA algorithm.
          child.setEa(this);

          // set the id of the individual.
          // child.setID(lastIndex + 1);

          // set the mutation probability of the child.
          child.setMutationProbability(getMutationProbability());

          // reset the childs fitness
          child.reset();

          // calculate the variance for the mutation.
          double variance = 1.0-Math.exp(-1.0/(getVarianceStep() + getIterations()));
          variance -= getMutator().getVariance();

          // mutate the new child.
          child.mutate(variance);

          // evaluate the fitness of the child.
          Fitness fitness = evaluateFitness(child);
          child.setFitness(fitness);

          // add the child to the vector of new individuals created.
          newIndividuals.add(child);
        }


        // get the iterator for the new children produced.
        offspringIterator = offspring.iterator();

        // get the iterator for the parents.
        Iterator parentIterator = parents.iterator();

        // get the parents and children.
        Individual parent1 = (Individual)parentIterator.next();
        Individual parent2 = (Individual)parentIterator.next();
        Individual offspring1 = (Individual)offspringIterator.next();
        Individual offspring2 = (Individual)offspringIterator.next();

        // get the distance from the first parent to the first child.
        double distance_p1c1 = distance(parent1, offspring1);

        // get the distance from the second parent to the second child.
        double distance_p2c2 = distance(parent2, offspring2);

        // get the distance from the first parent to the second child.
        double distance_p1c2 = distance(parent1, offspring2);

        // get the distance from the second parent to the first child.
        double distance_p2c1 = distance(parent2, offspring1);

        // determine whether the children replaces the parents.
        if (distance_p1c1 + distance_p2c2 < distance_p1c2 + distance_p2c1)  {
          // determine if child 1 will replace parent 1.
          if (offspring1.getFitness().compareTo(parent1.getFitness()) > 0) {
            // replace parent1 with child1. To do this, we must remove parent1
            // from the population.
            getPopulation().removeIndividual(parent1);
            getPopulation().addIndividual(offspring1);
          }
          // determine if child 2 will replace parent 2.
          if (offspring2.getFitness().compareTo(parent2.getFitness()) > 0) {
            // replace paret2 with child2. To do this we must remove parent2
            // from the population.
            getPopulation().removeIndividual(parent2);
            getPopulation().addIndividual(offspring2);
          }
        }
        else {
          // determine if child2 will replace parent1.
          if (offspring2.getFitness().compareTo(parent1.getFitness()) > 0) {
            // replace parent1 with child2. To do this we must remove parent1
            // from the population.
            getPopulation().removeIndividual(parent1);
            getPopulation().addIndividual(offspring2);
          }
          // determine if child1 will replace parent2.
          if (offspring1.getFitness().compareTo(parent2.getFitness()) > 0) {
            // replace parent2 with child1. To do this we must remove parent2
            // from the population.
            getPopulation().removeIndividual(parent2);
            getPopulation().addIndividual(offspring1);
          }
        }
      }
    }
  }

  protected double distance(Individual p1, Individual p2) {
    Gene[] gene1 = p1.getChromosome();
    Gene[] gene2 = p2.getChromosome();

    double distance = 0.0;
    for (int i=0; i<gene1.length; i++) {
      distance += Math.pow(gene1[i].getGeneValue() - gene2[i].getGeneValue(), 2.0);
    }
    return Math.sqrt(distance);
  }

  protected double distance(double[] c1, double[] c2) { // TODO: Refactor this and other distance measures into a strategy
    double distance = 0.0;
    for (int i=0; i<c1.length; i++) {
      distance += Math.pow(c1[i] - c2[i], 2.0);
    }
    return Math.sqrt(distance);
  }

  public Fitness[] getNicheSolutionFitness() {
    // identify the niches within the population.
    niches = identifyNiches(getPopulation());
    if (!niches.isEmpty()) {
      Fitness[] fitness = new Fitness[niches.size()];
      Iterator iterator = niches.iterator();
      int index = 0;
      while(iterator.hasNext()) {
        Population p = (Population)iterator.next();
        fitness[index] = getBestIndividual(p).getBestFitness();
        index++;
      }
      return fitness;
    }
    else {
      Fitness[] fitness = new Fitness[1];
      fitness[0] = getSolutionFitness();
      return fitness;
    }
  }

  // TODO: Make sure that getNichSolution()[0] is the best of the solutions
  public Solution getSolution() {
  	return new OptimisationSolution(getProblem(), getNicheSolution()[0]);
  }
  
  public Collection getSolutions() {
  	Collection list = new LinkedList();
  	double[][] solutions = getNicheSolution();
  	for (int i = 0; i < solutions.length; ++i) {
  		list.add(new OptimisationSolution(getProblem(), solutions[i]));
  	}
  	return list;
  }
  
  private double[][] getNicheSolution() {
    // identify the niches within the population.
    niches = identifyNiches(getPopulation());
    if (!niches.isEmpty()) {
      double[][] nicheSolution = new double[niches.size()][getOptimisationProblem().getDomain().getDimension()];

      // get the solution from each niche.
      Iterator iterator = niches.iterator();
      int index = 0;
      while (iterator.hasNext()) {
        Gene[] chromosome = getBestIndividual((Population)iterator.next()).getBestChromosome();
        for (int i=0; i<chromosome.length; i++) {
          nicheSolution[index][i] = chromosome[i].getGeneValue();
        }
        index++;
      }
      return nicheSolution;
    }
    else {
      double[][] nicheSolution = new double[1][getOptimisationProblem().getDomain().getDimension()];
      nicheSolution[0] = (double[]) getBestSolution().getPosition();
      return nicheSolution;
    }
  }

  private Vector identifyNiches(Population p) {
    // Niches are identified by using the fitness of the individual.  If the
    // individuals fitness value is less than the threshold, then a new niche
    // is created.
    // If the distance between a Individual and another individual is less than
    // the niche threshold then they belong to the same Niche.

    // iterate through the individals and determine which Niche they belong too.
    // Niches are represented by a Vector.
    Vector nicheVector = new Vector();
    Iterator iterator = p.individuals();
    if (getOptimisationProblem() instanceof FunctionMaximisationProblem) {
      while (iterator.hasNext()) {
        // get the individual from the population.
        Individual individual = (Individual)iterator.next();
        if (Math.abs(((Double) individual.getBestFitness().getValue()).doubleValue()) > getNicheThreshold()) {
          // create a new niche (which is a Population of individuals)
          Population niche = new Population();
          niche.addIndividual(individual);

          // add the niche to the indentified niche solutions.
          nicheVector.add(niche);
        }
      }
    }
    else {
      while(iterator.hasNext()) {
        // get the individual from the population.
        Individual individual = (Individual)iterator.next();
        if (Math.abs(((Double) individual.getBestFitness().getValue()).doubleValue()) < getNicheThreshold())
        {
          // create a new niche (which is a Population of individuals)
          Population niche = new Population();
          niche.addIndividual(individual);

          // add the niche to the indentified niche solutions.
          nicheVector.add(niche);
        }
      }
    }

    // try to merge the solutions.
    return mergeNiches(nicheVector);
  }

  protected Vector mergeNiches(Vector nicheVector) {
    // determine if the distance between one of the niche centers and another
    // niche center is less than the merge threshold.
    for(int i=0; i<nicheVector.size()-1; i++) {
      // get the population at index i.
      Population p_i = (Population)nicheVector.get(i);

      // find the center of the population i.
      double[] center_i = p_i.getCenter(getProblem().getDomain().getDimension());

      // determine if this population can be merged with any of the other populations.
      for(int j=i+1; j<nicheVector.size(); j++) {
        // get the population at index j.
        Population p_j = (Population)nicheVector.get(j);

        // calculate the center of the population j.
        double[] center_j = p_j.getCenter(getProblem().getDomain().getDimension());

        // determine if this population can be merged with population i.
        double d = distance(center_i, center_j);
        if (normalise(d) < getMergeThreshold()) {
          // merge the niche solutions.
          Population mergedNiche = mergeNiches(p_i, p_j);

          // add the merged solution to the results.
          nicheVector.add(mergedNiche);

          // remove population i from the niche vector.
          nicheVector.remove(p_i);

          // remove population j from the niche vector.
          nicheVector.remove(p_j);

          // set the i to begin at 0 for determining more niches to be merged.
          j = nicheVector.size();
          i = -1;
        }
      }
    }

    // return the result.
    return nicheVector;
  }

  protected double normalise(double dValue)  {
    Quantitative domain = (Quantitative) getOptimisationProblem().getDomain().getComponent(0);
    double max = domain.getUpperBound().doubleValue();
    dValue = dValue/max;
    //dValue = dValue*Math.sqrt(3.0)*2.0 - Math.sqrt(3.0);
    return dValue;
  }

  public Population mergeNiches(Population p1, Population p2) {
    // create a new Population.
    Population p = new Population();

    // add the individuals from population p1 to the new population.
    Iterator iterator1 = p1.individuals();
    while(iterator1.hasNext()) {
      p.addIndividual((Individual)iterator1.next());
    }

    // add the individuals from population p2 to the new population.
    Iterator iterator2 = p2.individuals();
    while(iterator2.hasNext()) {
      p.addIndividual((Individual)iterator2.next());
    }

    // return the new population.
    return p;
  }

  public Individual getBestIndividual(Population p) {
    if (getProblem() == null) {
      throw new RuntimeException("Optimisation problem is not set - cannot find best individualt");
    }
    // get the individuals from the population.
    Iterator iterator = p.individuals();

    if (p.isEmpty()) {
      throw new RuntimeException("Empty population - cannot fund best individual");
    }

    // set the best individual to be the first individual in the population.
    Individual bestIndividual = (Individual)iterator.next();

    while(iterator.hasNext()) {
      Individual i = (Individual)iterator.next();
      if (i.getBestFitness().compareTo(bestIndividual.getBestFitness()) > 0) {
        bestIndividual = i;
      }
    }
    return bestIndividual;
  }

  public void setNicheThreshold(double nicheThreshold) {
    this.nicheThreshold = nicheThreshold;
  }

  public double getNicheThreshold() {
    return nicheThreshold;
  }

  public void setMergeThreshold(double mergeThreshold) {
    this.mergeThreshold = mergeThreshold;
  }

  public double getMergeThreshold()  {
    return mergeThreshold;
  }
  
}