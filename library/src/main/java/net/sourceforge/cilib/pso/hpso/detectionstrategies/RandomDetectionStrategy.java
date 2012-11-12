/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.detectionstrategies;


import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

public class RandomDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {
    private ControlParameter probability;
    private RandomProvider random;

    public RandomDetectionStrategy() {
        probability = ConstantControlParameter.of(75.0);
        random = new MersenneTwister();
    }

    /**
     * Construct a copy of the given {@link RandomDetectionStrategy}.
     *
     * @param copy the {@link RandomDetectionStrategy} to copy.
     */
    public RandomDetectionStrategy(RandomDetectionStrategy copy) {
        this.probability = copy.probability.getClone();
        this.random = copy.random;
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
        double rNum = random.nextDouble();

        if (rNum < probability.getParameter()) {
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
