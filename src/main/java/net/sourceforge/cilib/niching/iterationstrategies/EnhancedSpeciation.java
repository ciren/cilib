/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.niching.iterationstrategies;

import fj.F;
import fj.data.List;
import java.util.Collection;
import java.util.Set;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;

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
