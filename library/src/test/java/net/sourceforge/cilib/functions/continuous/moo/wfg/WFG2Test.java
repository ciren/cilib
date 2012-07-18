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
package net.sourceforge.cilib.functions.continuous.moo.wfg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 */
public class WFG2Test {

    private static final int M = 3;
    private static final int k = 2 * (M - 1);
    private static final int l = 4;

    @Test
    public void testFitnessCalculation() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("library/src/test/resources/wfg/WFG2.data"));
        String line = "";
        while ((line = reader.readLine()) != null) {
            String[] components = line.split("\\s");

            Vector.Builder builder = Vector.newBuilder();
            for (int i = 0; i < k + l; ++i) {
                builder.add(Double.parseDouble(components[i]));
            }
            Vector x = builder.build();

            builder = Vector.newBuilder();
            for (int i = k + l; i < k + l + M; ++i) {
                builder.add(Double.parseDouble(components[i]));
            }
            Vector y = builder.build();

            Vector expected_y = Problems.WFG2(x, k, M);

            Assert.assertThat(y.size(), is(equalTo(expected_y.size())));

            for (int i = 0; i < y.size(); ++i) {
                Assert.assertEquals(expected_y.doubleValueOf(i), y.doubleValueOf(i), 0.001);
            }
        }
        reader.close();
    }
}
