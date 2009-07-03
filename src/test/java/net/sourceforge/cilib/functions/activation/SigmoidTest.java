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
package net.sourceforge.cilib.functions.activation;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;

import org.junit.Test;

public class SigmoidTest {

    @Test
    public void evaluate() {
        Sigmoid sigmoid = new Sigmoid();
        assertEquals(0.5, sigmoid.evaluate(Double.valueOf(0.0)), 0);
        assertEquals(1.0, sigmoid.evaluate(Double.POSITIVE_INFINITY), 0);
        assertEquals(1.0, sigmoid.evaluate(Double.MAX_VALUE), 0);
        assertEquals(0.0, sigmoid.evaluate(-Double.MAX_VALUE), 0);
        assertEquals(0.0, sigmoid.evaluate(Double.NEGATIVE_INFINITY), 0);
        assertEquals(0.5, sigmoid.evaluate(Double.MIN_VALUE), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invalidEvaluate() {
        Sigmoid sigmoid = new Sigmoid();
        sigmoid.setSteepness(new ConstantControlParameter(-8.0));

        sigmoid.evaluate(0.0);
    }
}
