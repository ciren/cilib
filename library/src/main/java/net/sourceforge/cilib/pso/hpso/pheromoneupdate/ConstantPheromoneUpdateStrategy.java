/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.pheromoneupdate;

import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Calculates the change in pheromone level for a particular particle's behavior
 * using three constants for whether the particle did better, the same or worse
 * than the previous iteration.
 */
public class ConstantPheromoneUpdateStrategy implements PheromoneUpdateStrategy, Cloneable {
    private ControlParameter better;
    private ControlParameter same;
    private ControlParameter worse;

    /**
     * Initialises the {@link ControlParameter}s to 1.0, 0.5 and 0.0 for whether
     * the particle did better, the same or worse respectively.
     */
    public ConstantPheromoneUpdateStrategy() {
        this.better = ConstantControlParameter.of(1.0);
        this.same = ConstantControlParameter.of(0.5);
        this.worse = ConstantControlParameter.of(0.0);
    }

    /**
     * Creates a {@linkplain ConstantPheromoneUpdateStrategy} with the same
     * attributes as another {@linkplain ConstantPheromoneUpdateStrategy}.
     *
     * @param o the other instance {@linkplain ConstantPheromoneUpdateStrategy}
     */
    public ConstantPheromoneUpdateStrategy(ConstantPheromoneUpdateStrategy o) {
        this.better = o.better;
        this.same = o.same;
        this.worse = o.worse;
    }

    /**
     * {@inheritDoc}
     */
    public ConstantPheromoneUpdateStrategy getClone() {
        return new ConstantPheromoneUpdateStrategy(this);
    }

    /**
     * Calculates the change in pheromone level for a particular
     * {@link Particle}'s behavior.
     *
     * @param e The {@linkplain Particle} whose behavior is being used for the
     * pheromone update
     * @return The change in pheromone level for a behavior
     */
    @Override
    public double updatePheromone(Particle e) {
        int compResult = ((Fitness)e.getProperties().get(EntityType.PREVIOUS_FITNESS)).compareTo(e.getFitness());
        double result = compResult < 0 ? this.better.getParameter() :
            (compResult == 0 ? this.same.getParameter() : this.worse.getParameter());
        return result;
    }

    public void setWorse(ControlParameter worse) {
        this.worse = worse;
    }

    public void setSame(ControlParameter same) {
        this.same = same;
    }

    public void setBetter(ControlParameter better) {
        this.better = better;
    }

    public ControlParameter getWorse() {
        return worse;
    }

    public ControlParameter getSame() {
        return same;
    }

    public ControlParameter getBetter() {
        return better;
    }
}
