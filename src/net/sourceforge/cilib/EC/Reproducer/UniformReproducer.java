/*
 * UniformReproducer.java
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

package net.sourceforge.cilib.EC.Reproducer;

import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.EC.EA.Individual;

public class UniformReproducer extends Reproducer {

    public UniformReproducer() {
    }

    public Vector reproduce(Vector parents) {
        // this reproducer only works with two parents.
        if (parents.size() != 2) {
            throw new RuntimeException("UniformReproducer inly works with two parents");
        }

        // get the iterator for the parent vector.
        Iterator iterator = parents.iterator();

        // get parent 1.
        Individual parent1 = (Individual) iterator.next();

        // get parent 2.
        Individual parent2 = (Individual) iterator.next();

        return uniform(parent1, parent2);
    }

    /**
     *
     * @param parent1 The first parent of the child.
     * @param parent2 The second parent of the child.
     * @return A vector containing two individuals created from parent1 and parent2
     * using a uniform mask to swap the genes of the individual.
     */
    protected Vector uniform(Individual parent1, Individual parent2) {
        // get the size of the parents.
        int dimension = parent1.getDimension();

        // create memory for a int array that will be used to select which
        // parent will be used for each gene of the new individuals.
        int mask1[] = new int[dimension];

        // initialise the mask so that only the first parent is selected.
        for (int i = 0; i < mask1.length; i++) {
            mask1[i] = 0;
        }

        // for each value of the mask, calculate a probability for each parent.
        for (int i = 0; i < dimension; i++) {
            double p = Math.random();
            if (p > 0.5) {
                mask1[i] = 1;
            }
        }

        // perform the crossover using mask1.
        Individual child1 = crossover(mask1, parent1, parent2);
        Individual child2 = crossover(mask1, parent2, parent1);

        // create a Vector for the returning result.
        Vector<Individual> result = new Vector<Individual>(2);
        result.add(child1);
        result.add(child2);
        return result;
    }
}
