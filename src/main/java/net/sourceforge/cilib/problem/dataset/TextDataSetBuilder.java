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
package net.sourceforge.cilib.problem.dataset;

/**
 * TODO: Complete this javadoc.
 */
public abstract class TextDataSetBuilder extends DataSetBuilder {
    private static final long serialVersionUID = -6352670028982771507L;

    public abstract void initialise();

    /**
     * Get the length of the shortest string contained in the processed
     * <tt>DataSet</tt> objects.
     * @return The length of the shortest string within this <tt>TextDataSetBuilder</tt>.
     */
    public abstract String getShortestString();

    /**
     * Get the length of the longest string contained in the processed
     * <tt>DataSet</tt> objects.
     * @return The length of the longest string within this <tt>TextDataSetBuilder</tt>.
     */
    public abstract String getLongestString();


    public abstract int size();

    public abstract String get(int index);

}
