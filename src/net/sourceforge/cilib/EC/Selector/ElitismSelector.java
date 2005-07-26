/*
 * ElitismSelector.java
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

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import net.sourceforge.cilib.EC.EA.Individual;
import net.sourceforge.cilib.EC.EA.Population;

public class ElitismSelector extends Selector {

    public ElitismSelector() {
    }

    public Vector select(Population p, int sizeOfSelection) {
        // if the size of selection is greater than the size of the population,
        // then the selection from the population is not possible.
        if (p.getSize() < sizeOfSelection) {
            throw new RuntimeException("number of individuals to select is greater than size of the population");
        }
        Vector<Individual> result = new Vector<Individual>();

        // get the individuals from the population.
        List<Individual> individuals = new Vector<Individual>(p.getIndividuals().values());

        // sort the individuals in the population according to their best fitness
        // values.
        Collections.sort(individuals);

        // limit the size of the result to value specified by sizeOfSelection.
        for (int i = 0; i < sizeOfSelection; i++) {
            result.add(individuals.get(i));
        }

        // return the result
        return result;
    }
}
