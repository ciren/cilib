/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.generator.Rand;

/**
 * This is the self-adaptive version of NSDEParameterAdaptationStrategy, where
 * the scaling factor probability adapts as the parameter adapts.
 *
 * It can be found in Zhenyu, Tang and Yao's 2008 paper "Self-adaptive
 * Differential Evolution with Neighbourhood Search"
 */
public class SaNSDEParameterAdaptationStrategy implements ParameterAdaptationStrategy{
    private double scalingFactorProbability;
    private ProbabilityDistributionFunction random;
    private ProbabilityDistributionFunction cauchyVariableRandom;
    private double totalAcceptedWithProbability;
    private double totalRejectedWithProbability;
    private double totalRejectedWithCauchy;
    private double totalAcceptedWithCauchy;
    private boolean probabilityChosen;

    /*
     * Default constructor for SaNSDEParameterAdaptationStrategy
     */
    public SaNSDEParameterAdaptationStrategy() {
        GaussianDistribution gaussian = new GaussianDistribution();
        gaussian.setMean(ConstantControlParameter.of(0.5));
        gaussian.setDeviation(ConstantControlParameter.of(0.3));
        random = gaussian;

        cauchyVariableRandom = new CauchyDistribution();
        scalingFactorProbability = 0.5;
        totalAcceptedWithProbability = 0;
        totalRejectedWithProbability = 0;
        totalRejectedWithCauchy = 0;
        totalAcceptedWithCauchy = 0;
        probabilityChosen = false;
    }

    /*
     * Copy constructor for SaNSDEParameterAdaptationStrategy
     * @param copy The SaNSDEParameterAdaptationStrategy to be copied
     */
    public SaNSDEParameterAdaptationStrategy(SaNSDEParameterAdaptationStrategy copy) {
        scalingFactorProbability = copy.scalingFactorProbability;
        random = copy.random;
        cauchyVariableRandom = copy.cauchyVariableRandom;
        totalAcceptedWithProbability = copy.totalAcceptedWithProbability;
        totalRejectedWithProbability = copy.totalRejectedWithProbability;
        totalRejectedWithCauchy = copy.totalRejectedWithCauchy;
        totalAcceptedWithCauchy = copy.totalAcceptedWithCauchy;
        probabilityChosen = copy.probabilityChosen;
    }

    /*
     * Clone method for SaNSDEParameterAdaptationStrategy
     * @return A new instance of this SaNSDEParameterAdaptationStrategy
     */
    public ParameterAdaptationStrategy getClone() {
        return new SaNSDEParameterAdaptationStrategy(this);
    }

    /*
     * Changes the parameter using the same method as the NSDEParameterAdaptationStrategy
     * @param parameter The parameter to be changed
     */
    public void change(SettableControlParameter parameter) {
        if(Rand.nextDouble() < scalingFactorProbability) {
            parameter.update(random.getRandomNumber());
            probabilityChosen = true;
        } else {
            parameter.update(cauchyVariableRandom.getRandomNumber());
            probabilityChosen = false;
        }
    }

