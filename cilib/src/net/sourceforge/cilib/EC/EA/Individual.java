/*
 * Individual.java
 * 
 * Created on June 24, 2003, 21:00 PM
 * 
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
 * Computational Intelligence Research Group(CIRG@UP) 
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

import net.sourceforge.cilib.Algorithm.InitialisationException;
import net.sourceforge.cilib.EC.Mutator.Mutator;
import net.sourceforge.cilib.Problem.Domain;
public class Individual implements Comparable {

    protected Gene chromosome[] = null;
    protected Gene bestChromosome[] = null;
    protected double fitness = -Double.MAX_VALUE;
    protected double bestFitness = -Double.MAX_VALUE;
    protected Class geneClass = null;
    protected int dimension;
    protected double mutationProbability;
    protected long ID = System.currentTimeMillis();
    private EA ea = null;

    public Individual() {

        // The default chromosome class is the DoubleChromosome class
        setGeneClass(DoubleGene.class);

        // The default dimension will be set to 0.
        setDimension(0);

        // The default mutation probability
        setMutationProbability(0.5);
    }

    public void reset() {
        fitness = Double.NEGATIVE_INFINITY;
        bestFitness = Double.NEGATIVE_INFINITY;
    }

    public void initialise(int component, Domain domain) {

        if (chromosome == null) {
            throw new RuntimeException("chromosome is null - cannot initialise");
        }

        try {
            // create a new instance of the specified gene class.
            chromosome[component] = (Gene) getGeneClass().newInstance();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        try {
            chromosome[component] = (Gene) getGeneClass().newInstance();
            bestChromosome[component] = (Gene) getGeneClass().newInstance();
        }
        catch (Exception ex) {
            throw new InitialisationException("Could not initialise Individual.");
        }

        // initialise the chromosome at the specified component.
        chromosome[component].initialise(domain);

        // copy the value into the best chromosome.
        bestChromosome[component].setGeneValue(chromosome[component]);
    }

    public void mutate() {
        // muatate each gene in the chromosome.
        for (int i = 0; i < dimension; i++) {
            chromosome[i].mutate(mutationProbability);
        }
    }

    public void mutate(double variance) {
        // muatate each gene in the chromosome.
        for (int i = 0; i < dimension; i++) {
            Mutator mutator = chromosome[i].getMutator();
            mutator.setVariance(variance);
            chromosome[i].setMutator(mutator);
            chromosome[i].mutate(mutationProbability);
        }

    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public void setGeneClass(Class geneClass) {
        this.geneClass = geneClass;
    }

    public Class getGeneClass() {
        return geneClass;
    }

    public void setDimension(int dimension) {
        // set the dimension.
        this.dimension = dimension;

        // initialise the chromosome array.
        chromosome = new Gene[dimension];

        // initialise the best chromosome array.
        bestChromosome = new Gene[dimension];
    }

    public int getDimension() {
        return dimension;
    }

    public void setChromosome(Gene[] chromosome) {

        if (this.chromosome == null
            || this.chromosome.length != chromosome.length) {
            setDimension(chromosome.length);
            for (int i = 0; i < chromosome.length; i++) {
                this.chromosome[i] = chromosome[i];
            }
        }
        else {
            for (int i = 0; i < chromosome.length; i++) {
                this.chromosome[i] = chromosome[i];
            }
        }
    }

    public Gene[] getChromosome() {
        return chromosome;
    }

    public Gene[] getBestChromosome() {
        return bestChromosome;
    }

    public void setBestChromosome(Gene[] bestChromosome) {
        for (int i = 0; i < bestChromosome.length; i++) {
            this.bestChromosome[i].setGeneValue(bestChromosome[i]);
        }
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;

        if (fitness > bestFitness) {
            bestFitness = fitness;
            setBestChromosome(chromosome);
        }

    }

    public double getFitness() {
        return fitness;
    }

    public void setBestFitness(double bestFitness) {
        this.bestFitness = bestFitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public void setEa(EA ea) {
        this.ea = ea;
    }

    public EA getEa() {
        return ea;
    }

    public int compareTo(Object o2) {
        // cast the o2 to an Individual.
        Individual individual2 = (Individual) o2;
        // compare the individuals fitness
        if (getBestFitness() < individual2.getBestFitness()) {
            return 1;
        }
        else if (getBestFitness() > individual2.getBestFitness()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
