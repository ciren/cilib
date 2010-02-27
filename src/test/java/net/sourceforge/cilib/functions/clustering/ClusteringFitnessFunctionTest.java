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

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * TODO: Algorithm.get() breaks testability. Enable this unit test once we have a guiced Algorithm.
 *
 * @author Theuns Cloete
 */
@Ignore
public class ClusteringFitnessFunctionTest {
    private static ClusteringFitnessFunction function = null;
    private static StaticDataSetBuilder dataSetBuilder = null;
    private static Vector centroids = null;
    private static ClusteringProblem problem = null;
    private static FunctionOptimisationProblem innerProblem = null;
    private static final double DELTA = 0.000000000001;

    @BeforeClass
    public static void intialise() {
        dataSetBuilder = new StaticDataSetBuilder();
        dataSetBuilder.addDataSet(new MockClusteringStringDataSet());
        dataSetBuilder.setIdentifier("mock-data-set-builder");
        function = new QuantisationErrorFunction();
        innerProblem = new FunctionMinimisationProblem();
        innerProblem.setFunction(function);
        problem = new ClusteringProblem();
        Algorithm algorithm = new PSO();
        algorithm.setOptimisationProblem(problem);
        algorithm.performInitialisation();
        problem.setDomain("Z(-5, 5),Z(-5, 5)");
        problem.setInnerProblem(innerProblem);
        problem.setNumberOfClusters(4);
        problem.setDataSetBuilder(dataSetBuilder);

        centroids = Vector.of(2, 2, -2, 2, -2, -2, 2, -2);
    }

    @AfterClass
    public static void destroy() {
        centroids = null;
        dataSetBuilder = null;
        problem = null;
        function = null;
    }

    @Test
    public void testEvaluate() {
        // the evaluate method arranges the clusters and centroids
        // 1.1150593228814152913
        double quantisation = 1.0 / 1.0;
        quantisation += (1.0 + Math.sqrt(2.0)) / 2.0;
        quantisation += (1.0 + 1.0 + Math.sqrt(2.0)) / 3.0;
        assertEquals(quantisation / 3.0, function.apply(centroids), DELTA);
    }

    @Test
    public void testQuantisationError() {
        // 1.1150593228814152913
        double quantisation = 1.0 / 1.0;
        quantisation += (1.0 + Math.sqrt(2.0)) / 2.0;
        quantisation += (1.0 + 1.0 + Math.sqrt(2.0)) / 3.0;
        assertEquals(quantisation / 3.0, function.calculateQuantisationError(), DELTA);
    }

    @Test
    public void testMaximumAverageDistance() {
        // from cluster 1: 1.2071067811865475244
        double maximumAverage = (1.0 + Math.sqrt(2.0)) / 2.0;
        assertEquals(maximumAverage, function.calculateMaximumAverageDistance(), DELTA);
    }

    @Test
    public void testMinimumInterClusterDistance() {
        // same for all clusters
        assertEquals(4.0, function.calculateMinimumInterClusterDistance(), DELTA);
    }

    @Test
    public void testMaximumInterClusterDistance() {
        // same for all clusters
        // 5.6568542494923801952
        double maximumInter = Math.sqrt(Math.pow(4.0, 2.0) + Math.pow(4.0, 2.0));
        assertEquals(maximumInter, function.calculateMaximumInterClusterDistance(), DELTA);
    }

    @Test
    public void testMinimumSetDistance() {
        assertEquals(3.0, function.calculateMinimumSetDistance(0, 1), DELTA);

        // 3.6055512754639892931
        double minimumSet = Math.sqrt(Math.pow(3.0, 2.0) + Math.pow(2.0, 2.0));
        assertEquals(minimumSet, function.calculateMinimumSetDistance(0, 2), DELTA);

        assertEquals(2.0, function.calculateMinimumSetDistance(1, 2), DELTA);
    }

    @Test
    public void testAverageSetDistance() {
        // 3.0811388300841896660
        double averageSet = 3.0;
        averageSet += Math.sqrt(Math.pow(1.0, 2.0) + Math.pow(3.0, 2.0));
        averageSet /= 2.0;
        assertEquals(averageSet, function.calculateAverageSetDistance(0, 1), DELTA);

        // 4.1067759725276179441
        averageSet = Math.sqrt(Math.pow(3.0, 2.0) + Math.pow(2.0, 2.0));
        averageSet += Math.sqrt(Math.pow(3.0, 2.0) + Math.pow(3.0, 2.0));
        averageSet += Math.sqrt(Math.pow(4.0, 2.0) + Math.pow(2.0, 2.0));
        averageSet /= 3.0;
        assertEquals(averageSet, function.calculateAverageSetDistance(0, 2), DELTA);

        // 2.6046642240893420780
        averageSet = 3.0 + 2.0;
        averageSet += 4.0 + 3.0;
        averageSet += Math.sqrt(Math.pow(1.0, 2.0) + Math.pow(3.0, 2.0));
        averageSet += Math.sqrt(Math.pow(1.0, 2.0) + Math.pow(2.0, 2.0));
        averageSet /= 6.0;
        assertEquals(averageSet, function.calculateAverageSetDistance(1, 2), DELTA);
    }

    @Test
    public void testClusterDiameter() {
        assertEquals(0.0, function.calculateClusterDiameter(0), DELTA);
        assertEquals(1.0, function.calculateClusterDiameter(1), DELTA);
        assertEquals(Math.sqrt(2.0), function.calculateClusterDiameter(2), DELTA);
    }

    @Test
    public void testIntraClusterDistance() {
        // 6.8284271247461900976
        double intra = 1.0 + 1.0 + Math.sqrt(2.0) + 1.0 + 1.0 + Math.sqrt(2.0);
        assertEquals(intra, function.calculateIntraClusterDistance(), DELTA);
    }

    @Test
    public void testAverageIntraClusterDistance() {
        // 2.2761423749153966992
        double intra = 1.0 + 1.0 + Math.sqrt(2.0) + 1.0 + 1.0 + Math.sqrt(2.0);
        assertEquals(intra / 3.0, function.calculateAverageIntraClusterDistance(), DELTA);
    }
}
