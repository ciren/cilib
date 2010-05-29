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
package net.sourceforge.cilib.measurement.single.clustering;

import java.util.ArrayList;

import java.util.Set;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.clustering.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Combines and measures the variance vectors of the clusters optimised by the given algorithm.
 *
 * @author theuns.cloete
 */
public class ClusterVariances implements Measurement {
    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "(R^?)^?";
    }

    /**
     * TODO: When we start using Guice, this method should be refactored
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        ClusteringProblem problem = (ClusteringProblem) algorithm.getOptimisationProblem();
        int numberOfClusters = problem.getNumberOfClusters();
        ArrayList<Vector> centroids = ClusteringFunctions.disassembleCentroids((Vector) algorithm.getBestSolution().getPosition(), numberOfClusters);
        StaticDataSetBuilder dataSetBuilder = (StaticDataSetBuilder) problem.getDataSetBuilder();
        Set<Pattern<Vector>> patterns = dataSetBuilder.getPatterns();
        DistanceMeasure distanceMeasure = problem.getDistanceMeasure();
        ArrayList<Cluster<Vector>> clusters = ClusteringFunctions.cluster(centroids, patterns, distanceMeasure, numberOfClusters);
        Vector.Builder combined = Vector.newBuilder();

        for (Cluster<Vector> cluster : clusters) {
            combined.copyOf(cluster.getVarianceVector(cluster.getCentroid()));
        }
        return combined.build();
    }
}
