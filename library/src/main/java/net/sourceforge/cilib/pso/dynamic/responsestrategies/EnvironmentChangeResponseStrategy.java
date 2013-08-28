/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.topologies.Neighbourhood;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.Cloneable;

/**
 * TODO: Complete this Javadoc.
 */
public abstract class EnvironmentChangeResponseStrategy implements Cloneable {
    protected boolean hasMemory = true;

    public EnvironmentChangeResponseStrategy() {
        this.hasMemory = true;
    }

    public EnvironmentChangeResponseStrategy(EnvironmentChangeResponseStrategy rhs) {
        this.hasMemory = rhs.hasMemory;
    }

    /**
     * Clone the <tt>EnvironmentChangeResponseStrategy</tt> object.
     *
     * @return A cloned <tt>EnvironmentChangeResponseStrategy</tt>
     */
    public abstract EnvironmentChangeResponseStrategy getClone();

    /**
     * Respond to the environment change and ensure that the neighbourhood best entities are
     * updated. This method (Template Method) calls two other methods in turn:
     * <ul>
     * <li>{@link #performReaction(PopulationBasedAlgorithm)}</li>
     * <li>{@link #updateNeighbourhoodBestEntities(Topology)}</li>
     * </ul>
     *
     * @param algorithm some {@link PopulationBasedAlgorithm population based algorithm}
     */
    public <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void respond(A algorithm) {
        performReaction(algorithm);
        if(hasMemory) {
            updateNeighbourhoodBestEntities(algorithm.getTopology(), algorithm.getNeighbourhood());
        }
    }

    /**
     * This is the method responsible for responding that should be overridden by sub-classes.
     * @param algorithm
     */
    protected abstract <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(A algorithm);

    /**
     * TODO: The problem with this is that it is PSO specific. It uses {@link Particle particles}
     * instead of {@link Entity entities}, because the {@link Entity} class does not have the
     * notion of a neighbourhood best.
     *
     * @param topology a topology of {@link Particle particles} :-(
     */
    protected <P extends Particle> void updateNeighbourhoodBestEntities(fj.data.List<P> topology, Neighbourhood<P> neighbourhood) {
        for (P current : topology) {
            current.updateFitness(current.getBehaviour().getFitnessCalculator().getFitness(current));
            for (P other : neighbourhood.f(topology, current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }
    }

    public boolean getHasMemory() {
        return hasMemory;
    }

    public void setHasMemory(boolean hasMemory) {
        this.hasMemory = hasMemory;
    }
}
