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
package net.sourceforge.cilib.util;

import com.google.common.base.Supplier;
import net.sourceforge.cilib.controlparameter.ControlParameter;

/**
 *
 * @author Wiehann Matthysen
 */
public final class ControlParameters {

    private ControlParameters() {
    }

    public static Supplier<Number> supplierOf(final ControlParameter controlParameter) {
        return new Supplier<Number>() {

            @Override
            public Number get() {
                return controlParameter.getParameter();
            }
        };
    }

    public static Supplier<Number> supplierOf(final ControlParameter controlParameter, final double min, final double max) {
        return new Supplier<Number>() {

            @Override
            public Number get() {
                return controlParameter.getParameter(min, max);
            }
        };
    }
}
