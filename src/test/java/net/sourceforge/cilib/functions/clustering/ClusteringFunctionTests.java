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
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Set;

import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
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

    public static ArrayList<Cluster<Vector>> getClusters() {
        ArrayList<Cluster<Vector>> clusters = Lists.newArrayList();
        Cluster<Vector> cluster = new Cluster<Vector>(Vector.of(0, 0));

        cluster.add(new Pattern<Vector>(Vector.of(1, 1), "1"));
        clusters.add(cluster);

        cluster = new Cluster<Vector>(Vector.of(10, 0));
        cluster.add(new Pattern<Vector>(Vector.of(9, 0), "2"));
        cluster.add(new Pattern<Vector>(Vector.of(10, 1), "2"));
        clusters.add(cluster);

        cluster = new Cluster<Vector>(Vector.of(10, 10));
        cluster.add(new Pattern<Vector>(Vector.of(9, 9), "3"));
        cluster.add(new Pattern<Vector>(Vector.of(10, 9), "3"));
        cluster.add(new Pattern<Vector>(Vector.of(9, 10), "3"));
        clusters.add(cluster);

        cluster = new Cluster<Vector>(Vector.of(0, 10));
        cluster.add(new Pattern<Vector>(Vector.of(2, 8), "4"));
        cluster.add(new Pattern<Vector>(Vector.of(0, 9), "4"));
        cluster.add(new Pattern<Vector>(Vector.of(1, 9), "4"));
        cluster.add(new Pattern<Vector>(Vector.of(1, 10), "4"));
        clusters.add(cluster);

        return clusters;
    }

    public static Set<Pattern<Vector>> getPatterns() {
        Set<Pattern<Vector>> patterns = Sets.newHashSet();

        for (Cluster<Vector> cluster : ClusteringFunctionTests.getClusters()) {
            patterns.addAll(cluster);
        }
        return patterns;
    }

    public static DistanceMeasure getDistanceMeasure() {
        return new EuclideanDistanceMeasure();
    }

    public static Vector getDataSetMean() {
        return Vector.of(5.2, 6.6);
    }

    public static double getDataSetVariance() {
//        System.out.println(Stats.variance(getPatterns(), getDataSetMean()));
//        System.out.println(23.6784269747802);

        return 23.6784269747802;
    }
}
