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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Schwefel function.
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Separable</li>
 * <li>Discontinuous</li>
 * </ul>
 *
 * f(x) = 0; x = (-420.9687,...,-420.9687);
 *
 * x e [-512.03,511.97]
 *
 * @author  Edwin Peer
 */
// TODO: Check discontinuous / continuous
public class Schwefel extends ContinuousFunction { // ?
    private static final long serialVersionUID = 3835871629510784855L;

    /**
     * Create a new instance of {@linkplain Schwefel}.
     */
    public Schwefel() {
        setDomain("R(-512.03, 511.97)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schwefel getClone() {
        return new Schwefel();
    }

    /**
     * Get the minimum of the function. It is defined to be a value of <code>0.0</code>.
     * @return The function minimum value.
     */
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sum = 0;
        for (int i = 0; i < input.getDimension(); ++i) {
            sum += input.getReal(i) * Math.sin(Math.sqrt(Math.abs(input.getReal(i))));
        }
        sum += getDimension() * 4.18982887272434686131e+02;
        return sum;
    }

}
