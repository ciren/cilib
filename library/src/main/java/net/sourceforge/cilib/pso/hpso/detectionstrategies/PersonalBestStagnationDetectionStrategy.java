/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.detectionstrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Int;

/**
 * This {@link BehaviorChangeTriggerDetectionStrategy} monitors a
 * {@link Particle}'s personal best fitness. If it has not change over a
 * recent window of iterations, the particle is assumed to have stagnated
 * and should therefore change its behavior.
 */
public class PersonalBestStagnationDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {
    private ControlParameter windowSize;

    /**
     * Construct a new {@link PersonalBestStagnationDetectionStrategy} with a
     * default window size of 10.
     */
    public PersonalBestStagnationDetectionStrategy() {
        windowSize = ConstantControlParameter.of(10);
    }

    /**
     * Construct a copy of the given {@link PersonalBestStagnationDetectionStrategy}.
     *
     * @param copy the {@link PersonalBestStagnationDetectionStrategy} to copy.
     */
    public PersonalBestStagnationDetectionStrategy(PersonalBestStagnationDetectionStrategy copy) {
        this.windowSize = copy.windowSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestStagnationDetectionStrategy getClone() {
        return new PersonalBestStagnationDetectionStrategy(this);
    }

    /**
     * Checks that a {@link Particle}'s personal best fitness has changed
     * during a recent window of iterations (number of iterations set by
     * <code>windowSize</code>). If it has not, the particle is assumed to
     * be stagnating.
     *
     * @param entity The {@link Particle} to check for stagnation.
     * @return True if the {@link Particle} is stagnating. False otherwise.
     */
    @Override
    public boolean detect(Particle entity) {
        int counter = ((Int)entity.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();

        if (counter > windowSize.getParameter()) {
            entity.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            return true;
        }

        return false;
    }

    public void setWindowSize(ControlParameter windowSize) {
        this.windowSize = windowSize;
    }
}
