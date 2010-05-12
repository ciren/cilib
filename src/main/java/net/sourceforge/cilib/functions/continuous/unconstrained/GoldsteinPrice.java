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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The Goldstein-Price function.<br><br>
 *
 * Minimum: f(x) = 3; x = (0, -1)<br><br>
 *
 * -2 &lt;= x &lt;= 2<br><br>
 *
 * @author Gary Pampara
 *
 */
public class GoldsteinPrice extends ContinuousFunction {

    private static final long serialVersionUID = 5635493177950325746L;

    /**
     * Create a new instance of {@linkplain GoldsteinPrice}. Domain defaults to R(-2, 2)^2
     */
    public GoldsteinPrice() {
        setDomain("R(-2, 2)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GoldsteinPrice getClone() {
        return new GoldsteinPrice();
    }

    /**
     * Get the minimum of the function. The value of the minimum of 3.0.
     */
    @Override
    public Double getMinimum() {
        return 3.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        double part1 = 1 + (x + y + 1.0) * (x + y + 1.0) * (19.0 - 14.0 * x + 3 * x * x - 14 * y + 6 * x * y + 3 * y * y);
        double part2 = 30 + (2 * x - 3 * y) * (2 * x - 3 * y) * (18 - 32 * x + 12 * x * x + 48 * y - 36 * x * y + 27 * y * y);
        return part1 * part2;
    }
}
