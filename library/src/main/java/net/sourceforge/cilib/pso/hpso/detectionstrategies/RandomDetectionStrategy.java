/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.detectionstrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;

public class RandomDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {
    private ControlParameter probability;

    public RandomDetectionStrategy() {
        probability = ConstantControlParameter.of(75.0);
    }

    /**
     * Construct a copy of the given {@link RandomDetectionStrategy}.
     *
     * @param copy the {@link RandomDetectionStrategy} to copy.
     */
    public RandomDetectionStrategy(RandomDetectionStrategy copy) {
        this.probability = copy.probability.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomDetectionStrategy getClone() {
        return new RandomDetectionStrategy(this);
    }

    @Override
    public boolean detect(Particle entity) {
        if (Rand.nextDouble() < probability.getParameter()) {
            return true;
        }

        return false;
    }

    public ControlParameter getProbability() {
        return probability;
    }

    public void setProbability(ControlParameter probability) {
        this.probability = probability;
    }
}
