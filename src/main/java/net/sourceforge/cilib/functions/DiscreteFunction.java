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
package net.sourceforge.cilib.functions;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Gary Pampara
 */
public abstract class DiscreteFunction extends AbstractFunction<Vector, Integer> {
    private static final long serialVersionUID = -1966158048234228064L;

    /**
     * Create an instance of {@linkplain DiscreteFunction}.
     */
    protected DiscreteFunction() {
    }

    /**
     * {@inheritDoc}
     */
    public Integer getMinimum() {
        return -Integer.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    public Integer getMaximum() {
        return Integer.MAX_VALUE;
    }

}
