/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 * Calculates the swarm diameter as the maximum euclidean distance between any
 * two particles.
 *
 *
 *
 * @author  Andries Engelbrecht
 */
public class Diameter implements Measurement<Real> {
    private static final long serialVersionUID = 5136996282460480831L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Diameter getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "R";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm popAlg = (PopulationBasedAlgorithm) algorithm;
        Topology<? extends Entity> topology = popAlg.getTopology();

        DiameterVisitor visitor = new DiameterVisitor();
        topology.accept(visitor);

        return new Real(visitor.getResult());
    }

}
