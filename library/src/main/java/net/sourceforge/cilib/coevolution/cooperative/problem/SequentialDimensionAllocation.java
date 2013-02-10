/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.problem;

/**
 * This is an implementation of the {@linkplain DimensionAllocation} class which stores a
 * offset into the original problem vector. Therefore the index values are in a sequential order.
 *
 */
public class SequentialDimensionAllocation extends DimensionAllocation {
    private static final long serialVersionUID = 3575267573164099385L;
    private int startIndex;

    /**
     * Constructor
     */
    public SequentialDimensionAllocation(int startIndex, int size) {
        super(size);
        this.startIndex = startIndex;
    }

    /**
     * Copy constructor
     * @param copy
     */
    public SequentialDimensionAllocation(SequentialDimensionAllocation copy){
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
    public SequentialDimensionAllocation getClone() {
        return new SequentialDimensionAllocation(this);
    }

}
