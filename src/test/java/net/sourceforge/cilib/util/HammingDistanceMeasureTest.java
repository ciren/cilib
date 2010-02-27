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

import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Theuns Cloete
 */
public class HammingDistanceMeasureTest {

    @Test(expected=IllegalStateException.class)
    public void testDistance() {
        DistanceMeasure measure = new HammingDistanceMeasure();
        Vector lhs = Vector.of(1.0, 2.0, 3.0, 4.0, 5.0);
        Vector rhs = Vector.of(1.0, 5.0, 3.0, 2.0, 4.0);

        assertThat(measure.distance(lhs, rhs), is(3.0));

        rhs = Vector.of(1.0, 5.0, 3.0, 2.0, 4.0, 99.0);
        measure.distance(lhs, rhs);
    }
}
