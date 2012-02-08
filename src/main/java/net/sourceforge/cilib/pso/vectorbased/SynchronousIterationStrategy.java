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
package net.sourceforge.cilib.pso.vectorbased;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.particle.VBParticle;

/**
 * Implementation of the synchronous iteration strategy for VBPSO.
 *
 */
public class SynchronousIterationStrategy extends AbstractIterationStrategy<VBPSO> {


    /**
     * {@inheritDoc}
     */
    @Override
    public SynchronousIterationStrategy getClone() {
        return this;
    }


    /**
     * <p>This is an ASynchronous strategy:</p>
     * <ol>
     * <li>For all particles:</li>
     * <ol><li>Update the particle velocity</li>
     *     <li>Update the particle position</li></ol>
     * <li>For all particles:</li>
     * <ol><li>Calculate the particle fitness</li>
     *     <li>For all particles in the current particle's neighbourhood:</li>
     *     <ol><li>Update the nieghbourhood best</li></ol></ol>
     * </ol>
     *
     * @see net.sourceforge.cilib.PSO.IterationStrategy#performIteration(net.sourceforge.cilib.PSO.PSO)
     * @param pso The {@link VBPSO} to have an iteration applied.
     */
    @Override
    public void performIteration(VBPSO pso) {
        Topology<VBParticle> topology = pso.getTopology();
        topology.update();

        for (VBParticle current : topology) {
            current.updateVelocity();
            current.updatePosition();

            boundaryConstraint.enforce(current);
        }

        for (Iterator<VBParticle> i = topology.iterator(); i.hasNext();) {
            Particle current = i.next();
            current.calculateFitness();

            for (Iterator<VBParticle> j = topology.neighbourhood(i); j.hasNext();) {
                VBParticle other = j.next();
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }
    }

}
