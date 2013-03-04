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
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.GaussianDistribution;

/**
 * This is an adaptation strategy described by Qin and Suganthan in the following article:
 *
 * This adaptive strategy can be found in the following article:
 *
 * @INPROCEEDINGS{1554904,
 * author={Qin, A.K. and Suganthan, P.N.},
 * booktitle={Evolutionary Computation, 2005. The 2005 IEEE Congress on}, title={Self-adaptive differential evolution algorithm for numerical optimization},
 * year={2005},
 * month={sept.},
 * volume={2},
 * number={},
 * pages={ 1785 - 1791 Vol. 2}}
 */
public class SaDEParameterAdaptationStrategy implements ParameterAdaptationStrategy{
    private ArrayList<Double> learningExperience;
    private double mean;
    private GaussianDistribution random;
    private RandomBoundedParameterInitialisationStrategy initialisationStrategy;

    /*
     * Default constructor for SaDEParameterAdaptationStrategy
     */
    public SaDEParameterAdaptationStrategy() {
        learningExperience = new ArrayList<Double>();
        mean = 0.0;
        random = new GaussianDistribution();
        random.setMean(ConstantControlParameter.of(0.5));
        random.setDeviation(ConstantControlParameter.of(0.1));
        initialisationStrategy = new RandomBoundedParameterInitialisationStrategy();
    }

    /*
     * Copy constructor for SaDEParameterAdaptationStrategy
     * @param copy The SaDEParameterAdaptationStrategy to be copied
     */
    public SaDEParameterAdaptationStrategy(SaDEParameterAdaptationStrategy copy) {
        learningExperience = (ArrayList<Double>) copy.learningExperience.clone();
        mean = copy.mean;
        random = copy.random;
        initialisationStrategy = copy.initialisationStrategy.getClone();
    }

    /*
     * Clone method for SaDEParameterAdaptationStrategy
     * @return The new instance of this SaDEParameterAdaptationStrategy
     */
    public ParameterAdaptationStrategy getClone() {
        return new SaDEParameterAdaptationStrategy(this);
    }

    /*
     * This method changes the value of the parameter sent to it.
     * In this case it samples a random number from a Gaussian
     * distribution with the given mean.
     * @param parameter The parameter to be changed
     */
    @Override
    public void change(SettableControlParameter parameter) {
        random.setMean(ConstantControlParameter.of(mean));
        initialisationStrategy.setRandom(random);

        SettableControlParameter newParameter = parameter.getClone();
        initialisationStrategy.initialise(newParameter);
        parameter.update(newParameter.getParameter());
    }

    /*
     * Recalculates the mean for future changes in the parameter
     * @return The new mean value
     */
    public double recalculateAdaptiveVariables() {
        if(learningExperience.size() > 0) {
            mean = 0.0;

            for(double value : learningExperience) {
                mean += value;
            }

            mean /=(double) learningExperience.size();

            learningExperience.clear();
        }

        return mean;
    }

    /*
     * Informs the SaDEParameterAdaptationStrategy that the offspring
     * generated using the parameter was accepted. It does this to gain
     * learning experience for future changes.
     * @param acceptedParameter The parameter that was accepted
     */

    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        if(accepted)
            learningExperience.add(parameter.getParameter());
    }

    /*
     * Returns the learning experience so far
     * @return The learning experience
     */
    public ArrayList<Double> getLearningExperience() {
        return learningExperience;
    }

    /*
     * Sets the learning experience to the one received as a parameter
     * @param learningExperience The new learningExperience
     */
    public void setLearningExperience(ArrayList<Double> learningExperience) {
        this.learningExperience = learningExperience;
    }

    /*
     * Returns the current value of the mean
     * @return The mean value
     */
    public double getMean() {
        return mean;
    }

    /*
     * Sets the value of the mean
     * @param mean The new mean value
     */
    public void setMean(double mean) {
        this.mean = mean;
    }

    /*
     * Returns the random provider
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
     * Returns the initialisation strategy used in the adaptation step.
     * @return The initialisation Strategy
     */
    public RandomBoundedParameterInitialisationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }

    /*
     * Sets the initialisation strategy tot he one received as a parameter
     * @param initialisationStrategy The new initialisation strategy
     */
    public void setInitialisationStrategy(RandomBoundedParameterInitialisationStrategy initialisationStrategy) {
        this.initialisationStrategy = initialisationStrategy;
    }

}
