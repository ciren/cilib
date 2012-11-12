/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
