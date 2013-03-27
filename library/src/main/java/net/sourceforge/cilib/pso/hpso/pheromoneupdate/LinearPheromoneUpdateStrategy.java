/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.pheromoneupdate;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Calculates the change in pheromone level for a particular {@link Particle}'s
 * behaviorin direct proportion to the change in the particle's fitness from one
 * iteration to the next.
 */
public class LinearPheromoneUpdateStrategy implements PheromoneUpdateStrategy{
    private ControlParameter gradient;

    /**
     * Creates a new instance of LinearPheromoneUpdateStrategy
     */
    public LinearPheromoneUpdateStrategy() {
        this.gradient = ConstantControlParameter.of(0.1);
    }

    /**
     * Creates a LinearPheromoneUpdateStrategy with the same attributes as another
     * LinearPheromoneUpdateStrategy
     *
     * @param o the other instance LinearPheromoneUpdateStrategy
     */
    public LinearPheromoneUpdateStrategy(LinearPheromoneUpdateStrategy o) {
        this.gradient = o.gradient;
    }

    /**
     * {@inheritDoc}
     */
    public LinearPheromoneUpdateStrategy getClone() {
        return new LinearPheromoneUpdateStrategy(this);
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
        return Math.abs(diff) * this.gradient.getParameter() * (e.getFitness().compareTo(prevFitness));
    }

    public void setGradient(ControlParameter gradient) {
        this.gradient = gradient;
    }

    public ControlParameter getGradient() {
        return gradient;
    }
}
