/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.tuning;

import net.cilib.algorithm.population.AbstractIterationStrategy;
import net.cilib.tuning.parameterchange.reactions.MinMaxParameterChangeReaction;
import net.cilib.tuning.parameterchange.reactions.ParameterChangeReaction;
import net.cilib.tuning.parameterchange.triggers.ParameterChangeTrigger;
import net.cilib.tuning.parameterchange.triggers.PeriodicParameterChangeTrigger;

public class IFRaceIterationStrategy extends AbstractIterationStrategy<TuningAlgorithm> {

    private FRaceIterationStrategy iterationStrategy;
    private ParameterChangeTrigger parameterChangeTrigger;
    private ParameterChangeReaction parameterChangeReaction;

    public IFRaceIterationStrategy() {
        this.iterationStrategy = new FRaceIterationStrategy();
        this.parameterChangeTrigger = new PeriodicParameterChangeTrigger();
        this.parameterChangeReaction = new MinMaxParameterChangeReaction();
    }

    @Override
    public AbstractIterationStrategy<TuningAlgorithm> getClone() {
        return this;
    }

    @Override
    public void performIteration(final TuningAlgorithm algorithm) {
        if (parameterChangeTrigger.f(algorithm)) {
            algorithm.setParameterList(parameterChangeReaction.f(algorithm));
            iterationStrategy.resetResults();
        }

        iterationStrategy.performIteration(algorithm);
    }

    public void setIterationStrategy(FRaceIterationStrategy iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public FRaceIterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    public void setParameterChangeTrigger(ParameterChangeTrigger parameterChangeTrigger) {
        this.parameterChangeTrigger = parameterChangeTrigger;
    }

    public ParameterChangeTrigger getParameterChangeTrigger() {
        return parameterChangeTrigger;
    }

    public void setParameterChangeReaction(ParameterChangeReaction parameterChangeReaction) {
        this.parameterChangeReaction = parameterChangeReaction;
    }

    public ParameterChangeReaction getParameterChangeReaction() {
        return parameterChangeReaction;
    }
}
