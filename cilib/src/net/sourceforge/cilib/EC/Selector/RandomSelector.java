/*
 * RandomSelector.java
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

package net.sourceforge.cilib.EC.Selector;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import net.sourceforge.cilib.EC.EA.Individual;
import net.sourceforge.cilib.EC.EA.Population;

public class RandomSelector extends Selector {

    public RandomSelector() {
    }

    public Vector select(Population p, int sizeOfSelection) {
        // if the size of the selection is greater than the size of population
        // then the selection process is not possible.
        if (p.getSize() < sizeOfSelection) {
            throw new RuntimeException("size of selection is greater than size of population");
        }

        // to perform random selection from the population, we do not want to select
        // individuals from the population that have already been selected.
        // One way of selecting individuals from the population is to create a
        // a bit mask, where a 1 represents that the individual must be selected
        // and a 0 represents that the individual must not be selected.
        // The number of 1's in the bit mask need to be equal to the sizeOfSelection.
        int[] mask = new int[p.getSize()];
        for (int i = 0; i < mask.length; i++) {
            mask[i] = 0;
        }
        Vector<Integer> available = new Vector<Integer>();
        for (int i = 0; i < p.getSize(); i++) {
            available.add(new Integer(i));
        }
        boolean done = false;
        int numberAdded = 0;
        while (!done) {
            // Select a random number between 0 and the size of available.
            Random R = new Random(System.currentTimeMillis());
            int selection = R.nextInt(available.size() - 1);
            int position = ((Integer) available.get(selection)).intValue();

            // set the mask at position to 1.
            if (mask[position] == 0) {
                mask[position] = 1;

                // remove the available position.
                available.remove(selection);

                // increment the number of individuals added.
                numberAdded++;
            }
            else {
                // shouldnt get here, but if it does, remove the available position
                // as it is invalid.
                available.remove(selection);
            }

            // check if we have the number of selected individuals.
            if (numberAdded >= sizeOfSelection) {
                done = true;
            }
        }

        // we have created the bit mask, no selection will be alot easier,
        // as we need to select all the individuals where the mask is 1.
        int index = 0;
        Iterator iterator = p.individuals();
        Vector<Individual> result = new Vector<Individual>();
        while (iterator.hasNext()) {
            Individual individual = (Individual) iterator.next();
            if (mask[index] == 1) {
                result.add(individual);
            }
            index++;
        }
        // return the resulting vector of individuals.
        return result;
    }
}
