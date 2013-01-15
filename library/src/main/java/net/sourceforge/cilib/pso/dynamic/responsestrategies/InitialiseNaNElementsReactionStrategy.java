/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This reaction strategy initialises new dimensions introduced into the
 * particles. These new dimensions are indicated by initially having the value
 * Double.NaN.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class InitialiseNaNElementsReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {

    public InitialiseNaNElementsReactionStrategy() {
    }

    public InitialiseNaNElementsReactionStrategy(InitialiseNaNElementsReactionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public InitialiseNaNElementsReactionStrategy<E> getClone() {
        return new InitialiseNaNElementsReactionStrategy<E>(this);
    }

    /**
     * Initialise the dimensions that have the value of Double.NaN. This is done
     * for all the particles in the topology.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        Topology<? extends Entity> entities = algorithm.getTopology();

        for (Entity entity : entities) {
            DynamicParticle particle = (DynamicParticle) entity;
            //initialise position
            Vector position = (Vector) particle.getPosition();
            for (int curElement = 0; curElement < position.size(); ++curElement) {
                if (Double.isNaN(position.doubleValueOf(curElement))) {
                    position.get(curElement).randomise();
                }
            }

            //initialise personal best
            Vector personalBest = (Vector) particle.getBestPosition();
            for (int curElement = 0; curElement < position.size(); ++curElement) {
                if (Double.isNaN(personalBest.doubleValueOf(curElement))) {
                    personalBest.setReal(curElement, position.doubleValueOf(curElement));
                }
            }

            //initialise velocity
            Vector velocity = particle.getVelocity();
            for (int curElement = 0; curElement < position.size(); ++curElement) {
                if (Double.isNaN(velocity.doubleValueOf(curElement))) {
                    velocity.setReal(curElement, 0.0);
                }
            }
        }
    }
}
