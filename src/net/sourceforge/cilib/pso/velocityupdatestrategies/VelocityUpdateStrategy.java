/*
 * VelocityUpdateStrategy.java
 *
 * Created on September 4, 2003, 1:27 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
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

import net.sourceforge.cilib.pso.particle.Particle;

/**
 *
 * @author Gary Pampara
 */
public interface VelocityUpdateStrategy extends Serializable {
	
	/**
	 * Clone the <tt>VelocityUpdateStrategy</tt> object.
	 * @return A cloned <tt>VelocityUpdateStrategy</tt>
	 */
	public VelocityUpdateStrategy clone();
	
	/**
	 * Perform the velocity update operation on the specified <tt>Particle</tt>.
	 * @param particle The <tt>Particle</tt> to apply the operation on.
	 */
    public void updateVelocity(Particle particle);
    
    
    /**
     * Update the needed control parameters for the <tt>VelocityUpdate</tt>, 
     * if needed.
     */
    public void updateControlParameters();
}
