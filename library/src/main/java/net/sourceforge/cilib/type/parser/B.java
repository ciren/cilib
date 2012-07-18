/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 *
 */
final class B implements TypeCreator {

    private static final long serialVersionUID = 7124782787032789332L;
    private RandomProvider random = new MersenneTwister();

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create() {
        return Bit.valueOf(random.nextBoolean());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(double value) {
        if (Double.compare(value, 0.0) == 0) {
            return Bit.valueOf(false);
        } else if (Double.compare(value, 1.0) == 0) {
            return Bit.valueOf(true);
        }
        throw new UnsupportedOperationException("Cannot create a bit type with the specified value.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(final Bounds bounds) {
        throw new UnsupportedOperationException("Bit types cannot be constructed with bounds");
    }
}
