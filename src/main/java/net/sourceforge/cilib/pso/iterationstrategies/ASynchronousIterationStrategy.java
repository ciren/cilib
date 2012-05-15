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
package net.sourceforge.cilib.pso.iterationstrategies;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;

/**
 * Implementation of the asynchrounous iteration strategy for PSO.
 *
 */
public class ASynchronousIterationStrategy extends AbstractIterationStrategy<PSO> {

    private static final long serialVersionUID = -3511991873784185698L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ASynchronousIterationStrategy getClone() {
        return this;
    }

    /**
     * <p>This is an ASynchronous strategy:</p>
     * <ol>
     * <li>For all particles:</li>
     * <ol>
     * <li>Update the particle velocity</li>
     * <li>Update the particle position</li>
     * <li>Calculate the particle fitness</li>
     * <li>For all paritcles in the current particle's neighbourhood</li>
     * <ol><li>Update the nieghbourhooh best</li></ol>
     * </ol>
     * </ol>
     *
     * @see net.sourceforge.cilib.PSO.IterationStrategy#performIteration()
     * @param algorithm The algorithm to which an iteration is to be applied.
     */
    public void performIteration(PSO algorithm) {
        Topology<Particle> topology = algorithm.getTopology();

        for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext();) {
            Particle current = i.next();
            current.updateVelocity();       // TODO: replace with visitor (will simplify particle interface)
            current.updatePosition();       // TODO: replace with visitor (will simplify particle interface)

            Entity enforcedEntity = boundaryConstraint.enforce(current);
            current = (Particle) enforcedEntity.getClone();

            current.calculateFitness();

            for (Iterator<? extends Particle> j = topology.neighbourhood(i); j.hasNext();) {
                Particle other = j.next();
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current); // TODO: neighbourhood visitor?
                }
            }
        }
    }
}
