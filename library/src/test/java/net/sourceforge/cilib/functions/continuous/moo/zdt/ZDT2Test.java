/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.moo.zdt;

import net.cilib.problem.solution.MOFitness;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class ZDT2Test {

    @Test
    public void testEvaluate01() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(1.0));
        for (int i = 0; i < 29; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT2 t2 = new ZDT2();
        MOFitness fitness = t2.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(1.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(0.0)));
    }

    @Test
    public void testEvaluate02() {
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < 30; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT2 t2 = new ZDT2();
        MOFitness fitness = t2.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(0.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(1.0)));
    }

    @Test
    public void testEvaluate03() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(0.0));
        builder.add(Real.valueOf(-29.0 / 9.0));
        for (int i = 0; i < 28; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT2 t2 = new ZDT2();
        MOFitness fitness = t2.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(0.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(Double.NaN)));
    }
}
