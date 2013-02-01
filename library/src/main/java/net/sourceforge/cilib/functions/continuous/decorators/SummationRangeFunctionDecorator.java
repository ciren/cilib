/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Real;

/**
 * Implementation to sum a function that is applied to certain input ranges as specified
 * in CEC2010 Large Scale Global Optimization.
 * <p>
 * Reference:
 * </p>
 * <p>
 * K. Tang, Xiaodong Li, P. N. Suganthan, Z. Yang and T. Weise,
 * Benchmark Functions for the CEC'2010 Special Session and Competition on
 * Large Scale Global Optimization. Technical Report, Nature Inspired Computation and
 * Applications Laboratory, USTC, China, http://nical.ustc.edu.cn/cec10ss.php,
 * and Nanyang Technological University, 2009.
 * </p>
 */
public class SummationRangeFunctionDecorator implements ContinuousFunction {

    private RangeFunctionDecorator range;
    private ControlParameter lower;
    private ControlParameter upper;
    private int groupSize;

    /**
     * Create an instance of the decorator.
     */
    public SummationRangeFunctionDecorator() {
        range = new RangeFunctionDecorator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        int lowerIndex = Real.valueOf(lower.getParameter()).intValue();
        int upperIndex = Real.valueOf(upper.getParameter()).intValue();

        double sum = 0.0;

        for(int k = lowerIndex; k < upperIndex; k++) {
            int start = k * groupSize;
            int end = (k + 1) * groupSize;

            range.setStart(ConstantControlParameter.of(start));
            range.setEnd(ConstantControlParameter.of(end));
            sum += range.apply(input);
        }

        return sum;
    }

    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getFunction() {
        return range.getFunction();
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setFunction(ContinuousFunction function) {
        range.setFunction(function);
    }

    /**
     * Get the lower limit associated with the summation operator.
     * @return The lower limit of the summation operator.
     */
    public ControlParameter getLower() {
        return this.lower;
    }

    /**
     * Set the lower limit of the summation operator.
     * @param lower The lower limit.
     */
    public void setLower(ControlParameter lower) {
        this.lower = lower;
    }

    /**
     * Get the upper limit associated with the summation operator.
     * @return The upper limit of the summation operator.
     */
    public ControlParameter getUpper() {
        return this.upper;
    }

    /**
     * Set the upper limit of the summation operator.
     * @param upper The upper limit.
     */
    public void setUpper(ControlParameter upper) {
        this.upper = upper;
    }

    /**
     * Get the group size associated with the summation function.
     * @return The group size of the function.
     */
    public int getGroupSize() {
        return this.groupSize;
    }

    /**
     * Set the groupSize of the summation function.
     * @param groupSize The group size.
     */
    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

}
