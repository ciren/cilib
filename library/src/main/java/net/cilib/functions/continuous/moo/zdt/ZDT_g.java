/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.moo.zdt;

import net.cilib.functions.ContinuousFunction;
import net.cilib.type.types.container.Vector;

/**
 *
 */
final class ZDT_g extends ContinuousFunction {

    private static final long serialVersionUID = -1677096322807541565L;

    ZDT_g() { }


    @Override
    public Double f(Vector input) {
        double sum = 0.0;
        for (int i = 1; i < input.size(); ++i) {
            sum += input.doubleValueOf(i);
        }
        return 1.0 + 9.0 * sum / (input.size() - 1.0);
    }
}
