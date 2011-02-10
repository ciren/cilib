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
package net.sourceforge.cilib.pso.velocityprovider;


import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 *
 * @author Gary Pampara
 */
public interface VelocityProvider extends Cloneable {

    /**
     * Clone the <tt>VelocityProvider</tt> object.
     * @return A cloned <tt>VelocityProvider</tt>
     */
    @Override
    VelocityProvider getClone();

    /**
     * Perform the velocity update operation on the specified <tt>Particle</tt>.
     * @param particle The <tt>Particle</tt> to apply the operation on.
     */
    Vector get(Particle particle);

    /**
     * Update the needed control parameters for the <tt>VelocityProvider</tt>,
     * if needed.
     * @param particle The particle for whom the VelocityProvider parameters need to be updated.
     */
    void updateControlParameters(Particle particle);
}
