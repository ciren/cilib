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
