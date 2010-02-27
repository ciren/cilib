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
import java.util.Collection;
import java.util.Hashtable;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.dataset.Pattern;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;

/**
 * This measurement is handy when debugging a clustering in R^n space using GNUplot. Logging should be disabled and no
 * other output should be written to standard out. To try it, start GNUplot, execute:<br/>
 * load "&lt./simulator.sh path/to/your/cilib.config.file.xml -noprogress"<br/>
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

    @Override
    public Type getValue(Algorithm algorithm) {
        ClusteringUtils helper = ClusteringUtils.get();
        Vector centroids = (Vector) algorithm.getBestSolution().getPosition();
        helper.arrangeClustersAndCentroids(centroids);

        System.out.println("reset");
        System.out.println("set term jpeg medium");
        System.out.println("set style data lines");
        System.out.println("set key off");
        

        ArrayList<Hashtable<Integer, Pattern>> arrangedClusters = helper.getArrangedClusters();
        ArrayList<Vector> arrangedCentroids = helper.getArrangedCentroids();
        int iteration = AbstractAlgorithm.get().getIterations();

        this.plotCentroids(iteration, arrangedCentroids);
        this.plotClustersWithCentroids(iteration, arrangedClusters, arrangedCentroids);

        return Int.valueOf(0, new Bounds(0, 0));
    }

    private void plotCentroids(int iteration, ArrayList<Vector> centroids) {
        System.out.println("set output \"centroids.all.iteration." + String.format("%04d", iteration) + ".jpg\"");
        System.out.println("set title 'Iteration " + iteration + ": Centroids (" + centroids.size() + ")'");
        System.out.print("plot ");

        for (int i = 0; i < centroids.size(); ++i) {
            System.out.print("'-' title 'centroid " + i + "'");
            if (i < centroids.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        for (Vector centroid : centroids) {
            for (int i = 0; i < centroid.size(); ++i) {
                System.out.println(String.format("%d\t%f", i, centroid.doubleValueOf(i)));
            }
            System.out.println("e");
        }
    }

    private void plotClustersWithCentroids(int iteration, ArrayList<Hashtable<Integer, Pattern>> clusters, ArrayList<Vector> centroids) {
        for (int i = 0; i < clusters.size(); ++i) {
            System.out.println("set output \"cluster." + String.format("%02d", i) + ".iteration."+ String.format("%04d", iteration) + ".jpg\"");

            plotClusterWithCentroid(iteration, i, clusters.get(i).values(), centroids.get(i));
        }
    }

    private void plotClusterWithCentroid(int iteration, int id, Collection<Pattern> cluster, Vector centroid) {
        System.out.println("set title 'Iteration " + iteration + ": Cluster " + id + " (" + cluster.size() + " patterns)'");
        System.out.print("plot ");

        for (Pattern pattern : cluster) {
            System.out.print("'-' title '" + pattern.clazz + "', ");
        }

        System.out.println("'-' title 'centroid' linetype 1 linewidth 5");

        for (Pattern pattern : cluster) {
            Vector vector = pattern.data;

            for (int i = 0; i < vector.size(); ++i) {
                System.out.println(String.format("%d\t%f", i, vector.doubleValueOf(i)));
            }
            System.out.println("e");
        }

        for (int i = 0; i < centroid.size(); ++i) {
            System.out.println(String.format("%d\t%f", i, centroid.doubleValueOf(i)));
        }
        System.out.println("e");
    }
}
