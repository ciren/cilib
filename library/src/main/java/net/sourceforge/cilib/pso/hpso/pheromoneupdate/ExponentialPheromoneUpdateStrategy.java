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
 * using exponential change:
 * <br>change = sign(diffInFitnessChange) * exp(abs(diffInFitnessChange)) - 1
 */
public class ExponentialPheromoneUpdateStrategy implements PheromoneUpdateStrategy, Cloneable {

    private ControlParameter scale;

    /**
     * Creates a new instance of ExponentialPheromoneUpdateStrategy
     */
    public ExponentialPheromoneUpdateStrategy() {
        this.scale = ConstantControlParameter.of(0.001);
    }

    /**
     * Creates an ExponentialPheromoneUpdateStrategy with the same attributes as another
     * ExponentialPheromoneUpdateStrategy
     *
     * @param o the other instance ExponentialPheromoneUpdateStrategy
     */
    public ExponentialPheromoneUpdateStrategy(ExponentialPheromoneUpdateStrategy o) {
    }

    /**
     * {@inheritDoc}
     */
    public ExponentialPheromoneUpdateStrategy getClone() {
        return new ExponentialPheromoneUpdateStrategy(this);
    }

    /**
     * Calculates the change in pheromone level for a particular particle's behavior.
     *
     * @param e The particle whose behavior is being used for the pheromone update
     * @return The change in pheromone level for a behavior
     */
    @Override
    public double updatePheromone(Particle e) {
        Fitness prevFitness = ((Fitness)e.getProperties().get(EntityType.PREVIOUS_FITNESS));
        double diff = e.getFitness().getValue() - (prevFitness.getValue().isNaN() ? 0 : prevFitness.getValue());
        double sign = e.getFitness().compareTo(prevFitness);
        return Math.exp(sign * Math.abs(diff) * scale.getParameter());
    }

    public void setScale(ControlParameter scale) {
        this.scale = scale;
    }

    public ControlParameter getScale() {
        return scale;
    }
}
