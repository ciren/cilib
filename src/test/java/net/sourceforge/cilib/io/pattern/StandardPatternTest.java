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
package net.sourceforge.cilib.io.pattern;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class StandardPatternTest {

    private Vector feature;
    private StringType klas;
    private StandardPattern pattern1;
    private StandardPattern pattern2;
    private StandardPattern pattern3;

    @Before
    public void setup() {
        feature = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        klas = new StringType("class1");

        pattern1 = new StandardPattern();
        pattern1.setVector(feature);
        pattern1.setTarget(klas);

        pattern2 = new StandardPattern(feature, klas);
        pattern3 = new StandardPattern(pattern1);
    }


    @Test
    public void testConstructors() {
        Assert.assertEquals(feature, pattern1.getVector());
        Assert.assertEquals(feature, pattern2.getVector());
        Assert.assertEquals(feature, pattern3.getVector());

        Assert.assertEquals(klas, pattern1.getTarget());
        Assert.assertEquals(klas, pattern2.getTarget());
        Assert.assertEquals(klas, pattern3.getTarget());
    }
}
