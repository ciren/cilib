/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.detectionstrategies;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;

public class PeriodicDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {
    private ControlParameter period;

    public PeriodicDetectionStrategy() {
        period = ConstantControlParameter.of(10.0);
    }

    /**
     * Construct a copy of the given {@link RandomDetectionStrategy}.
     *
     * @param copy the {@link RandomDetectionStrategy} to copy.
     */
    public PeriodicDetectionStrategy(PeriodicDetectionStrategy copy) {
        this.period = copy.period.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodicDetectionStrategy getClone() {
        return new PeriodicDetectionStrategy(this);
    }

    @Override
    public boolean detect(Particle entity) {
        int iters = AbstractAlgorithm.get().getIterations();

        if (iters % period.getParameter() == 0) {
            return true;
        }

        return false;
    }

    public ControlParameter getPeriod() {
        return period;
    }

    public void setPeriod(ControlParameter period) {
        this.period = period;
    }
}
