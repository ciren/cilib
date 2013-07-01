/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.niching.iterationstrategies;

import java.util.Set;

import net.cilib.algorithm.population.AbstractIterationStrategy;
import net.cilib.algorithm.population.IterationStrategy;
import net.cilib.entity.Topologies;
import net.cilib.pso.PSO;
import net.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.cilib.pso.particle.Particle;
import fj.F;

/**
 * Reinitialises redundant particles but leaves the neighbourhood bests intact.
 */
public class EnhancedSpeciation extends AbstractIterationStrategy<PSO> {

    private IterationStrategy<PSO> delegate;

    public EnhancedSpeciation() {
        this.delegate = (IterationStrategy) new SynchronousIterationStrategy();
    }

    public EnhancedSpeciation(EnhancedSpeciation copy) {
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public EnhancedSpeciation getClone() {
        return new EnhancedSpeciation(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        delegate.performIteration(algorithm);

        final Set<Particle> nBests = (Set<Particle>) Topologies.getNeighbourhoodBestEntities(algorithm.getTopology(), algorithm.getNeighbourhood());
        algorithm.setTopology(algorithm.getTopology().map(new F<Particle, Particle>() {
            @Override
            public Particle f(Particle a) {
                if (a.getFitness().compareTo(a.getNeighbourhoodBest().getFitness()) == 0 && !nBests.contains(a)) {
                    a.reinitialise();
                }

                return a;
            }
        }));
    }
}
