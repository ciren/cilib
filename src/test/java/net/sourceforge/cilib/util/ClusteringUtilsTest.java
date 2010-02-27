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
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.clustering.QuantisationErrorFunction;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Theuns Cloete
 */
public class ClusteringUtilsTest {
    private static Vector centroids;
    private static StaticDataSetBuilder dataSetBuilder;
    private static ClusteringProblem problem;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dataSetBuilder = new StaticDataSetBuilder();
        dataSetBuilder.addDataSet(new MockClusteringStringDataSet());
        dataSetBuilder.setIdentifier("mock-data-set-builder");
        problem = new ClusteringProblem();
        ContinuousFunction function = new QuantisationErrorFunction();
        FunctionOptimisationProblem innerProblem = new FunctionMinimisationProblem();
        innerProblem.setFunction(function);
        problem.setInnerProblem(innerProblem);
        problem.setDomain("Z(-5, 5),Z(-5, 5)");
        problem.setNumberOfClusters(4);
        problem.setDataSetBuilder(dataSetBuilder);

        Algorithm algorithm = new PSO();
        algorithm.setOptimisationProblem(problem);
        algorithm.performInitialisation();

        centroids = Vector.of(2, 2, -2, 2, -2, -2, 2, -2);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        centroids = null;
        dataSetBuilder = null;
        problem = null;
    }

    @Test
    public void testMiscStuff() {
        assertThat(problem.getNumberOfClusters(), equalTo(4));
        assertThat(dataSetBuilder.getNumberOfPatterns(), equalTo(6));
        assertThat(centroids.size(), equalTo(8));
    }

    private void testArrangeClustersAndCentroids(ArrayList<Cluster<Vector>> arrangedClusters) {
        assertThat(arrangedClusters.size(), is(3));

        assertThat(arrangedClusters.get(0).size(), is(1));
        assertThat(arrangedClusters.get(0).getCentroid(), equalTo(Vector.of(-2, 2)));
        assertThat(arrangedClusters.get(0).getMean(), equalTo(Vector.of(-1, 2)));

        assertThat(arrangedClusters.get(1).size(), is(2));
        assertThat(arrangedClusters.get(1).getCentroid(), equalTo(Vector.of(-2, -2)));
        assertThat(arrangedClusters.get(1).getMean(), equalTo(Vector.of(-1.5, -1)));

        assertThat(arrangedClusters.get(2).size(), is(3));
        assertThat(arrangedClusters.get(2).getCentroid(), equalTo(Vector.of(2, -2)));
        assertThat(arrangedClusters.get(2).getMean(), equalTo(Vector.of(1.3333333333333333, -1.3333333333333333)));
    }

    @Test
    public void testArrangeClustersAndCentroidsCombined() {
        testArrangeClustersAndCentroids(ClusteringUtils.arrangeClustersAndCentroids(centroids, problem, dataSetBuilder));
    }

    @Test
    public void testArrangeClustersAndCentroidsSeparate() {
        testArrangeClustersAndCentroids(ClusteringUtils.arrangeClustersAndCentroids(ClusteringUtils.disassembleCentroids(centroids, 4), problem, dataSetBuilder));
    }

    @Test
    public void testAssembleCentroids() {
        ArrayList<Vector> separated = new ArrayList<Vector>();

        separated.add(Vector.of(0.0, 1.0, 2.0));
        separated.add(Vector.of(3.0, 4.0, 5.0));
        separated.add(Vector.of(6.0, 7.0, 8.0));

        Vector assembled = ClusteringUtils.assembleCentroids(separated);

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
        List<Vector> separated = ClusteringUtils.disassembleCentroids(assembled, 3);

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
    public void testFormClusters() {
        ArrayList<Vector> separate = ClusteringUtils.disassembleCentroids(centroids, 4);
        ArrayList<Cluster<Vector>> clusters = ClusteringUtils.formClusters(separate, problem, dataSetBuilder.getPatterns());

        assertThat(clusters.size(), is(4));

        assertThat(clusters.get(0).size(), is(0));
        assertThat(clusters.get(0).getCentroid(), sameInstance(separate.get(0)));

        assertThat(clusters.get(1).size(), is(1));
        assertThat(clusters.get(1).getCentroid(), sameInstance(separate.get(1)));
        assertThat(clusters.get(1).getMean(), equalTo(Vector.of(-1, 2)));

        assertThat(clusters.get(2).size(), is(2));
        assertThat(clusters.get(2).getCentroid(), sameInstance(separate.get(2)));
        assertThat(clusters.get(2).getMean(), equalTo(Vector.of(-1.5, -1)));

        assertThat(clusters.get(3).size(), is(3));
        assertThat(clusters.get(3).getCentroid(), sameInstance(separate.get(3)));
        assertThat(clusters.get(3).getMean(), equalTo(Vector.of(1.3333333333333333, -1.3333333333333333)));
    }

    @Test
    public void testSignificantClusters() {
        ArrayList<Cluster<Vector>> clusters = new ArrayList<Cluster<Vector>>();
        Cluster<Vector> cluster = new Cluster<Vector>(Vector.of(0.0, 1.0, 2.0));

        cluster.add(new Pattern<Vector>(Vector.of(0.1, 1.1, 2.1), "1"));
        cluster.add(new Pattern<Vector>(Vector.of(0.2, 1.2, 2.2), "1"));
        cluster.add(new Pattern<Vector>(Vector.of(0.3, 1.3, 2.3), "1"));
        clusters.add(cluster);

        cluster = new Cluster<Vector>(Vector.of(3.0, 4.0, 5.0));
        cluster.add(new Pattern<Vector>(Vector.of(3.1, 4.1, 5.1), "2"));
        clusters.add(cluster);

        cluster = new Cluster<Vector>(Vector.of(6.0, 7.0, 8.0));
        clusters.add(cluster);

        ArrayList<Cluster<Vector>> significant = ClusteringUtils.significantClusters(clusters);

        assertThat(significant.size(), is(2));
        assertThat(significant.get(0).size(), is(3));
        assertThat(significant.get(1).size(), is(1));
        assertThat(significant.get(0), sameInstance(clusters.get(0)));
        assertThat(significant.get(1), sameInstance(clusters.get(1)));
    }
}
