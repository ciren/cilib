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
public class ZDT6Test {

    @Test
    public void testEvaluate01() {
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < 10; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT6 t6 = new ZDT6();
        MOFitness fitness = t6.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(1.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(0.0)));
    }

    @Test
    public void testEvaluate02() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(0.0));
        builder.add(Real.valueOf(1.0 / (9.0 * 9.0 * 9.0)));
        for (int i = 0; i < 8; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT6 t6 = new ZDT6();
        MOFitness fitness = t6.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(1.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(1.5)));
    }

    @Test
    public void testEvaluate03() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(Real.valueOf(0.0));
        builder.add(Real.valueOf(-9.0));
        for (int i = 0; i < 8; ++i) {
            builder.add(Real.valueOf(0.0));
        }
        ZDT6 t6 = new ZDT6();
        MOFitness fitness = t6.getFitness(builder.build());

        assertThat(fitness.getFitness(0).getValue(), is(equalTo(1.0)));
        assertThat(fitness.getFitness(1).getValue(), is(equalTo(Double.NaN)));
    }
}
