/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.initialisation.RandomBoundedParameterInitialisationStrategy;
import net.sourceforge.cilib.ec.SaDEIndividual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.GaussianDistribution;

/**
 * This is the weighted parameter adaptation strategy described by Zhenyu, Tang and Yao
 * in their 2008 paper "Self-adaptive Differential Evolution with Neighbourhood
 * Search". It is the implementation of the parameter adaptation strategy used
 * to adapt the crossover probability.
 */
public class SaCRWParameterAdaptationStrategy implements ParameterAdaptationStrategy{
    private ArrayList<Double> learningExperience;
    private ArrayList<Double> fitnessDifferences;
    private double fitnessDifference;
    private double crossoverMean;
    private GaussianDistribution random;
    private RandomBoundedParameterInitialisationStrategy initialisationStrategy;

    /*
     * Default constructor for the SaCRWParameterAdaptationStrategy
     */
    public SaCRWParameterAdaptationStrategy() {
        learningExperience = new ArrayList<Double>();
        fitnessDifferences = new ArrayList<Double>();
        fitnessDifference = 0;
        crossoverMean = 0.0;
        random = new GaussianDistribution();
        random.setMean(ConstantControlParameter.of(0.5));
        random.setDeviation(ConstantControlParameter.of(0.1));
        initialisationStrategy = new RandomBoundedParameterInitialisationStrategy();
    }

    /*
     * Copy constructor for the SaCRWParameterAdaptationStrategy
     * @param copy The SaCRWParameterAdaptationStrategy to be copied
     */
    public SaCRWParameterAdaptationStrategy(SaCRWParameterAdaptationStrategy copy) {
        learningExperience = (ArrayList<Double>) copy.learningExperience.clone();
        fitnessDifferences = (ArrayList<Double>) copy.fitnessDifferences.clone();
        fitnessDifference = copy.fitnessDifference;
        crossoverMean = copy.crossoverMean;
        random = copy.random;
        initialisationStrategy = copy.initialisationStrategy.getClone();
    }

    /*
     * Clone method for the SaCRWParameterAdaptationStrategy
     * @return A new instance of this SaCRWParameterAdaptationStrategy
     */
    public ParameterAdaptationStrategy getClone() {
        return new SaCRWParameterAdaptationStrategy(this);
    }

    /*
     * Changes the parameter value according to the Weighted crossover
     * rate CR self-adaptation strategy.
     * @param parameter The parameter to be changed
     */
    public void change(SettableControlParameter parameter) {
        random.setMean(ConstantControlParameter.of(crossoverMean));
        initialisationStrategy.setRandom(random);

        SettableControlParameter newParameter = parameter.getClone();
        initialisationStrategy.initialise(newParameter);
        parameter.update(newParameter.getParameter());
    }

    /*
     * Adds the value of a successful parameter to the learning experience as
     * well as the value by which the fitness has improved to a list of these
     * better fitness differences.
     * @param parameter The value of the parameter which was accepted/rejected
     * @param entity The entity that was accepted/rejected which holds the accepted/rejected parameter
     * @param accepted Whether the parameter was accepted or not
     */
    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        if(accepted) {
            learningExperience.add(parameter.getParameter());
            fitnessDifference = ((SaDEIndividual) entity).getPreviousFitness().getValue() -
                    entity.getFitness().getValue();
            fitnessDifferences.add(fitnessDifference);
        }
    }

    /*
     * Recalculates the value of the crossoverMean variable using the learningExperience
     * and the fitnessDifferences accumulated so far
     * @return The new value of the crossoverMean
     */
    public double recalculateAdaptiveVariables() {
        double fitnessSum = 0;
        int parameterIndex = 0;
        crossoverMean = 0;
        for(double weight : fitnessDifferences) {
            fitnessSum += weight;
        }

        if(fitnessSum != 0) {
            for(double parameterValue : learningExperience) {
                crossoverMean += (fitnessDifferences.get(parameterIndex) / fitnessSum) * parameterValue;
                parameterIndex++;
            }
        }
        learningExperience.clear();
        fitnessDifferences.clear();
        return crossoverMean;
    }

    /*
     * Returns the learningExperience, which is a list of all successful parameter values.
     * @return The learningExperience
     */
    public ArrayList<Double> getLearningExperience() {
        return learningExperience;
    }

    /*
     * Sets the learning experience to the list received as a parameter
     * @param learningExperience The new learningExperience
     */
    public void setLearningExperience(ArrayList<Double> learningExperience) {
        this.learningExperience = learningExperience;
    }

    /*
     * Returns the fitnessDifferences, which is a list of fitness improvement
     * values caused by successful parameters
     * @return The fitnessDifferences
     */
    public ArrayList<Double> getFitnessDifferences() {
        return fitnessDifferences;
    }

    /*
     * Sets the fitnessDifferences to the list received as a parameter
     * @param fitnessDifferences The new fitnessDifferences
     */
    public void setFitnessDifferences(ArrayList<Double> fitnessDifferences) {
        this.fitnessDifferences = fitnessDifferences;
    }

    /*
     * Returns the value of the crossoverMean
     * @return The value of the crossoverMean
     */
    public double getCrossoverMean() {
        return crossoverMean;
    }

    /*
     * Sets the value of the crossoverMean to the value received as a parameter
     * @param crossoverMean The new value for the crossoverMean
     */
    public void setCrossoverMean(double crossoverMean) {
        this.crossoverMean = crossoverMean;
    }

    /*
     * Returns the random provider used to generate a new value for the parameter
     * @return The random provider
     */
    public GaussianDistribution getRandom() {
        return random;
    }

    /*
     * Sets the random provider to the one received as a parameter
     * @param random The new random provider
     */
    public void setRandom(GaussianDistribution random) {
        this.random = random;
    }

    /*
     * Gets the initialisationStrategy used to change the parameter
     * @return The initialisationStrategy
     */
    public RandomBoundedParameterInitialisationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }

    /*
     * Sets the initialisationStrategy used to change the parameter to the one received
     * as a parameter
     * @param initialisationStrategy The initialisationStrategy
     */
    public void setInitialisationStrategy(RandomBoundedParameterInitialisationStrategy initialisationStrategy) {
        this.initialisationStrategy = initialisationStrategy;
    }

}
