/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition.nnperformancecomparators;

import java.util.ArrayList;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * An interface for classes that compares the performance of two neural networks.
 */
public interface NNPerformanceComparator extends Cloneable {

    /**
     * {@inheritDoc }
     */
    @Override
    public NNPerformanceComparator getClone();

    /**
     * Checks if the outputs of two NNs are statistically similar.
     * @param validationSet The validation set to use for comparison.
     * @param leftNN The validation outputs of the left NN.
     * @param rightNN The validation outputs of the right NN.
     * @return True if the two NNs are statistically similar.
     */
    public boolean isSame(StandardPatternDataTable validationSet, ArrayList<Vector> leftNN, ArrayList<Vector> rightNN);

    /**
     * Gets the p-value obtained during the previous performance of the test.
     * @return The p-value from the last time the test was performed.
     */
    public double getLastPValue();

    /**
     * Gets the p-value needed to obtain the target confidence level.
     * @return The target p-value that must be obtained.
     */
    public double getTargetPValue();
}
