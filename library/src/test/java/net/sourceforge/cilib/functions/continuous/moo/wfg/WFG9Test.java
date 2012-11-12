/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
public class WFG9Test {

    private static final int M = 3;
    private static final int k = 2 * (M - 1);
    private static final int l = 4;

    @Test
    public void testFitnessCalculation() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("library/src/test/resources/wfg/WFG9.data"));
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

            Vector expected_y = Problems.WFG9(x, k, M);

            Assert.assertThat(y.size(), is(equalTo(expected_y.size())));

            for (int i = 0; i < y.size(); ++i) {
                Assert.assertEquals(expected_y.doubleValueOf(i), y.doubleValueOf(i), 0.01);
            }
        }
        reader.close();
    }
}
