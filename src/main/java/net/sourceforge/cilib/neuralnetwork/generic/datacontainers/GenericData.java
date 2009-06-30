/**
 * Copyright (C) 2003 - 2009
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
 */
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;

/**
 * @author stefanv
 *
 */
public class GenericData implements NeuralNetworkData {

    protected ArrayList<NNPattern> candidateSet = null;
    protected ArrayList<NNPattern> trainingSet = null;
    protected ArrayList<NNPattern> generalisationSet = null;
    protected ArrayList<NNPattern> validationSet = null;

    protected DataDistributionStrategy distributor;
    protected NeuralNetworkDataIterator iter = null;

    /**
     * Create an instance of {@linkplain GenericData}.
     */
    public GenericData() {
        this.candidateSet = null;
        this.trainingSet = null;
        this.generalisationSet = null;
        this.validationSet = null;

        this.distributor = null;
        this.iter = new LinearDataIterator(null);
    }

    /**
     * Perform the needed initialisation for the data object.
     */
    public void initialize(){
        candidateSet = new ArrayList<NNPattern>();
        trainingSet = new ArrayList<NNPattern>();
        generalisationSet = new ArrayList<NNPattern>();
        validationSet = new ArrayList<NNPattern>();

        if (this.distributor == null){
            throw new IllegalArgumentException("Required data distributor object was null during initialization");
        }

        this.distributor.initialize();
        this.distributor.populateData(this.candidateSet,
                                      this.trainingSet,
                                      this.generalisationSet,
                                      this.validationSet);
    }


    /**
     * Set the required {@linkplain DataDistributionStrategy}.
     * @param distributor The {@linkplain DataDistributionStrategy} to use.
     */
    public void setDistributor(DataDistributionStrategy distributor) {
        this.distributor = distributor;
    }

    /**
     * Get the size of the candidate set.
     * @return The candidate set size.
     */
    public int getCandidateSetSize() {
        return candidateSet.size();
    }

    /**
     * Get the size of the training set.
     * @return The training set size.
     */
    public int getTrainingSetSize() {
        return trainingSet.size();
    }

    /**
     * Get the size of the generalisation set.
     * @return The generalisation set size.
     */
    public int getGeneralisationSetSize() {
        return generalisationSet.size();
    }

    /**
     * Get the size of the validation set.
     * @return The validation set size.
     */
    public int getValidationSetSize() {
        return validationSet.size();
    }


    /**
     * Get an {@linkplain Iterator} for the training set.
     * @return The training set {@linkplain Iterator}.
     */
    public NeuralNetworkDataIterator getTrainingSetIterator() {
        return new LinearDataIterator(trainingSet);
    }


    /**
     * Get an {@linkplain Iterator} for the generalisation set.
     * @return The generalisation set {@linkplain Iterator}.
     */
    public NeuralNetworkDataIterator getGeneralisationSetIterator() {
        return new LinearDataIterator(generalisationSet);
    }


    /**
     * Get an {@linkplain Iterator} for the validation set.
     * @return The validation set {@linkplain Iterator}.
     */
    public NeuralNetworkDataIterator getValidationSetIterator() {
        return new LinearDataIterator(validationSet);
    }

    /**
     * Get an {@linkplain Iterator} for the candidate set.
     * @return The candidate set {@linkplain Iterator}.
     */
    public NeuralNetworkDataIterator getCandidateSetIterator() {
        return new LinearDataIterator(candidateSet);
    }


    /**
     * Update the active learning, based on the provided input.
     * @param input The input for the update.
     */
    public void activeLearningUpdate(Object input) {
        //Do nothing as no active learning defined.
        //refine in subclasses.
    }


    /**
     * Shuffle the training set.
     * @see Collections#shuffle(java.util.List, Random)
     */
    public void shuffleTrainingSet(){
        Collections.shuffle(this.trainingSet, new Random(System.currentTimeMillis()));
    }



    /**
     * Print some statistics about the data.
     */
    public void printStatistics() {
        System.out.println("Data distribution :\n\n");
        System.out.println("Candidate set size      : " + this.getCandidateSetSize());
        System.out.println("Training set size       : " + this.getTrainingSetSize());
        System.out.println("Generalisation set size : " + this.getGeneralisationSetSize());
        System.out.println("Validation set size     : " + this.getValidationSetSize());
    }


}
