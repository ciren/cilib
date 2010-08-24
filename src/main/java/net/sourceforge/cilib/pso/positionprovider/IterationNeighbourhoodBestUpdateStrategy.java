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
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;

/**
 * @author Gary Pampara
 */
public class IterationNeighbourhoodBestUpdateStrategy implements NeighbourhoodBestUpdateStrategy {
    private static final long serialVersionUID = 9029103734770326975L;

    /**
     * {@inheritDoc}
     */
    public IterationNeighbourhoodBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Fitness getSocialBestFitness(Entity entity) {
        return entity.getFitness();
    }

}
