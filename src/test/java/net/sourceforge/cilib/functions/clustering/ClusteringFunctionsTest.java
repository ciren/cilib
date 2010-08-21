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
package net.sourceforge.cilib.functions.clustering;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * @author Theuns Cloete
 */
public class ClusteringFunctionsTest {

    @Test
    public void testCluster() {
        ArrayList<Vector> centroids = ClusteringFunctionTests.getSeparateCentroids();
        DataTable<StandardPattern, TypeList> dataTable = ClusteringFunctionTests.getDataTable();
        DistanceMeasure distanceMeasure = ClusteringFunctionTests.getDistanceMeasure();
        ArrayList<Cluster> clusters = ClusteringFunctions.cluster(centroids, dataTable, distanceMeasure, centroids.size());

        assertThat(clusters.size(), is(centroids.size()));
        assertThat(clusters.get(0).size(), is(1));
        assertThat(clusters.get(1).size(), is(2));
        assertThat(clusters.get(2).size(), is(3));
        assertThat(clusters.get(3).size(), is(4));
        assertThat(clusters.get(0).getCentroid(), is(centroids.get(0)));
        assertThat(clusters.get(1).getCentroid(), is(centroids.get(1)));
        assertThat(clusters.get(2).getCentroid(), is(centroids.get(2)));
        assertThat(clusters.get(3).getCentroid(), is(centroids.get(3)));
    }

    @Test
    public void testAssembleCentroids() {
        ArrayList<Vector> separated = new ArrayList<Vector>();

        separated.add(Vector.of(0.0, 1.0, 2.0));
        separated.add(Vector.of(3.0, 4.0, 5.0));
        separated.add(Vector.of(6.0, 7.0, 8.0));

        Vector assembled = ClusteringFunctions.assembleCentroids(separated);

        assertThat(assembled.size(), is(9));
        assertThat(assembled.get(0).doubleValue(), is(0.0));
        assertThat(assembled.get(1).doubleValue(), is(1.0));
        assertThat(assembled.get(2).doubleValue(), is(2.0));
        assertThat(assembled.get(3).doubleValue(), is(3.0));
        assertThat(assembled.get(4).doubleValue(), is(4.0));
        assertThat(assembled.get(5).doubleValue(), is(5.0));
        assertThat(assembled.get(6).doubleValue(), is(6.0));
        assertThat(assembled.get(7).doubleValue(), is(7.0));
        assertThat(assembled.get(8).doubleValue(), is(8.0));
    }

    @Test
    public void testDisassembleCentroids() {
        Vector assembled = Vector.of(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        List<Vector> separated = ClusteringFunctions.disassembleCentroids(assembled, 3);

        assertThat(separated.size(), is(3));
        assertThat(separated.get(0).size(), is(3));
        assertThat(separated.get(0).get(0).doubleValue(), is(0.0));
        assertThat(separated.get(0).get(1).doubleValue(), is(1.0));
        assertThat(separated.get(0).get(2).doubleValue(), is(2.0));
        assertThat(separated.get(1).size(), is(3));
        assertThat(separated.get(1).get(0).doubleValue(), is(3.0));
        assertThat(separated.get(1).get(1).doubleValue(), is(4.0));
        assertThat(separated.get(1).get(2).doubleValue(), is(5.0));
        assertThat(separated.get(2).size(), is(3));
        assertThat(separated.get(2).get(0).doubleValue(), is(6.0));
        assertThat(separated.get(2).get(1).doubleValue(), is(7.0));
        assertThat(separated.get(2).get(2).doubleValue(), is(8.0));
    }

    @Test
    public void testSignificantClusters() {
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        Cluster cluster = new Cluster(Vector.of(0.0, 1.0, 2.0));

        cluster.add(new StandardPattern(Vector.of(0.1, 1.1, 2.1), new StringType("1")));
        cluster.add(new StandardPattern(Vector.of(0.2, 1.2, 2.2), new StringType("1")));
        cluster.add(new StandardPattern(Vector.of(0.3, 1.3, 2.3), new StringType("1")));
        clusters.add(cluster);

        cluster = new Cluster(Vector.of(3.0, 4.0, 5.0));
        cluster.add(new StandardPattern(Vector.of(3.1, 4.1, 5.1), new StringType("2")));
        clusters.add(cluster);

        cluster = new Cluster(Vector.of(6.0, 7.0, 8.0));
        clusters.add(cluster);

        ArrayList<Cluster> significant = ClusteringFunctions.significantClusters(clusters);

        assertThat(significant.size(), is(2));
        assertThat(significant.get(0).size(), is(3));
        assertThat(significant.get(1).size(), is(1));
        assertThat(significant.get(0), sameInstance(clusters.get(0)));
        assertThat(significant.get(1), sameInstance(clusters.get(1)));
    }

    @Test
    public void testZMax() {
        Vector.Builder prototype = Vector.newBuilder();

        prototype.add(Real.valueOf(0.0, new Bounds(0.0, 3.0)));
        assertThat(ClusteringFunctions.zMax(prototype.build()), closeTo(3.0, ClusteringFunctionTests.EPSILON));

        prototype.add(Real.valueOf(0.0, new Bounds(0.0, 4.0)));
        assertThat(ClusteringFunctions.zMax(prototype.build()), closeTo(12.0, ClusteringFunctionTests.EPSILON));

        prototype.add(Real.valueOf(0.0, new Bounds(0.0, 5.0)));
        assertThat(ClusteringFunctions.zMax(prototype.build()), closeTo(60.0, ClusteringFunctionTests.EPSILON));

        prototype.add(Real.valueOf(0.0, new Bounds(1.0, 5.0)));
        assertThat(ClusteringFunctions.zMax(prototype.build()), closeTo(240.0, ClusteringFunctionTests.EPSILON));

        prototype.add(Real.valueOf(0.0, new Bounds(2.0, 5.0)));
        assertThat(ClusteringFunctions.zMax(prototype.build()), closeTo(720.0, ClusteringFunctionTests.EPSILON));
    }
}
