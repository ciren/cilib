/*
 * Reproducer.java
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

import java.util.Vector;

import net.sourceforge.cilib.EC.EA.Gene;
import net.sourceforge.cilib.EC.EA.Individual;
import net.sourceforge.cilib.Problem.Domain;

public abstract class Reproducer {
    public abstract Vector reproduce(Vector parents);

    protected Individual crossover(
        int mask[],
        Individual parent1,
        Individual parent2) {
        // create a new Individual.
        Individual child = new Individual();

        // set the dimension for the child.
        child.setDimension(parent1.getDimension());

        // initialise the new Individaul.  This will create memory for the
        // individuals gene values.
        for (int i = 0; i < child.getDimension(); i++) {
            child.initialise(i, new Domain(-1.0, 1.0));
        }

        // set the mutation probability for the child.
        child.setMutationProbability(parent1.getMutationProbability());

        // set the gene class for the child.
        child.setGeneClass(parent1.getGeneClass());

        // Get the first chromosome from parent 1
        Gene[] chromosome1 = parent1.getChromosome();

        // Get the first chromosome from parent 2
        Gene[] chromosome2 = parent2.getChromosome();

        // create a gene[] for the new child.
        Gene[] chromosome = new Gene[child.getDimension()];
        Gene[] b_chromosome = new Gene[child.getDimension()];
        if (chromosome.length != mask.length) {
            throw new RuntimeException("chromosome length != mask length");
        }
        for (int i = 0; i < chromosome.length; i++) {
            try {
                chromosome[i] = (Gene) child.getGeneClass().newInstance();
                b_chromosome[i] = (Gene) child.getGeneClass().newInstance();
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            if (mask[i] == 0) {
                chromosome[i].setGeneValue(chromosome1[i]);
                b_chromosome[i].setGeneValue(chromosome1[i]);
            }
            else {
                chromosome[i].setGeneValue(chromosome2[i]);
                b_chromosome[i].setGeneValue(chromosome2[i]);
            }
        }
        // set the chromosome for the new child.
        child.setChromosome(chromosome);

        // set the best chromosome for the new child.
        child.setBestChromosome(b_chromosome);

        // return the new child.
        return child;
    }
}
