/*
 * DoubleGene.java
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

// TODO: Fix all the random generator stuff in this class - it's a real mess

package net.sourceforge.cilib.EC.EA;

import java.util.Random;

import net.sourceforge.cilib.Domain.Component;
import net.sourceforge.cilib.Domain.Continuous;
import net.sourceforge.cilib.EC.Mutator.Mutator;
import net.sourceforge.cilib.Random.MersenneTwister;

public class DoubleGene extends Gene {

    /**
     * The value of this gene.  Initialised to 0.
     */

    double geneValue = 0.0;
    Random initialisationRandomGenerator = null;
    // TODO: Use plugable random initialisers as done in PSO

    public DoubleGene() {
        // set the Random generator.
        initialisationRandomGenerator = new MersenneTwister();
        geneValue = initialisationRandomGenerator.nextDouble();
    }

    public DoubleGene(double geneValue) {
        setGeneValue(geneValue);
    }

    public void mutate(double mutationProbability) {

        // calculate a random value between 0 and 1.

        double E = Math.random();
        // TODO: Use a simulation quality generator from Random package

        if (E < mutationProbability) {
            mutator.mutate(this);
        }

    }

    public void initialise(Component domain) {
        Continuous component = (Continuous) domain;
        double lower = component.getLowerBound().doubleValue() * Math.random();
        double upper = component.getUpperBound().doubleValue() * Math.random();

        // set the Random generator.

        initialisationRandomGenerator = new MersenneTwister();

        double alpha1 = initialisationRandomGenerator.nextDouble();
        double alpha2 = initialisationRandomGenerator.nextDouble();
        geneValue = alpha1 * upper;
        geneValue += alpha2 * lower;

    }

    public void setGeneValue(double geneValue) {
        this.geneValue = geneValue;
    }

    public double getGeneValue() {
        return geneValue;
    }

    public void setMutator(Mutator mutator) {
        this.mutator = mutator;
    }

    public Mutator getMutator() {
        return mutator;
    }

    public void setInitialisationRandomGenerator(Random initialisationRandomGenerator) {
        this.initialisationRandomGenerator = initialisationRandomGenerator;
    }

    public Random getInitialisationRandomGenerator() {
        return initialisationRandomGenerator;
    }

    public void setGeneValue(Gene gene) {
        this.geneValue = ((DoubleGene) gene).getGeneValue();
    }

}