    /*
     * Adds the accepted/rejected value of the parameter to the appropriate counter.
     * @param parameter The parameter that was accepted/rejected
     * @param entity The entity that uses the above parameter
     * @param accepted Whether the parameter was accepted or rejected
     */
    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        if(accepted) {
            //learningExperience.add(acceptedParameter.getParameter());
            if(probabilityChosen) {
                totalAcceptedWithProbability += parameter.getParameter();
            } else {
                totalAcceptedWithCauchy += parameter.getParameter();
            }
        } else {
            if(probabilityChosen) {
                totalRejectedWithProbability += parameter.getParameter();
            } else {
                totalRejectedWithCauchy += parameter.getParameter();
            }
        }
    }

    /*
     * Recalculates the scalingFactorProbability using the values of
     * accepted and rejected parameters using each strategy.
     * @return The new scaling factor probability
     */
    public double recalculateAdaptiveVariables() {
        double nominator = totalAcceptedWithProbability * (totalAcceptedWithCauchy + totalRejectedWithCauchy);
        double denominator = totalAcceptedWithCauchy * (totalAcceptedWithProbability + totalRejectedWithProbability)
                + totalAcceptedWithProbability * (totalAcceptedWithCauchy + totalRejectedWithCauchy);

        if(denominator != 0.0) {
            scalingFactorProbability = nominator / denominator;
        }

        totalAcceptedWithProbability = 0;
        totalRejectedWithProbability = 0;
        totalRejectedWithCauchy = 0;
        totalAcceptedWithCauchy = 0;
        return scalingFactorProbability;
    }

    /*
     * Returns the current value of the scalingFactorProbability
     * @return The scalingFactorProbability
     */
    public double getScalingFactorProbability() {
        return scalingFactorProbability;
    }

    /*
     * Returns the random probability distribution function used when
     * changing the parameter
     * @return The probability distribution function
     */
    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    /*
     * Sets the random probability distribution function used when
     * changing the parameter
     * @param The new probability distribution function
     */
    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    /*
     * Returns the probability distribution function used to adapt the parameter
     * @return The probability distribution function
     */
    public ProbabilityDistributionFunction getCauchyVariableRandom() {
        return cauchyVariableRandom;
    }

    /*
     * Sets the probability distribution function used to adapt the parameter to the one
     * received as a parameter
     * @return The probability distribution function
     */
    public void setCauchyVariableRandom(ProbabilityDistributionFunction cauchyVariableRandom) {
        this.cauchyVariableRandom = cauchyVariableRandom;
    }

    /*
     * Returns the value of totalAcceptedWithProbability
     * @return The value of totalAcceptedWithProbability
     */
    public double getTotalAcceptedWithProbability() {
        return totalAcceptedWithProbability;
    }

    /*
     * Sets the value of totalAcceptedWithProbability to the value
     * received as a parameter
     * @param The new value of totalAcceptedWithProbability
     */
    public void setTotalAcceptedWithProbability(double totalAcceptedWithProbability) {
        this.totalAcceptedWithProbability = totalAcceptedWithProbability;
    }

    /*
     * Returns the value of totalRejectedWithProbability
     * @return The value of totalRejectedWithProbability
     */
    public double getTotalRejectedWithProbability() {
        return totalRejectedWithProbability;
    }

    /*
     * Sets the value of totalRejectedWithProbability to the value
     * received as a parameter
     * @param The new value of totalRejectedWithProbability
     */
    public void setTotalRejectedWithProbability(double totalRejectedWithProbability) {
        this.totalRejectedWithProbability = totalRejectedWithProbability;
    }

    /*
     * Returns the value of totalRejectedWithCauchy
     * @return The value of totalRejectedWithCauchy
     */
    public double getTotalRejectedWithCauchy() {
        return totalRejectedWithCauchy;
    }

    /*
     * Sets the value of totalRejectedWithCauchy to the value
     * received as a parameter
     * @param The new value of totalRejectedWithCauchy
     */
    public void setTotalRejectedWithCauchy(double totalRejectedWithCauchy) {
        this.totalRejectedWithCauchy = totalRejectedWithCauchy;
    }

    /*
     * Returns the value of getTotalAcceptedWithCauchy
     * @return The value of getTotalAcceptedWithCauchy
     */
    public double getTotalAcceptedWithCauchy() {
        return totalAcceptedWithCauchy;
    }

    /*
     * Sets the new value of getTotalAcceptedWithCauchy to the value
     * received as a parameter
     * @param The value of getTotalAcceptedWithCauchy
     */
    public void setTotalAcceptedWithCauchy(double totalAcceptedWithCauchy) {
        this.totalAcceptedWithCauchy = totalAcceptedWithCauchy;
    }
}
