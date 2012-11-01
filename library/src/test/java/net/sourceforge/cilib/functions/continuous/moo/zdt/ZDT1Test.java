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

/**
 *
 */
public class ZDT1Test {

    @Test
    public void testEvaluate01() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(1.0));
        for (int i = 0; i < 29; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT1 t1 = new ZDT1();
        MOFitness fitness = t1.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(1.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(0.0)));
    }

    @Test
    public void testEvaluate02() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(-1.0));
        for (int i = 0; i < 29; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT1 t1 = new ZDT1();
        MOFitness fitness = t1.getFitness(builder.build());

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
        ZDT1 t1 = new ZDT1();
        MOFitness fitness = t1.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(0.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(Double.NaN)));
    }
}
