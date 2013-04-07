/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.io.transform.DoNothingDataOperator;
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
    protected StandardPatternDataTable generalisationSet;
    protected StandardPatternDataTable validationSet;
    protected double trainingSetPercentage;
    protected double generalisationSetPercentage;
    protected double validationSetPercentage;
    protected DataOperator shuffler;
    protected DataOperator patternConversionOperator;

    /**
     * Default constructor.
     */
    public NNTrainingProblem() {
        neuralNetwork = new NeuralNetwork();
        trainingSetPercentage = 0.66;
        generalisationSetPercentage = 0.34;
        validationSetPercentage = 0.0;
        patternConversionOperator = new PatternConversionOperator();
        shuffler = new DoNothingDataOperator();
    }

    public NNTrainingProblem(NNTrainingProblem rhs) {
        super(rhs);
        neuralNetwork = new NeuralNetwork(rhs.neuralNetwork);
        trainingSetPercentage = rhs.trainingSetPercentage;
        generalisationSetPercentage = rhs.generalisationSetPercentage;
        validationSetPercentage = rhs.validationSetPercentage;
        patternConversionOperator = rhs.patternConversionOperator;
        trainingSet = rhs.trainingSet.getClone();
        generalisationSet = rhs.generalisationSet.getClone();
        validationSet = rhs.validationSet.getClone();
        shuffler = rhs.shuffler.getClone();
    }	

    /**
     * Initialises the problem by setting up the datasets: has to be implemented by inheriting classes.
     */
    public abstract void initialise();

    /**
     * Gets the generalisation dataset.
     * @return the generalisation dataset.
     */
    public StandardPatternDataTable getGeneralisationSet() {
        return generalisationSet;
    }

    /**
     * Sets the generalisation dataset.
     * @param generalisationSet the new generalisation dataset.
     */
    public void setGeneralisationSet(StandardPatternDataTable generalisationSet) {
        this.generalisationSet = generalisationSet;
    }

    /**
     * Gets the percentage of the training set to use for generalisation.
     * @return the percentage of the training set to use for generalisation.
     */
    public double getGeneralisationSetPercentage() {
        return generalisationSetPercentage;
    }

    /**
     * Sets the percentage of the training set to use for generalisation.
     * @param generalisationSetPercentage the new percentage of the training set to use for generalisation.
     */
    public void setGeneralisationSetPercentage(double generalisationSetPercentage) {
        this.generalisationSetPercentage = generalisationSetPercentage;
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
    public DataOperator getShuffler() {
        return shuffler;
    }

    /**
     * Sets the {@link ShuffleOperator}
     * @param shuffler the new shuffle operator.
     */
    public void setShuffler(DataOperator shuffler) {
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
