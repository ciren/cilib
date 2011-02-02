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
package net.cilib.predef;

import com.google.common.base.Function;
import org.junit.Test;
import static net.cilib.predef.Predef.*;

/**
 *
 * @author gpampara
 */
public class ArrayTest {

    @Test
    public void map() {
        Array<Integer> l = array(Integer.valueOf(2));

        Array<Double> d = l.map(new Function<Integer, Double>() {
            @Override
            public Double apply(Integer from) {
                return from.doubleValue();
            }
        });
    }
}