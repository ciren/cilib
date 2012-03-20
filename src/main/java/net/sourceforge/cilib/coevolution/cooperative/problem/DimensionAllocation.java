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

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.measurement.single.Fitness;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This abstract class hides the detailes of which sub-dimensions a participating
 * algorithm in a {@linkplain CooperativeCoevolutionAlgorithm} optimizes.
 * <p>
 * The {@linkplain CooperativeCoevolutionProblemAdapter} class uses the concrete
 * {@linkplain DimensionAllocation} to copy the solution from a participating
 * {@linkplain Entity} in a participating {@linkplain PopulationBasedAlgorithm}
 * into the contet vector so that its {@linkplain Fitness} can be calculated.
 */
public abstract class DimensionAllocation implements Cloneable {
    private int size;

    /**
     * Constructor
     */
    public DimensionAllocation(){
        size = 0;
    }

    /**
     * Constructor
     * @param size The number of dimensions stored by this {@linkplain DimensionAllocation}.
     */
    public DimensionAllocation(int size){
        this.size = size;
    }

    /**
     * Copy constructor.
     * @param copy
     */
    public DimensionAllocation(DimensionAllocation copy){
        size = copy.size;
    }

    /**
     * Return the number of dimensions stored by this {@linkplain DimensionAllocation}.
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * Return the index into the original problem vector of the element at the given index into the participant's solution.
     * @param elementIndex The index into the participant's soltion vector.
     * @return The index of the same value for the original problem vector.
     */
    public abstract int getProblemIndex(int elementIndex);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract DimensionAllocation getClone();
}
