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
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.util.Vectors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItems;

/**
 * @author Theuns Cloete
 */
public class ClusterTest {

    private static Cluster cluster;

    @BeforeClass
    public static void setUpClass() throws Exception {
        cluster = new Cluster();
        cluster.setCentroid(Vector.of(2, 3, 4));
        cluster.add(new StandardPattern(Vector.of(1, 2, 3), new StringType("Class1")));
        cluster.add(new StandardPattern(Vector.of(2, 3, 4), new StringType("Class2")));
        cluster.add(new StandardPattern(Vector.of(3, 4, 5), new StringType("Class3")));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        cluster = null;
    }

    @Test
    public void testGetClone() {
        Cluster clone = cluster.getClone();

        assertThat(cluster, not(sameInstance(clone)));
        assertThat(cluster.getCentroid(), equalTo(clone.getCentroid()));
        assertThat(cluster.getCentroid(), not(sameInstance(clone.getCentroid())));

        assertThat(clone, hasItems((StandardPattern []) cluster.toArray(new StandardPattern [] {})));
    }

    @Test
    public void testGetMeanVector() {
        Vector mean = cluster.getMean();

        assertThat(mean.doubleValueOf(0), is(2.0));
        assertThat(mean.doubleValueOf(1), is(3.0));
        assertThat(mean.doubleValueOf(2), is(4.0));
    }

    @Test
    public void testGetVarianceVector() {
        Vector variance = cluster.getVarianceVector();

        assertThat(variance.doubleValueOf(0), is(0.6666666666666666));
        assertThat(variance.doubleValueOf(1), is(0.6666666666666666));
        assertThat(variance.doubleValueOf(2), is(0.6666666666666666));
    }

    @Test
    public void testGetVarianceVectorCustomCenter() {
        Vector variance = cluster.getVarianceVector(cluster.getCentroid());

        assertThat(variance.doubleValueOf(0), is(0.6666666666666666));
        assertThat(variance.doubleValueOf(1), is(0.6666666666666666));
        assertThat(variance.doubleValueOf(2), is(0.6666666666666666));
    }

    @Test
    public void testGetVarianceScalar() {
        assertThat(cluster.getVariance(), is(1.1547005383792515));
    }

    @Test
    public void testGetVarianceScalarCustomCenter() {
        assertThat(cluster.getVariance(cluster.getCentroid()), is(1.1547005383792515));
    }

    @Test
    public void testGetStdDeviationVector() {
        Vector stdDev = cluster.getStdDeviationVector();

        assertThat(stdDev.doubleValueOf(0), is(0.816496580927726));
        assertThat(stdDev.doubleValueOf(1), is(0.816496580927726));
        assertThat(stdDev.doubleValueOf(2), is(0.816496580927726));
    }

    @Test
    public void testGetStdDeviationVectorCustomCenter() {
        Vector stdDev = cluster.getStdDeviationVector(cluster.getCentroid());

        assertThat(stdDev.doubleValueOf(0), is(0.816496580927726));
        assertThat(stdDev.doubleValueOf(1), is(0.816496580927726));
        assertThat(stdDev.doubleValueOf(2), is(0.816496580927726));
    }

    @Test
    public void testGetStdDeviationScalar() {
        assertThat(cluster.getStdDeviation(), is(1.074569931823542));
    }

    @Test
    public void testGetStdDeviationScalarCustomCenter() {
        assertThat(cluster.getStdDeviation(cluster.getCentroid()), is(1.074569931823542));
    }
}
