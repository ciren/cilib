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
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This measurement is handy when debugging a clustering in R^n space using GNUplot. Logging should be disabled and no
 * other output should be written to standard out. To try it, start GNUplot, execute:<br/>
 * load "&lt;./simulator.sh path/to/your/cilib.config.file.xml -noprogress"<br/>
 * and enjoy.
 *
 * @author Theuns Cloete
 */
public class PlotHighDimensionalClustersAndCentroids implements Measurement {
    private static final long serialVersionUID = 6736966446500272325L;

    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "Z";
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

        System.out.println("reset");
        System.out.println("set term jpeg medium");
        System.out.println("set style data lines");
        System.out.println("set key off");

        int iteration = algorithm.getIterations();

        this.plotCentroids(iteration, clusters);
        this.plotClustersWithCentroids(iteration,clusters);

        return Int.valueOf(0, new Bounds(0, 0));
    }

    private void plotCentroids(int iteration, ArrayList<Cluster<Vector>> clusters) {
        System.out.println("set output \"centroids.all.iteration." + String.format("%04d", iteration) + ".jpg\"");
        System.out.println("set title 'Iteration " + iteration + ": Centroids (" + clusters.size() + ")'");
        System.out.print("plot ");

        for (int i = 0, n = clusters.size(); i < n; ++i) {
            System.out.print("'-' title 'centroid " + i + "'");
            if (i < n - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        for (Cluster<Vector> cluster : clusters) {
            Vector centroid = cluster.getCentroid();

            for (int i = 0, n = centroid.size(); i < n; ++i) {
                System.out.println(String.format("%d\t%f", i, centroid.doubleValueOf(i)));
            }
            System.out.println("e");
        }
    }

    private void plotClustersWithCentroids(int iteration, ArrayList<Cluster<Vector>> clusters) {
        for (int i = 0, n = clusters.size(); i < n; ++i) {
            System.out.println("set output \"cluster." + String.format("%02d", i) + ".iteration."+ String.format("%04d", iteration) + ".jpg\"");

            plotClusterWithCentroid(iteration, i, clusters.get(i), clusters.get(i).getCentroid());
        }
    }

    private void plotClusterWithCentroid(int iteration, int id, Cluster<Vector> cluster, Vector centroid) {
        System.out.println("set title 'Iteration " + iteration + ": Cluster " + id + " (" + cluster.size() + " patterns)'");
        System.out.print("plot ");

        for (Pattern<Vector> pattern : cluster) {
            System.out.print("'-' title '" + pattern.getClassification() + "', ");
        }

        System.out.println("'-' title 'centroid' linetype 1 linewidth 5");

        for (Pattern<Vector> pattern : cluster) {
            Vector data = pattern.getData();

            for (int i = 0, n = data.size(); i < n; ++i) {
                System.out.println(String.format("%d\t%f", i, data.doubleValueOf(i)));
            }
            System.out.println("e");
        }

        for (int i = 0, n = centroid.size(); i < n; ++i) {
            System.out.println(String.format("%d\t%f", i, centroid.doubleValueOf(i)));
        }
        System.out.println("e");
    }
}
