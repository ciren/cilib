/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.derating;

import net.sourceforge.cilib.functions.ContinuousFunction;

/**
 *
 */
public abstract class DeratingFunction extends ContinuousFunction {
    public abstract double getRadius();
}
