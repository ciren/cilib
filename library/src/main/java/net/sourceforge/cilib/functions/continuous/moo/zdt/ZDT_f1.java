/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.zdt;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
final class ZDT_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 921516091265963637L;

    ZDT_f1() { }

    @Override
    public Double apply(Vector input) {
        return input.doubleValueOf(0);
    }
}
