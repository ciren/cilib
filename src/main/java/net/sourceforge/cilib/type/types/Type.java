/*
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
	
	/**
	 * {@inheritDoc}
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object other);

	/**
	 * {@inheritDoc}
	 * @see Object#hashCode()
	 */
	public int hashCode();
	
	/**
	 * Get the representation of this {@code Type} with domain information attached.
	 * @return The {@code String} representing the {@code Type}s representation.
	 */
	public String getRepresentation();

	/**
	 * Determine if the current type instance is within the defined bounds
	 * of the domain.
	 * @return {@literal true} if it is in the bounds, {@literal false} otherwise.
	 */
	public boolean isInsideBounds();

	/**
	 * Get the assigned dimensionality of the current {@code Type} object.
	 * @return The associated dimension value.
	 */
	public int getDimension();

	/**
	 * Randomise the current {@code Type} instance.
	 */
	public void randomise();

	/**
	 * Reset the current {@code Type} instance to default values.
	 */
	public void reset();

}
