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
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.io.pattern.StandardPattern;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.NeuralInputSource;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class PatternInputSourceTest {

    private StandardPattern standardPattern;

    @Before
    public void setup() {
        Vector vector = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        standardPattern = new StandardPattern(vector, vector);
    }

    @Test
    public void testGetNeuralInput() {
        NeuralInputSource source = new PatternInputSource(standardPattern);
        for (int i = 0; i < standardPattern.getVector().size(); i++) {
            Assert.assertEquals(standardPattern.getVector().doubleValueOf(i), source.getNeuralInput(i), Maths.EPSILON);
        }
    }
}
