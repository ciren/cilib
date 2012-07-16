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
package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Real;

import org.junit.Test;

public class BlackboardTest {

    @Test
    public void toStringTest() {
        Blackboard<String, Real> properties = new Blackboard<String, Real>();

        properties.put("first", Real.valueOf(1.0));
        properties.put("second", Real.valueOf(2.0));

        // The output will be different as the hascode of the string "second" evaluates before
        // the value of "first"
        assertEquals("{second=2.0, first=1.0}", properties.toString());
    }
}
