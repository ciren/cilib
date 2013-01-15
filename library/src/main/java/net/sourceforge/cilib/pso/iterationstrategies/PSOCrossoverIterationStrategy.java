/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.operations.BoltzmannCrossoverSelection;
import net.sourceforge.cilib.pso.crossover.operations.PSOCrossoverOperation;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * An iteration strategy that uses different PSOCrossoverOperations to affect the
 * swarm of particles.
 */
public class PSOCrossoverIterationStrategy extends AbstractIterationStrategy<PSO> {

    private PSOCrossoverOperation crossoverOperation;

    /**
     * Default constructor
     */
    public PSOCrossoverIterationStrategy() {
        this.crossoverOperation = new BoltzmannCrossoverSelection();
    }

    /**
     * Copy constructor
     *
     * @param copy
     */
    public PSOCrossoverIterationStrategy(PSOCrossoverIterationStrategy copy) {
        this.crossoverOperation = copy.crossoverOperation.getClone();
    }

    /**
     * Clones this instance
     *
     * @return the clone
     */
    @Override
    public PSOCrossoverIterationStrategy getClone() {
        return new PSOCrossoverIterationStrategy(this);
    }

    /**
     *
     *
     * @param algorithm
     */
    @Override
    public void performIteration(PSO algorithm) {
        Topology<Particle> topology = algorithm.getTopology();

        for (Particle current : topology) {
            current.updateVelocity();
            current.updatePosition();

            boundaryConstraint.enforce(current);
            current.calculateFitness();
        }

        algorithm.setTopology(crossoverOperation.f(algorithm));
        topology = algorithm.getTopology();

        for (Particle current : topology) {
            for (Particle other : topology.neighbourhood(current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }
    }

    public void setCrossoverOperation(PSOCrossoverOperation crossoverOperation) {
        this.crossoverOperation = crossoverOperation;
    }
}
