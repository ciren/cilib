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
package net.sourceforge.cilib.util;

import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.IsCollectionContaining.hasItem;

/**
 * TODO: Enable this unit test again. We need guice
 *
 * @author Theuns Cloete
 */
@Ignore
public class ClusteringUtilsTest {
    private static Vector centroids = null;
    private static ArrayList<Cluster<Vector>> arrangedClusters = null;
    private static StaticDataSetBuilder dataSetBuilder = null;
    private static ClusteringProblem problem = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dataSetBuilder = new StaticDataSetBuilder();
        dataSetBuilder.addDataSet(new MockClusteringStringDataSet());
        problem = new ClusteringProblem();
        Algorithm algorithm = new PSO();
        algorithm.setOptimisationProblem(problem);
        algorithm.performInitialisation();
        problem.setDomain("Z(0, 37),Z(0, 51)");
        problem.setNumberOfClusters(7);
        problem.setDataSetBuilder(dataSetBuilder);

        Vector.Builder centroidsBuilder = Vector.newBuilder();

        centroidsBuilder.add(Int.valueOf(1));
        centroidsBuilder.add(Int.valueOf(1));
        centroidsBuilder.add(Int.valueOf(33));
        centroidsBuilder.add(Int.valueOf(8));
        centroidsBuilder.add(Int.valueOf(20));
        centroidsBuilder.add(Int.valueOf(19));
        centroidsBuilder.add(Int.valueOf(11));
        centroidsBuilder.add(Int.valueOf(22));
        centroidsBuilder.add(Int.valueOf(30));
        centroidsBuilder.add(Int.valueOf(27));
        centroidsBuilder.add(Int.valueOf(19));
        centroidsBuilder.add(Int.valueOf(40));
        centroidsBuilder.add(Int.valueOf(5));
        centroidsBuilder.add(Int.valueOf(50));
        arrangedClusters = ClusteringUtils.arrangeClustersAndCentroids(centroids, problem, dataSetBuilder);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testArrangeClustersAndCentroids() {
        assertThat(arrangedClusters.size(), equalTo(6));

        assertThat(arrangedClusters.get(0).size(), equalTo(1));
        assertThat(arrangedClusters.get(1).size(), equalTo(26));
        assertThat(arrangedClusters.get(2).size(), equalTo(29));
        assertThat(arrangedClusters.get(3).size(), equalTo(18));
        assertThat(arrangedClusters.get(4).size(), equalTo(6));
        assertThat(arrangedClusters.get(5).size(), equalTo(13));
    }

    @Test
    public void testMiscStuff() {
        assertThat(problem.getNumberOfClusters(), equalTo(7));
        assertThat(dataSetBuilder.getNumberOfPatterns(), equalTo(93));
        assertThat(centroids.size(), equalTo(14));

        for (Cluster<Vector> cluster : arrangedClusters) {
            assertThat(cluster.getCentroid().size(), equalTo(2));

            for (Pattern<Vector> pattern : cluster) {
                assertThat(pattern.getData().size(), equalTo(2));
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPatternAssignments() {
        int counter = 0;
        // cluster 0
        Cluster<Vector> cluster = arrangedClusters.get(0);

        while (counter <= 0) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // cluster 1
        cluster = arrangedClusters.get(1);
        while (counter <= 26) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // cluster 2
        cluster = arrangedClusters.get(2);
        while (counter <= 40) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // cluster 3
        cluster = arrangedClusters.get(3);
        // this pattern can belong to either clusters 2 or 3, depending on the order of the centroids
        while (counter <= 41) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }
        // more cluster 2
        cluster = arrangedClusters.get(2);
        while (counter <= 48) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 3
        cluster = arrangedClusters.get(3);
        while (counter <= 51) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 2
        cluster = arrangedClusters.get(2);
        while (counter <= 52) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 3
        cluster = arrangedClusters.get(3);
        while (counter <= 56) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 2
        cluster = arrangedClusters.get(2);
        while (counter <= 60) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // cluster 4
        cluster = arrangedClusters.get(4);
        while (counter <= 61) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 3
        cluster = arrangedClusters.get(3);
        while (counter <= 69) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 2
        cluster = arrangedClusters.get(2);
        while (counter <= 71) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 4
        cluster = arrangedClusters.get(4);
        while (counter <= 73) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 3
        cluster = arrangedClusters.get(3);
        while (counter <= 75) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 2
        cluster = arrangedClusters.get(2);
        while (counter <= 76) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // more cluster 4
        cluster = arrangedClusters.get(4);
        while (counter <= 79) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // cluster 5
        cluster = arrangedClusters.get(5);
        while (counter <= 92) {
            Pattern<Vector> pattern = dataSetBuilder.getPattern(counter);

            assertThat(cluster, hasItem(pattern));
            ++counter;
        }

        // the last cluster is empty
        // should throw an exception (no such element / index out of bounds)
        assertThat(arrangedClusters.get(6), nullValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrangedCentroids() {
        assertThat(arrangedClusters.get(0).getCentroid(), equalTo(centroids.copyOfRange(0, 2)));

        assertThat(arrangedClusters.get(1).getCentroid(), equalTo(centroids.copyOfRange(2, 4)));

        assertThat(arrangedClusters.get(2).getCentroid(), equalTo(centroids.copyOfRange(4, 6)));

        assertThat(arrangedClusters.get(3).getCentroid(), equalTo(centroids.copyOfRange(6, 8)));

        assertThat(arrangedClusters.get(4).getCentroid(), equalTo(centroids.copyOfRange(8, 10)));

        assertThat(arrangedClusters.get(5).getCentroid(), equalTo(centroids.copyOfRange(10, 12)));

        // the last centroid is useless, i.e. no pattern "belongs" to it
        // should throw an exception (no such element / index out of bounds)
        arrangedClusters.get(6).getCentroid();
    }
}
