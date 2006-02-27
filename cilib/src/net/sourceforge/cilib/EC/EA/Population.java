/*
 * Population.java
 *
 * Created on August 24, 2003, 21:00 PM
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import net.sourceforge.cilib.Algorithm.InitialisationException;

public class Population {
    private HashMap individuals = new HashMap();
    private int size = 20;
    private long lastIndex = 0;

    public Population() {
    }

    /**
     *
     * @param Individuals Individuals must already be initialised.
     */
    public Population(int populationSize) {
        this.size = populationSize;
    }

    public void initialise() {
        // create the individuals and add them to the vector of individuals.
        for (int i = 0; i < size; i++) {
            try {
                // create a new individual.
                // the indidual must be initialised in the Selector class that uses the population.
                Individual individual = new Individual();

                // set the individuals id.
                individual.setID(i);

                // add the individual to the individual vector.
                individuals.put(new Long(individual.getID()), individual);

                // set the last index.
                lastIndex = i;
            }
            catch (Exception e) {
                throw new InitialisationException("Could not instantiate particle");
            }
        }
    }

    /**
     * The id of the individuals will be changed so that they are unique within,
     * the population.
     * @param Individuals Individuals must already be initialised.
     */
    public void setIndividuals(Vector v_individuals) {
        // make an exact copy of the vector of individuals.
        Vector copy_individuals = (Vector) v_individuals.clone();
        //Vector copy_individuals = v_individuals;

        // clean out the original v_individual
        v_individuals.clear();

        // remove all elements in the population.
        individuals.clear();

        // iterate through the individuals and add them to the hashmap.
        int index = 0;
        Iterator iterator = copy_individuals.iterator();
        while (iterator.hasNext()) {
            Individual individual = (Individual) iterator.next();
            individual.setID(index);
            individuals.put(new Long(index), individual);
            index++;

            // set the last index.
            lastIndex = index;
        }

        this.size = copy_individuals.size();
    }

    public HashMap getIndividuals() {
        return individuals;
    }

    public int getSize() {
        return individuals.size();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addIndividual(Individual individual) {
        // get the individuals id.
        long id = individual.getID();

        // check if there is a object with same id in the hashmap.
        while (individuals.containsKey(new Long(id))) {
            long index = getLastIndex();
            id = index + 1;
            individual.setID(id);
        }

        individuals.put(new Long(id), individual);
    }

    public void removeIndividual(Individual individual) {
        // get the identifier of the individual.
        long id = individual.getID();

        // remove the individual from the population.
        if (individuals.remove(new Long(id)) == null) {
            throw new RuntimeException("individual does not exisit in the population");
        }
    }

    public Iterator individuals() {
        return individuals.values().iterator();
    }

    public long getLastIndex() {
        // the individuals are stored in the HashMap with their id's so we can
        // use the KeySet to obtain the largest id.
        Set keySet = individuals.keySet();
        Vector list = new Vector(keySet);

        // get the maximum element in the list.
        Long value = (Long) Collections.max(list);

        return value.longValue();
    }

    public double[] getCenter(int dimension) {
        if (individuals.isEmpty()) {
            throw new RuntimeException("population has no individuals");
        }
        // calculate the center of the niche.
        Iterator iterator = individuals();
        double[] center = new double[dimension];

        // find the best individual in the population.
        Individual bestIndividual = (Individual) iterator.next();
        Gene[] chromosome = bestIndividual.getChromosome();
        for (int i = 0; i < chromosome.length; i++) {
            center[i] = chromosome[i].getGeneValue();
        }
        while (iterator.hasNext()) {
            // get the individual from the niche.
            Individual individual = (Individual) iterator.next();
            if (individual.getBestFitness()
                > bestIndividual.getBestFitness()) {
                bestIndividual = individual;

                // add the contribution of the individual to the center of the niche.
                chromosome = bestIndividual.getChromosome();
                for (int i = 0; i < chromosome.length; i++) {
                    center[i] = chromosome[i].getGeneValue();
                }
            }
        }

        return center;
    }

    public boolean isEmpty() {
        return individuals.isEmpty();
    }
}
