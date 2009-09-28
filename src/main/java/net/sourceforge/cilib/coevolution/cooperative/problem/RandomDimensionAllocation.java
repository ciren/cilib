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

import java.util.ArrayList;
import java.util.List;

/**
 * This is an implimentation of the {@linkplain DimensionAllocation} class which stores a
 * list of randomly generated index values.
 *
 * @author leo
 */
public class RandomDimensionAllocation extends DimensionAllocation {
    private static final long serialVersionUID = -2006187323873094124L;
    private List<Integer> indexList;

    /**
     * Constructor
     */
    public RandomDimensionAllocation(List<Integer> indicies) {
        super(indicies.size());
        indexList = indicies;
    }

    /**
     * Copy constructor
     * @param copy
     */
    public RandomDimensionAllocation(RandomDimensionAllocation copy) {
        super(copy);
        indexList = new ArrayList<Integer>(copy.indexList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomDimensionAllocation getClone() {
        return new RandomDimensionAllocation(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getProblemIndex(int elementIndex) {
        return indexList.get(elementIndex);
    }

}
