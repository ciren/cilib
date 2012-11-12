/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.zdt;

import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ZDT3Test {

    @Test
    public void testEvaluate01() {
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < 30; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT3 t3 = new ZDT3();
        MOFitness fitness = t3.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(0.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(1.0)));
    }

    @Test
    public void testEvaluate02() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(-1.0));
        for (int i = 0; i < 29; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT3 t3 = new ZDT3();
        MOFitness fitness = t3.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(-1.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(Double.NaN)));
    }

    @Test
    public void testEvaluate03() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(0.0));
        builder.add(Real.valueOf(-29.0 / 9.0));
        for (int i = 0; i < 28; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT3 t3 = new ZDT3();
        MOFitness fitness = t3.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(0.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(Double.NaN)));
    }

    @Test
    public void testEvaluate04() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(1.0));
        for (int i = 0; i < 29; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT3 t3 = new ZDT3();
        MOFitness fitness = t3.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(1.0)));
        assertEquals(fitness.getFitness(1).getValue(), 0.0, 0.00001);
    }
}
