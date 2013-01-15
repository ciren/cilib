/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterationstrategies;

import fj.F;
import fj.data.List;
import java.util.Collection;
import java.util.Set;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;

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

        final Set<Particle> nBests = (Set<Particle>) Topologies.getNeighbourhoodBestEntities(algorithm.getTopology());
        Collection<Particle> newTopology =  List.iterableList(algorithm.getTopology()).map(new F<Particle, Particle>() {
            @Override
            public Particle f(Particle a) {
                if (a.getFitness().compareTo(a.getNeighbourhoodBest().getFitness()) == 0 && !nBests.contains(a)) {
                    a.reinitialise();
                }

                return a;
            }
        }).toCollection();
        algorithm.getTopology().clear();
        algorithm.getTopology().addAll(newTopology);
    }
}
