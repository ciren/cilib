/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.creation;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * This Creation Strategy selects between two selection strategies according to
 * some probability.
 * <p>
 * As time passes, it adapts the probability in order to favour the strategy
 * that results in more individuals surviving to the next generation. The
 * probabilities change every X iterations where X is the learning period.
 * <p>
 * This adaptive strategy can be found in the following article:
 * <p>
 * @inproceedings{1554904,
 * author={Qin, A.K. and Suganthan, P.N.},
 * booktitle={Evolutionary Computation, 2005. The 2005 IEEE Congress on}, title={Self-adaptive differential evolution algorithm for numerical optimization},
 * year={2005},
 * month={sept.},
 * volume={2},
 * number={},
 * pages={ 1785 - 1791 Vol. 2}}
 */
public class SaDECreationStrategy implements CreationStrategy {
    private CreationStrategy strategy1;
    private CreationStrategy strategy2;
    private double randomValue;
    private double probability;
    private double totalAcceptedWithStrategy1;
    private double totalAcceptedWithStrategy2;
    private double totalRejectedWithStrategy1;
    private double totalRejectedWithStrategy2;
    private ProbabilityDistributionFunction random;
    private int learningPeriod;
    private double iterationToChange;
    private boolean probabilitiesChanged;
    private SettableControlParameter scaleParameter;

    /*
     * Default constructor for SaDECreationStrategy
     */
    public SaDECreationStrategy() {
        strategy1 = new RandCreationStrategy();
        strategy2 = new RandToBestCreationStrategy();
        probability = 0.5;
        totalAcceptedWithStrategy1 = 0;
        totalAcceptedWithStrategy2 = 0;
        totalRejectedWithStrategy1 = 0;
        totalRejectedWithStrategy2 = 0;
        random = new UniformDistribution();
        randomValue = 0;
        learningPeriod = 50;
        iterationToChange = learningPeriod;
        probabilitiesChanged = false;
        scaleParameter = ConstantControlParameter.of(0.5);
    }

    /*
     * Copy constructor for SaDECreationStrategy
     * @param copy The SaDECreationStrategy to be copied
     */
    public SaDECreationStrategy(SaDECreationStrategy copy) {
        strategy1 = copy.strategy1;
        strategy2 = copy.strategy2;
        probability = copy.probability;
        totalAcceptedWithStrategy1 = copy.totalAcceptedWithStrategy1;
        totalAcceptedWithStrategy2 = copy.totalAcceptedWithStrategy2;
        totalRejectedWithStrategy1 = copy.totalRejectedWithStrategy1;
        totalRejectedWithStrategy2 = copy.totalRejectedWithStrategy2;
        random = copy.random;
        randomValue = copy.randomValue;
        learningPeriod = copy.learningPeriod;
        iterationToChange = copy.iterationToChange;
        probabilitiesChanged = copy.probabilitiesChanged;
        scaleParameter = copy.scaleParameter.getClone();
    }

    /*
     * Clone method for SaDECreationStrategy
     * @return a new instance of the current SaDECreationStrategy
     */
    public CreationStrategy getClone() {
        return new SaDECreationStrategy(this);
    }

    /*
     * Creates a new trial vector. It chooses between the strategies to be used for
     * the trial vector creation and checks if it is time to change the probability
     * values.
     *
     * @param targetEntity The target entity used for creating the trial vector
     * @param current The current individual being dealt with
     * @param topology The topology from which individuals are selected in order to create the difference vector
     * @return trialEntity The trial vector
     */
    public <T extends Entity> T create(T targetEntity, T current, Topology<T> topology) {
        randomValue = random.getRandomNumber(0,1);

        if((iterationToChange == AbstractAlgorithm.get().getIterations()) && !probabilitiesChanged) {
            updateProbabilities();
            iterationToChange += learningPeriod;
        } else if((iterationToChange != AbstractAlgorithm.get().getIterations()) && probabilitiesChanged) {
            probabilitiesChanged = false;
        }

        T trialEntity;

        if(randomValue <= probability) {
           trialEntity = (T) strategy1.create(targetEntity, current, topology).getClone();
        } else {
            trialEntity = strategy2.create(targetEntity, current, topology);
        }

        return trialEntity;
    }

    /*
     * Updates the value of the probability to favour a more successful strategy.
     */
    protected void updateProbabilities() {
        double nominator = totalAcceptedWithStrategy1 * (totalAcceptedWithStrategy2 + totalRejectedWithStrategy2);
        double denominator = totalAcceptedWithStrategy2 * (totalAcceptedWithStrategy1 + totalRejectedWithStrategy1)
                + totalAcceptedWithStrategy1 * (totalAcceptedWithStrategy2 + totalRejectedWithStrategy2);

        probability = nominator / denominator;

        totalAcceptedWithStrategy1 = 0;
        totalAcceptedWithStrategy2 = 0;
        totalRejectedWithStrategy1 = 0;
        totalRejectedWithStrategy2 = 0;
        probabilitiesChanged = true;
    }

