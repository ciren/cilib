/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;


/**
 */
final class R implements TypeCreator {
    private static final long serialVersionUID = -3393953231231613279L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create() {
        return Real.valueOf(0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(double value) {
        return Real.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(final Bounds bounds) {
        return Real.valueOf(0.0, bounds);
    }

}
