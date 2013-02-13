/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions;

import fj.F;

public abstract class DynamicFunction<A, T> extends F<A, T> {

    public abstract T getOptimum();

    public abstract void changeEnvironment();
}
