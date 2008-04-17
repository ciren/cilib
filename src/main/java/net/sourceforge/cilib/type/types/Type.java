/*
 * Type.java
 *
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.type.types;

import java.io.Serializable;

import net.sourceforge.cilib.util.Cloneable;

/**
 * {@code Type} interface for all type-objects that are used within CIlib.
 */
public interface Type extends Serializable, Cloneable {
	
	/**
	 * {@inheritDoc}
	 */
	public Type getClone();
	
	public boolean equals(Object other);

	public int hashCode();
	
	public String getRepresentation();

	public boolean isInsideBounds();

	public int getDimension();

	public void randomise();

	public void reset();

}
