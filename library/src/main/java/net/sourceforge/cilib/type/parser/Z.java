/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 *
 */
final class Z implements TypeCreator {

    private static final long serialVersionUID = -5763440861780552761L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create() {
        return Int.valueOf(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(double value) {
        return Int.valueOf(Double.valueOf(value).intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(final Bounds bounds) {
        return Int.valueOf(0, bounds);
    }
}
