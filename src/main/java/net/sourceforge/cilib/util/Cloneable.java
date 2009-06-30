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
package net.sourceforge.cilib.util;

import java.io.Serializable;

/**
 * <p>
 * This interface provides a publicly available {@code getClone()} method that
 * can be used to obtain a cloned version of the instance on which the
 * method was invoked.
 * </p>
 * <p>
 * This interface was added in order to bypass the current issues with
 * {@code clone()} as provided with the JDK. The protected status of
 * {@code Object.clone()} prevented some operations that were needed for CILib.
 * </p>
 * <p>
 * Another consideration was the problems associated with {@code Object.clone()}
 * as discussed by Joshua Bloch.
 * </p>
 *
 * @see "Item 11 of the 2<sup>nd</sup> edition of <emp>Effective Java</emp> by Joshua Bloch."
 */
public interface Cloneable extends Serializable {

    /**
     * Create a cloned copy of the current object and return it. In general
     * the created copy will be a deep copy of the provided instance. As
     * a result this operation an be quite expensive if used incorrectly.
     * @return An exact clone of the current object instance.
     * @see Object#clone()
     */
    public Object getClone();

}
