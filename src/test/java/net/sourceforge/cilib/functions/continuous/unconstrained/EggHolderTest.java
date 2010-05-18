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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class EggHolderTest {

    public EggHolderTest() {

    }

    @Test
    public void testEvaluate() {
        ContinuousFunction function = new EggHolder();
        function.setDomain("R(-512,512)^2");

        Vector vector = new Vector();
        vector.add(new Real(200));
        vector.add(new Real(100));

        Assert.assertEquals(-166.745338888944,function.apply(vector),0.00000000001);
    }

    @Test
    public void testMinimum() {
        ContinuousFunction function = new EggHolder();
        function.setDomain("R(-512,512)^2");

        Vector vector = new Vector();
        vector.add(new Real(512));
        vector.add(new Real(404.2319));

        Assert.assertEquals(-959.640662710616,function.apply(vector),0.00000000001);
    }
}
