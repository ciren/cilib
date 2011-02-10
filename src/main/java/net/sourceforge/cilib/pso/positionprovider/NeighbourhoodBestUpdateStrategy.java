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

import java.io.Serializable;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface specifying the manner in which the neighbourhood best of the
 * {@linkplain Entity} is obtained.
 *
 * @author Gary Pampara
 */
public interface NeighbourhoodBestUpdateStrategy extends Serializable, Cloneable {

    /**
     * {@inheritDoc}
     */
    NeighbourhoodBestUpdateStrategy getClone();

    /**
     * Get the social best fitness (neighbourhood best) of the given {@linkplain Entity}.
     * @param entity The Entity to determine the social best fitness from.
     * @return The social best (neighbourhood best) {@linkplain Fitness}.
     */
    Fitness getSocialBestFitness(Entity entity);
}
