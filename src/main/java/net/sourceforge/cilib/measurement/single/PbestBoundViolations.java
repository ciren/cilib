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
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 * @author  Andries Engelbrecht
 */
public class PbestBoundViolations implements Measurement<Real> {
    private static final long serialVersionUID = 7547646366505677446L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PbestBoundViolations getClone() {
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
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;

        int numberOfViolations = 0;
        int populationSize = populationBasedAlgorithm.getTopology().size();

        for (Entity populationEntity : populationBasedAlgorithm.getTopology()) {
            Vector pbest = (Vector) populationEntity.getProperties().get(EntityType.Particle.BEST_POSITION);
        if (pbest == null)
               throw new UnsupportedOperationException("Entity is not a particle.");

            for (Numeric position : pbest) {
                Bounds bounds = position.getBounds();

                if (!bounds.isInsideBounds(position.getReal())) {
                    numberOfViolations++;
                    break;
                }
            }
        }

        return new Real((double)numberOfViolations/(double)populationSize);
    }

}
