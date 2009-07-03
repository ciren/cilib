/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.functions.clustering;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClusteringFitnessFunctionTest {
    private static ClusteringFitnessFunction function = null;
    private static AssociatedPairDataSetBuilder dataSetBuilder = null;
    private static Vector centroids = null;
    private static ClusteringProblem problem = null;
    private static FunctionOptimisationProblem innerProblem = null;
    private static final double DELTA = 0.000000000001;

    @BeforeClass
    public static void intialise() {
        dataSetBuilder = new AssociatedPairDataSetBuilder();
        dataSetBuilder.addDataSet(new MockClusteringStringDataSet());
        function = new QuantisationErrorFunction();
        innerProblem = new FunctionMinimisationProblem();
        innerProblem.setFunction(function);
        problem = new ClusteringProblem();
        problem.setDomain("Z(0, 37),Z(0, 51)");
        problem.setInnerProblem(innerProblem);
        problem.setNumberOfClusters(7);
        problem.setDataSetBuilder(dataSetBuilder);

        centroids = new Vector();
        centroids.append(new Int(1));
        centroids.append(new Int(1));
        centroids.append(new Int(33));
        centroids.append(new Int(8));
        centroids.append(new Int(20));
        centroids.append(new Int(19));
        centroids.append(new Int(11));
        centroids.append(new Int(22));
        centroids.append(new Int(30));
        centroids.append(new Int(27));
        centroids.append(new Int(19));
        centroids.append(new Int(40));
        centroids.append(new Int(5));
        centroids.append(new Int(50));
    }


    @AfterClass
    public static void destroy() {
        function = null;
        dataSetBuilder = null;
        centroids = null;
    }

    @Test
    public void testEvaluate() {
        // the evaluate method arranges the clusters and centroids
        assertEquals(4.27188655918496, function.evaluate(centroids), DELTA);
    }

    @Test
    public void testQuantisationError() {
        assertEquals(4.27188655918496, function.calculateQuantisationError(), DELTA);
    }

    @Test
    public void testMaximumAverageDistance() {
        assertEquals(5.765984935187037, function.calculateMaximumAverageDistance(), DELTA);
    }

    @Test
    public void testMinimumInterClusterDistance() {
        assertEquals(Math.sqrt(90), function.calculateMinimumInterClusterDistance(), DELTA);
    }

    @Test
    public void testMaximumInterClusterDistance() {
        assertEquals(Math.sqrt(1845), function.calculateMaximumInterClusterDistance(), DELTA);
    }

    @Test
    public void testMinimumSetDistance() {
        assertEquals(Math.sqrt(388), function.calculateMinimumSetDistance(0, 1), DELTA);
        assertEquals(Math.sqrt(389), function.calculateMinimumSetDistance(0, 2), DELTA);
        assertEquals(Math.sqrt(405), function.calculateMinimumSetDistance(0, 3), DELTA);
        assertEquals(Math.sqrt(925), function.calculateMinimumSetDistance(0, 4), DELTA);
        assertEquals(Math.sqrt(1066), function.calculateMinimumSetDistance(0, 5), DELTA);
        assertEquals(1.0, function.calculateMinimumSetDistance(1, 2), DELTA);
        assertEquals(Math.sqrt(181), function.calculateMinimumSetDistance(1, 3), DELTA);
        assertEquals(Math.sqrt(169), function.calculateMinimumSetDistance(1, 4), DELTA);
        assertEquals(Math.sqrt(450), function.calculateMinimumSetDistance(1, 5), DELTA);
        assertEquals(1.0, function.calculateMinimumSetDistance(2, 3), DELTA);
        assertEquals(2.0, function.calculateMinimumSetDistance(2, 4), DELTA);
        assertEquals(Math.sqrt(29), function.calculateMinimumSetDistance(2, 5), DELTA);
        assertEquals(Math.sqrt(90), function.calculateMinimumSetDistance(3, 4), DELTA);
        assertEquals(Math.sqrt(85), function.calculateMinimumSetDistance(3, 5), DELTA);
        assertEquals(Math.sqrt(41), function.calculateMinimumSetDistance(4, 5), DELTA);
    }

    @Test
    public void testAverageSetDistance() {
        assertEquals(28.74157583325409, function.calculateAverageSetDistance(0, 1), DELTA);
        assertEquals(23.48964010748228, function.calculateAverageSetDistance(0, 2), DELTA);
        assertEquals(22.3251767476371, function.calculateAverageSetDistance(0, 3), DELTA);
        assertEquals(31.41056805514129, function.calculateAverageSetDistance(0, 4), DELTA);
        assertEquals(40.7867709331524, function.calculateAverageSetDistance(0, 5), DELTA);
        assertEquals(18.55559976582913, function.calculateAverageSetDistance(1, 2), DELTA);
        assertEquals(27.60024942795051, function.calculateAverageSetDistance(1, 3), DELTA);
        assertEquals(21.74671419268618, function.calculateAverageSetDistance(1, 4), DELTA);
        assertEquals(38.91030178983154, function.calculateAverageSetDistance(1, 5), DELTA);
        assertEquals(10.52656908164985, function.calculateAverageSetDistance(2, 3), DELTA);
        assertEquals(9.524130794948, function.calculateAverageSetDistance(2, 4), DELTA);
        assertEquals(22.78200247378393, function.calculateAverageSetDistance(2, 5), DELTA);
        assertEquals(13.81371117279074, function.calculateAverageSetDistance(3, 4), DELTA);
        assertEquals(18.65560457581544, function.calculateAverageSetDistance(3, 5), DELTA);
        assertEquals(17.464647454513713, function.calculateAverageSetDistance(4, 5), DELTA);
    }

    @Test
    public void testClusterDiameter() {
        assertEquals(0.0, function.calculateClusterDiameter(0), DELTA);
        assertEquals(Math.sqrt(233), function.calculateClusterDiameter(1), DELTA);
        assertEquals(Math.sqrt(226), function.calculateClusterDiameter(2), DELTA);
        assertEquals(Math.sqrt(34), function.calculateClusterDiameter(3), DELTA);
        assertEquals(Math.sqrt(32), function.calculateClusterDiameter(4), DELTA);
        assertEquals(Math.sqrt(153), function.calculateClusterDiameter(5), DELTA);
    }

    @Test
    public void testIntraClusterDistance() {
        assertEquals(427.037141232211, function.calculateIntraClusterDistance(), DELTA);
    }

    @Test
    public void testAverageIntraClusterDistance() {
        assertEquals(427.037141232211 / 93, function.calculateAverageIntraClusterDistance(), DELTA);
    }
}
