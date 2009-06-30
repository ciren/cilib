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
package net.sourceforge.cilib.container;

/**
 * Very basic array abstraction.
 */
public interface Array {

    /**
     * Get the element at the provided index.
     * @param <E> The generic type.
     * @param index The index of the desired element.
     * @return The element at {@code index}, if available.
     */
    public <E> E get(int index);

    /**
     * Set the element at {@code index}.
     * @param <E> The generic type.
     * @param object The object to place within the array.
     * @param index The index to set the object at.
     */
    public <E> void set(E object, int index);

    /**
     * Obtain the current size of the array structure.
     * @return The array length.
     */
    public int size();

}
