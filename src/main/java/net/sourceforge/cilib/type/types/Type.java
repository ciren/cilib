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
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.util.Cloneable;

/**
 * {@code Type} interface for all type-objects that are used within CIlib.
 * The types are built on an extend the Java Collections Framework.
 */
public interface Type extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getClone();

    /**
     * Compare the specified object with this type for equality. Returns
     * {@code true} if and only if the specified object is also an instance
     * of the same type.
     * @param o The object to compare.
     * @return {@code true} if equality exists, {@code false} otherwise.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o);

    /**
     * Returns the hash code value for this list.  The hash code of a list
     * is defined to be the result of the following calculation:
     * <pre>
     *  int hashCode = 7;
     *  Iterator&lt;E&gt; i = list.iterator();
     *  while (i.hasNext()) {
     *      E obj = i.next();
     *      hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
     *  }
     * </pre>
     * This ensures that <tt>type1.equals(type2)</tt> implies that
     * <tt>type1.hashCode()==type2.hashCode()</tt> for any two types,
     * <tt>type1</tt> and <tt>type2</tt>, as required by the general
     * contract of {@link Object#hashCode}.
     *
     * @return the hash code value for this list
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    @Override
    public int hashCode();

}
