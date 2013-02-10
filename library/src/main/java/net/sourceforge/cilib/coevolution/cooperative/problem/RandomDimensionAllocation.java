/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation of the {@linkplain DimensionAllocation} class which stores a
 * list of randomly generated index values.
 *
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
