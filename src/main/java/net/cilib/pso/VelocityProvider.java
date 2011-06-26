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
package net.cilib.pso;

import fj.F2;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.Velocity;
import net.cilib.entity.Particle;

/**
 * Provider interface defining how to create {@link Velocity}
 * instances.
 *
 * @author gpampara
 */
public abstract class VelocityProvider extends F2<Particle, Topology, Velocity> {

    /**
     * Create a new {@code Velocity} instance based on the given
     * {@code Particle}.
     *
     * @param particle to base the calculation of the {@link Velocity} on.
     * @return an newly created immutable {@link Velocity}.
     */
//    Velocity create(Particle particle, HasCandidateSolution local, HasCandidateSolution global);
}
