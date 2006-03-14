/*
 * BehaviouralParameters.java
 * 
 * Created on Nov 18, 2005
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
 *
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.type.types.Type;

/**
 * Interface to describe the additional Behavioural parameters that Entities may have.
 * 
 * @author Gary Pampara
 */
public interface BehaviouralParameters {
	
	/**
	 * Return the <tt>Entity</tt> associated behavioural parameters
	 * @return a <tt>Type</tt> representing the behavioural parameters
	 */
	public Type getBehaviouralParameters();
	
	/**
	 * Set the behavioural parameters for the <tt>Entity</tt>
	 * @param behaviouralParameters The behavioural parameters to set
	 */
	public void setBehaviouralParameters(Type behaviouralParameters);

}