    /*
     * Adds 1 to the count of accepted offspring generated using a strategy if the input is true.
     * Adds 1 to the count of rejected offspring generated using a strategy if the input is false.
     * @param accepted A boolean stating whether the latest offspring was accepted or rejected.
     */

    public String accepted(boolean accepted) {
        if(accepted) {
            if(randomValue < probability) {
                totalAcceptedWithStrategy1++; //these are updated so that the SaDECreationStrategy can be used separately from the SaDEIterationStrategy
                return "Strategy 1 Accepted";
            } else {
                totalAcceptedWithStrategy2++; //these are updated so that the SaDECreationStrategy can be used separately from the SaDEIterationStrategy
                return "Strategy 2 Accepted";
            }
        } else {
            if(randomValue < probability) {
                totalRejectedWithStrategy1++; //these are updated so that the SaDECreationStrategy can be used separately from the SaDEIterationStrategy
                return "Strategy 1 Rejected";
            } else {
                totalRejectedWithStrategy2++; //these are updated so that the SaDECreationStrategy can be used separately from the SaDEIterationStrategy
                return "Strategy 2 Rejected";
            }
        }
    }

    /*
     * Sets the first creation strategy to the one received as a parameter
     * @param strategy The new strategy
     */
    public void setStrategy1(CreationStrategy strategy) {
        strategy1 = strategy;
    }

    /*
     * Returns the first creation strategy.
     * @return strategy1 The first creation strategy
     */
    public CreationStrategy getStrategy1() {
        return strategy1;
    }

    /*
     * Sets the second creation strategy to the one received as a parameter
     * @param strategy The new strategy
     */
    public void setStrategy2(CreationStrategy strategy) {
        strategy2 = strategy;
    }

    /*
     * Returns the second creation strategy.
     * @return strategy2 The first creation strategy
     */
    public CreationStrategy getStrategy2() {
        return strategy2;
    }

    /*
     * Sets the probability of the first strategy being chosen to the one received as a parameter
     * @param probability The new probability
     */
    public void setProbability1(double probability) {
        this.probability = probability;
    }

    /*
     * Returns the probability of the first strategy being chosen.
     * @return probability The probability of the first strategy being chosen.
     */
    public double getProbability1() {
        return probability;
    }

    /*
     * Sets the random generator to the one received as a parameter
     * @param random The new random generator
     */
    public void setRandom(ProbabilityDistributionFunction randomDistricution) {
        random = randomDistricution;
    }

    /*
     * Returns the random generator
     * @return random The random generator
     */
    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    /*
     * Sets the learning period (every how many iterations must the probabilities change)
     * @param learningPeriod The new learning period
     */
    public void setLearningPeriod(int period) {
        learningPeriod = period;
    }

    /*
     * Returns the learning period (every how many iterations must the probabilities change)
     * @return learningPeriod The learning period
     */
    public int getLearningPeriod() {
        return learningPeriod;
    }

    public void setScaleControlParameter(SettableControlParameter scaleParameter) {
        this.scaleParameter = scaleParameter;
        strategy1.setScaleParameter(scaleParameter.getParameter());
        strategy2.setScaleParameter(scaleParameter.getParameter());
    }

    public void setScaleParameter(double scaleParameter) {
        this.scaleParameter.setParameter(scaleParameter);
        strategy1.setScaleParameter(scaleParameter);
        strategy2.setScaleParameter(scaleParameter);
    }

    public SettableControlParameter getScaleParameter() {
        return scaleParameter;
    }

    public double getTotalAcceptedWithStrategy1() {
        return totalAcceptedWithStrategy1;
    }

    public void setTotalAcceptedWithStrategy1(double totalAcceptedWithStrategy1) {
        this.totalAcceptedWithStrategy1 = totalAcceptedWithStrategy1;
    }

    public double getTotalAcceptedWithStrategy2() {
        return totalAcceptedWithStrategy2;
    }

    public void setTotalAcceptedWithStrategy2(double totalAcceptedWithStrategy2) {
        this.totalAcceptedWithStrategy2 = totalAcceptedWithStrategy2;
    }

    public double getTotalRejectedWithStrategy1() {
        return totalRejectedWithStrategy1;
    }

    public void setTotalRejectedWithStrategy1(double totalRejectedWithStrategy1) {
        this.totalRejectedWithStrategy1 = totalRejectedWithStrategy1;
    }

    public double getTotalRejectedWithStrategy2() {
        return totalRejectedWithStrategy2;
    }

    public void setTotalRejectedWithStrategy2(double totalRejectedWithStrategy2) {
        this.totalRejectedWithStrategy2 = totalRejectedWithStrategy2;
    }

    public double getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(double randomValue) {
        this.randomValue = randomValue;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getIterationToChange() {
        return iterationToChange;
    }

    public void setIterationToChange(double iterationToChange) {
        this.iterationToChange = iterationToChange;
    }

    public boolean probabilitiesChanged() {
        return probabilitiesChanged;
    }

    public void setProbabilitiesChanged(boolean probabilitiesChanged) {
        this.probabilitiesChanged = probabilitiesChanged;
    }

}
