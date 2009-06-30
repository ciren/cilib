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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.io.Serializable;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.util.Cloneable;

/**
 *
 * @author Gary Pampara
 */
public interface VelocityUpdateStrategy extends Serializable, Cloneable {

    /**
     * Clone the <tt>VelocityUpdateStrategy</tt> object.
     * @return A cloned <tt>VelocityUpdateStrategy</tt>
     */
    public VelocityUpdateStrategy getClone();

    /**
     * Perform the velocity update operation on the specified <tt>Particle</tt>.
     * @param particle The <tt>Particle</tt> to apply the operation on.
     */
    public void updateVelocity(Particle particle);

    /**
     * Update the needed control parameters for the <tt>VelocityUpdate</tt>,
     * if needed.
     * @param particle The particle for whom the VelocityUpdateStrategy parameters need to be updated.
     */
    public void updateControlParameters(Particle particle);

}
