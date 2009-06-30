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
package net.sourceforge.cilib.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.IsCollectionContaining.hasItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClusteringUtilsTest {

    private static ClusteringUtils helper = ClusteringUtils.get();
    private static Vector centroids = null;
    private static ArrayList<Hashtable<Integer, Pattern>> arrangedClusters = null;
    private static ArrayList<Vector> arrangedCentroids = null;
    private static AssociatedPairDataSetBuilder dataSetBuilder = null;
    private static ClusteringProblem problem = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dataSetBuilder = new AssociatedPairDataSetBuilder();
        dataSetBuilder.addDataSet(new MockClusteringStringDataSet());
        problem = new ClusteringProblem();
        problem.setDomain("Z(0, 37),Z(0, 51)");
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
        helper.arrangeClustersAndCentroids(centroids);
        arrangedClusters = helper.getArrangedClusters();
        arrangedCentroids = helper.getArrangedCentroids();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testArrangeClustersAndCentroids() {
        assertThat(arrangedCentroids.size(), equalTo(6));
        assertThat(arrangedClusters.size(), equalTo(6));
        assertThat(arrangedCentroids.size(), equalTo(arrangedClusters.size()));

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

        for (Hashtable<Integer, Pattern> cluster : arrangedClusters) {
            for (Pattern pattern : cluster.values()) {
                assertThat(pattern.data.size(), equalTo(2));
            }
        }

        for (Vector centroid : arrangedCentroids) {
            assertThat(centroid.size(), equalTo(2));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPatternAssignments() {
        int counter = 0;
        // cluster 0
        Collection<Integer> indices = arrangedClusters.get(0).keySet();
        Collection<Pattern> patterns = arrangedClusters.get(0).values();

        while (counter <= 0) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // cluster 1
        indices = arrangedClusters.get(1).keySet();
        patterns = arrangedClusters.get(1).values();
        while (counter <= 26) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // cluster 2
        indices = arrangedClusters.get(2).keySet();
        patterns = arrangedClusters.get(2).values();
        while (counter <= 40) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // cluster 3
        indices = arrangedClusters.get(3).keySet();
        patterns = arrangedClusters.get(3).values();
        // this pattern can belong to either clusters 2 or 3, depending on the order of the centroids
        while (counter <= 41) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }
        // more cluster 2
        indices = arrangedClusters.get(2).keySet();
        patterns = arrangedClusters.get(2).values();
        while (counter <= 48) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 3
        indices = arrangedClusters.get(3).keySet();
        patterns = arrangedClusters.get(3).values();
        while (counter <= 51) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 2
        indices = arrangedClusters.get(2).keySet();
        patterns = arrangedClusters.get(2).values();
        while (counter <= 52) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 3
        indices = arrangedClusters.get(3).keySet();
        patterns = arrangedClusters.get(3).values();
        while (counter <= 56) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 2
        indices = arrangedClusters.get(2).keySet();
        patterns = arrangedClusters.get(2).values();
        while (counter <= 60) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // cluster 4
        indices = arrangedClusters.get(4).keySet();
        patterns = arrangedClusters.get(4).values();
        while (counter <= 61) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 3
        indices = arrangedClusters.get(3).keySet();
        patterns = arrangedClusters.get(3).values();
        while (counter <= 69) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 2
        indices = arrangedClusters.get(2).keySet();
        patterns = arrangedClusters.get(2).values();
        while (counter <= 71) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 4
        indices = arrangedClusters.get(4).keySet();
        patterns = arrangedClusters.get(4).values();
        while (counter <= 73) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 3
        indices = arrangedClusters.get(3).keySet();
        patterns = arrangedClusters.get(3).values();
        while (counter <= 75) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 2
        indices = arrangedClusters.get(2).keySet();
        patterns = arrangedClusters.get(2).values();
        while (counter <= 76) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // more cluster 4
        indices = arrangedClusters.get(4).keySet();
        patterns = arrangedClusters.get(4).values();
        while (counter <= 79) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // cluster 5
        indices = arrangedClusters.get(5).keySet();
        patterns = arrangedClusters.get(5).values();
        while (counter <= 92) {
            assertThat(indices, hasItem(counter));
            assertThat(patterns, hasItem(dataSetBuilder.getPattern(counter++)));
        }

        // the last cluster is empty
        // should throw an exception (no such element / index out of bounds)
        assertThat(arrangedClusters.get(6).get(0), nullValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrangedCentroids() {
        assertThat(arrangedCentroids.get(0), equalTo(centroids.subList(0, 1)));

        assertThat(arrangedCentroids.get(1), equalTo(centroids.subList(2, 3)));

        assertThat(arrangedCentroids.get(2), equalTo(centroids.subList(4, 5)));

        assertThat(arrangedCentroids.get(3), equalTo(centroids.subList(6, 7)));

        assertThat(arrangedCentroids.get(4), equalTo(centroids.subList(8, 9)));

        assertThat(arrangedCentroids.get(5), equalTo(centroids.subList(10, 11)));

        // the last centroid is useless, i.e. no pattern "belongs" to it
        // should throw an exception (no such element / index out of bounds)
        assertThat(arrangedCentroids.get(6), nullValue());
    }
}
