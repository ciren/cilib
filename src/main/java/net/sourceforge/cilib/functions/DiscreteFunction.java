/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
