/*
 * NicheGA.java
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
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 */

package net.sourceforge.cilib.EC.EA;

import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.EC.Selector.ElitismSelector;

public class NicheGA
    extends DeterministicCrowding
    implements OptimisationAlgorithm {
    private Vector niches = new Vector();

    public NicheGA() {
    }

    protected void performIteration() {
        super.performIteration();

        // perform an iteration on all the GA in niches.
        Iterator nicheIterator = niches.iterator();
        while (nicheIterator.hasNext()) {
            EA ea = (EA) nicheIterator.next();
            ea.performIteration();
        }

        // identify all the niches within the population.
        Vector populationNiches = identifyNiches(getPopulation());
        Iterator populationIterator = populationNiches.iterator();
        while (populationIterator.hasNext()) {
            Population niche = (Population) populationIterator.next();

            // create a new EA with the population.
            EA ea = new EA();
            ea.setPopulation(niche);
            ea.setMutationProbability(getMutationProbability());
            ea.setMutator(getMutator());
            ea.setGeneType(getGeneType());
            ea.setNextGenerationSelector(new ElitismSelector());
            ea.setNumberOfOffspring(getNumberOfOffspring());
            ea.setOptimisationProblem(getOptimisationProblem());
            ea.setParentSelector(getParentSelector());
            ea.setProblem(getProblem());
            ea.setReproducer(getReproducer());
            ea.setReproductionProbability(getReproductionProbability());
            ea.setVarianceStep(getVarianceStep());

            // add the new ea to the niches.
            niches.add(ea);
        }

        // merge niches.
        mergeNiches();
    }

    public void mergeNiches() {
        Vector populationVector = new Vector();
        Iterator iterator = niches.iterator();
        while (iterator.hasNext()) {
            EA ea = (EA) iterator.next();
            populationVector.add(ea.getPopulation());
        }

        // merge the populations.
        mergeNiches(populationVector);

        // setup the niches.
        niches.clear();
        Iterator populationIterator = populationVector.iterator();
        while (populationIterator.hasNext()) {
            Population niche = (Population) populationIterator.next();

            // create a new EA with the population.
            EA ea = new EA();
            ea.setPopulation(niche);
            ea.setMutationProbability(getMutationProbability());
            ea.setMutator(getMutator());
            ea.setGeneType(getGeneType());
            ea.setNextGenerationSelector(new ElitismSelector());
            ea.setNumberOfOffspring(niche.getSize() * 2);
            ea.setOptimisationProblem(getOptimisationProblem());
            ea.setParentSelector(getParentSelector());
            ea.setProblem(getProblem());
            ea.setReproducer(getReproducer());
            ea.setReproductionProbability(getReproductionProbability());
            ea.setVarianceStep(getVarianceStep());

            // add the new ea to the niches.
            niches.add(ea);
        }
    }

    public double[] getNicheSolutionFitness() {
        if (!niches.isEmpty()) {
            double[] fitness = new double[niches.size()];
            Iterator iterator = niches.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                EA ea = (EA) iterator.next();
                fitness[index] = ea.getBestIndividual().getBestFitness();
                index++;
            }
            return fitness;
        }
        else {
            double[] fitness = new double[1];
            fitness[0] = getSolutionFitness();
            return fitness;
        }
    }

    public double[][] getNicheSolution() {
        if (!niches.isEmpty()) {
            double[][] nicheSolution =
                new double[niches
                    .size()][getOptimisationProblem()
                    .getDimension()];

            // get the solution from each niche.
            Iterator iterator = niches.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Gene[] chromosome =
                    ((EA) iterator.next())
                        .getBestIndividual()
                        .getBestChromosome();
                for (int i = 0; i < chromosome.length; i++) {
                    nicheSolution[index][i] = chromosome[i].getGeneValue();
                }
                index++;
            }
            return nicheSolution;
        }
        else {
            double[][] nicheSolution =
                new double[1][getOptimisationProblem().getDimension()];
            nicheSolution[0] = getBestSolution().getPosition();
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
        Vector individuals = new Vector(p.getIndividuals().values());
        for (int i = 0; i < individuals.size() - 2; i++) {
            // get the individual from the population.
            Individual individual = (Individual) individuals.get(i);
            if (Math.abs(individual.getBestFitness()) < getNicheThreshold()) {
                // find the individual that is closest to the current individual.
                Individual closestIndividual =
                    findClosestIndividual(individual, individuals);

                if (!individuals.removeElement(individual)) {
                    throw new RuntimeException("could not remove individual with id = " + individual.getID());
                }
                else {
                    i = -1;
                }
                if (!individuals.removeElement(closestIndividual)) {
                    throw new RuntimeException("could not remove closest individual with id = " + closestIndividual.getID());
                }
                else {
                    i = -1;
                }

                // create a new niche (which is a Population of individuals)
                Population niche = new Population();
                niche.addIndividual(individual);
                niche.addIndividual(closestIndividual);

                // add the niche to the indentified niche solutions.
                nicheVector.add(niche);
            }
        }

        p.setIndividuals(individuals);

        // try to merge the solutions.
        return mergeNiches(nicheVector);
    }

    //  private Vector mergeNiches(Vector nicheVector)
    //  {
    //    // determine if the distance between one of the niche centers and another
    //    // niche center is less than the merge threshold.
    //    for(int i=0; i<nicheVector.size()-1; i++)
    //    {
    //      // get the population at index i.
    //      Population p_i = (Population)nicheVector.get(i);
    //
    //      // find the center of the population i.
    //      double[] center_i = p_i.getCenter(getProblem().getDimension());
    //
    //      // determine if this population can be merged with any of the other populations.
    //      for(int j=i+1; j<nicheVector.size(); j++)
    //      {
    //        // get the population at index j.
    //        Population p_j = (Population)nicheVector.get(j);
    //
    //        // calculate the center of the population j.
    //        double[] center_j = p_j.getCenter(getProblem().getDimension());
    //
    //        // determine if this population can be merged with population i.
    //        double d = distance(center_i, center_j);
    //        if (normalise(d) < getMergeThreshold())
    //        {
    //          // merge the niche solutions.
    //          Population mergedNiche = mergeNiches(p_i, p_j);
    //
    //          // add the merged solution to the results.
    //          nicheVector.add(mergedNiche);
    //
    //          // remove population i from the niche vector.
    //          nicheVector.remove(p_i);
    //
    //          // remove population j from the niche vector.
    //          nicheVector.remove(p_j);
    //
    //          // set the i to begin at 0 for determining more niches to be merged.
    //          j = nicheVector.size();
    //          i = -1;
    //        }
    //      }
    //    }
    //
    //    // return the result.
    //    return nicheVector;
    //  }

    private Individual findClosestIndividual(
        Individual individual,
        Vector individuals) {
        // the closest individual is defined as that individual that has the closest
        // distance to the individual.
        if (individuals.size() < 2) {
            System.err.println(
                Thread.currentThread() + "> NicheGA.findClosestIndividual()");
            System.err.println(
                "Trying to find the closest individuals, but the population"
                    + "size is less than 2");
            Thread.dumpStack();
        }

        Iterator iterator = individuals.iterator();
        Individual closestIndividual = (Individual) iterator.next();
        double closestDistance = distance(individual, closestIndividual);
        while (closestIndividual.getID() == individual.getID()) {
            closestIndividual = (Individual) iterator.next();
            closestDistance = distance(individual, closestIndividual);
        }
        iterator = individuals.iterator();
        while (iterator.hasNext()) {
            Individual i = (Individual) iterator.next();
            double d = distance(individual, i);
            if (i.getID() != individual.getID() && d < closestDistance) {
                closestDistance = d;
                closestIndividual = i;
            }
        }
        return closestIndividual;
    }
}