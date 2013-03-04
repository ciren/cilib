/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Michalewicz funtion 12.</p>
 *
 ** <p><b>Reference:</b>
 *  http://www.geatbx.com/docu/fcnindex-01.html#TopOfPage</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> if n=5 then f(<b>x</b>*) = -4.687 </li>
 * <li> if n=10 then f(<b>x</b>*) = -9.66 </li>
 * <li> for x<sub>i</sub> in [0, pi]</li>
 * </ul>
 * </p>
 *
 * Characteristics:
 * <ul>
 * <li>Multi-modal</li>
 * <li>Has n! local minima</li>
 * <li>Non-separable</li>
 * </ul>
 *
 * R(0, 3.141592653589793)^10
 *
 */
public class Michalewicz implements ContinuousFunction {

    private static final long serialVersionUID = -4391269929189674709L;
    /**
     * m controls the steepness of the valleys; the larger m, the
     * more difficult the search
     */
    private int m = 10;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sumsq = 0.0;

        for (int i = 0; i < input.size(); i++) {
            double x = input.doubleValueOf(i);
            sumsq += Math.sin(x) * Math.pow(Math.sin(((i+1) * x * x)/Math.PI), 2*m);
        }

        return -sumsq;
    }

    /**
     * Get the current value of <code>M</code>.
     * @return The value of <code>M</code>.
     */
    public int getM() {
        return m;
    }

    /**
     * Set the value of <code>M</code>.
     * @param m The value to set.
     */
    public void setM(int m) {
        this.m = m;
    }
}
