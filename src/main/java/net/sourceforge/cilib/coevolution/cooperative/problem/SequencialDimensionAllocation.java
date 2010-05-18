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
package net.sourceforge.cilib.coevolution.cooperative.problem;

/**
 * This is an implimentation of the {@linkplain DimensionAllocation} class which stores a
 * offset into the original problem vector. Therefore the index values are in a sequencial order.
 *
 * @author leo
 */
public class SequencialDimensionAllocation extends DimensionAllocation {
    private static final long serialVersionUID = 3575267573164099385L;
    private int startIndex;

    /**
     * Constructor
     */
    public SequencialDimensionAllocation(int startIndex, int size) {
        super(size);
        this.startIndex = startIndex;
    }

    /**
     * Copy constructor
     * @param copy
     */
    public SequencialDimensionAllocation(SequencialDimensionAllocation copy){
        super(copy);
        startIndex = copy.startIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getProblemIndex(int elementIndex){
        return startIndex + elementIndex;
    }

    /**
     * {@inheritDoc}
     */
    public SequencialDimensionAllocation getClone() {
        return new SequencialDimensionAllocation(this);
    }

}
