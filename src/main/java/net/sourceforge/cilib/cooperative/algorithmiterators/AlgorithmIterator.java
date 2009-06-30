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
package net.sourceforge.cilib.cooperative.algorithmiterators;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.util.Cloneable;

/**
 * TODO: Complete this Javadoc.
 *
 * @param <E> The type.
 */
public interface AlgorithmIterator<E> extends ListIterator<E>, Cloneable {

    @Override
    public AlgorithmIterator<E> getClone();

    public E current();

    public void setAlgorithms(List<E> algorithms);
}
