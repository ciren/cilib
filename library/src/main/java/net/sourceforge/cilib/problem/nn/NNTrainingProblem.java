/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.io.transform.ShuffleOperator;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.AbstractProblem;

/**
 * Abstract class represents an {@link net.sourceforge.cilib.problem.Problem} where the goal is to optimize
 * the set of weights of a neural network. Used as an interface to more specific training problems
 * such as training from a static dataset and sliding window training.
 */
public abstract class NNTrainingProblem extends AbstractProblem {
    protected NeuralNetwork neuralNetwork;
    protected StandardPatternDataTable trainingSet;
    protected StandardPatternDataTable generalizationSet;
    protected StandardPatternDataTable validationSet;
    protected double trainingSetPercentage;
    protected double generalizationSetPercentage;
    protected double validationSetPercentage;
    protected ShuffleOperator shuffler;
    protected DataOperator patternConversionOperator;

    /**
     * Default constructor.
     */
    public NNTrainingProblem() {
        neuralNetwork = new NeuralNetwork();
        trainingSetPercentage = 0.66;
        generalizationSetPercentage = 0.34;
        validationSetPercentage = 0.0;
        patternConversionOperator = new PatternConversionOperator();
    }

    /**
     * Initializes the problem by setting up the datasets: has to be implemeted by inheriting classes.
     */
    public abstract void initialise();

    /**
     * Gets the generalization dataset.
     * @return the generalization dataset.
     */
    public StandardPatternDataTable getGeneralizationSet() {
        return generalizationSet;
    }

    /**
     * Sets the generalization dataset.
     * @param generalizationSet the new generalization dataset.
     */
    public void setGeneralizationSet(StandardPatternDataTable generalizationSet) {
        this.generalizationSet = generalizationSet;
    }

    /**
     * Gets the percentage of the training set to use for generalization.
     * @return the percentage of the training set to use for generalization.
     */
    public double getGeneralizationSetPercentage() {
        return generalizationSetPercentage;
    }

    /**
     * Sets the percentage of the training set to use for generalization.
     * @param generalizationSetPercentage the new percentage of the training set to use for generalization.
     */
    public void setGeneralizationSetPercentage(double generalizationSetPercentage) {
        this.generalizationSetPercentage = generalizationSetPercentage;
    }

    /**
     * Gets the neural network.
     * @return the neural network.
     */
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    /**
     * Sets the neural network.
     * @param neuralNetwork the new neural network.
     */
    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    /**
     * Gets the training dataset.
     * @return the training dataset.
     */
    public StandardPatternDataTable getTrainingSet() {
        return trainingSet;
    }

    /**
     * Sets the training dataset.
     * @param trainingSet the new training dataset.
     */
    public void setTrainingSet(StandardPatternDataTable trainingSet) {
        this.trainingSet = trainingSet;
    }

    /**
     * Gets the percentage of the training set to use for training.
     * @return the percentage of the training set to use for training.
     */
    public double getTrainingSetPercentage() {
        return trainingSetPercentage;
    }

    /**
     * Sets the percentage of the training set to use for training.
     * @param trainingSetPercentage the new percentage of the training set to use for training.
     */
    public void setTrainingSetPercentage(double trainingSetPercentage) {
        this.trainingSetPercentage = trainingSetPercentage;
    }

    /**
     * Gets the validation dataset.
     * @return the validation dataset.
     */
    public StandardPatternDataTable getValidationSet() {
        return validationSet;
    }

    /**
     * Sets the validation dataset.
     * @param validationSet the new validation dataset.
     */
    public void setValidationSet(StandardPatternDataTable validationSet) {
        this.validationSet = validationSet;
    }

    /**
     * Gets the percentage of the training set to use for validation.
     * @return the percentage of the training set to use for validation.
     */
    public double getValidationSetPercentage() {
        return validationSetPercentage;
    }

    /**
     * Sets the percentage of the training set to use for validation.
     * @param validationSetPercentage the new percentage of the training set to use for validation.
     */
    public void setValidationSetPercentage(double validationSetPercentage) {
        this.validationSetPercentage = validationSetPercentage;
    }

    /**
     * Gets the {@link ShuffleOperator}
     * @return the shuffle operator.
     */
    public ShuffleOperator getShuffler() {
        return shuffler;
    }

    /**
     * Sets the {@link ShuffleOperator}
     * @param shuffler the new shuffle operator.
     */
    public void setShuffler(ShuffleOperator shuffler) {
        this.shuffler = shuffler;
    }

    /**
     * Get the { @link PatternConversionOperator}
     * @return the pattern conversion operator
     */
    public DataOperator getPatternConversionOperator() {
        return patternConversionOperator;
    }

    /**
     * Set the { @link PatternConversionOperator}
     * @param patternConverstionOperator the new pattern conversion operator
     */
    public void setPatternConversionOperator(DataOperator patternConverstionOperator) {
        this.patternConversionOperator = patternConverstionOperator;
    }
}
