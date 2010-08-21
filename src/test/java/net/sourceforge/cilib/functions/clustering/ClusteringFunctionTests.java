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

import com.google.common.collect.Lists;

import java.util.ArrayList;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * @author Theuns Cloete
 */
public final class ClusteringFunctionTests {
    public static final double EPSILON = 0.0000000000001;

    private ClusteringFunctionTests() {
    }

    public static Vector getCombinedCentroids() {
        return Vector.of(0, 0, 10, 0, 10, 10, 0, 10);
    }

    public static ArrayList<Vector> getSeparateCentroids() {
        ArrayList<Vector> centroids = Lists.newArrayList();

        centroids.add(Vector.of(0, 0));
        centroids.add(Vector.of(10, 0));
        centroids.add(Vector.of(10, 10));
        centroids.add(Vector.of(0, 10));

        return centroids;
    }

    public static ArrayList<Cluster> getClusters() {
        ArrayList<Cluster> clusters = Lists.newArrayList();
        Cluster cluster = new Cluster(Vector.of(0, 0));

        cluster.add(new StandardPattern(Vector.of(1, 1), new StringType("1")));
        clusters.add(cluster);

        cluster = new Cluster(Vector.of(10, 0));
        cluster.add(new StandardPattern(Vector.of(9, 0), new StringType("2")));
        cluster.add(new StandardPattern(Vector.of(10, 1), new StringType("2")));
        clusters.add(cluster);

        cluster = new Cluster(Vector.of(10, 10));
        cluster.add(new StandardPattern(Vector.of(9, 9), new StringType("3")));
        cluster.add(new StandardPattern(Vector.of(10, 9), new StringType("3")));
        cluster.add(new StandardPattern(Vector.of(9, 10), new StringType("3")));
        clusters.add(cluster);

        cluster = new Cluster(Vector.of(0, 10));
        cluster.add(new StandardPattern(Vector.of(2, 8), new StringType("4")));
        cluster.add(new StandardPattern(Vector.of(0, 9), new StringType("4")));
        cluster.add(new StandardPattern(Vector.of(1, 9), new StringType("4")));
        cluster.add(new StandardPattern(Vector.of(1, 10), new StringType("4")));
        clusters.add(cluster);

        return clusters;
    }

    public static DataTable<StandardPattern, TypeList> getDataTable() {
        DataTable<StandardPattern, TypeList> dataTable = new StandardPatternDataTable();

        for (Cluster cluster : ClusteringFunctionTests.getClusters()) {
            for (StandardPattern pattern : cluster) {
                dataTable.addRow(pattern);
            }
        }
        return dataTable;
    }

    public static DistanceMeasure getDistanceMeasure() {
        return new EuclideanDistanceMeasure();
    }

    public static Vector getDataSetMean() {
        return Vector.of(5.2, 6.6);
    }

    public static double getDataSetVariance() {
        return 23.6784269747802;
    }
}
