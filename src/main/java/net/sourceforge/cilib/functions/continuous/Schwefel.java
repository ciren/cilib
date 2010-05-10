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
 * R(-512.03, 511.97)^30
 *
 * @author  Edwin Peer
 */
// TODO: Check discontinuous / continuous
public class Schwefel implements ContinuousFunction { // ?

    private static final long serialVersionUID = 3835871629510784855L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;
        for (int i = 0; i < input.size(); ++i) {
            sum += input.doubleValueOf(i) * Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i))));
        }
        return sum + input.size() * 4.18982887272434686131e+02;
    }
}
