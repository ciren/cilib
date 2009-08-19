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

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Hashtable;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A class that simplifies clustering when making use of a {@link ClusteringProblem}, a
 * {@link ClusterableDataSet} and a {@link ClusteringFitnessFunction}. <br/> This
 * class is not dependent on a {@link ClusteringFitnessFunction}, but the
 * {@link ClusteringFitnessFunction}s use this class extensively.
 *
 */
public final class ClusteringUtils {
    private static final long serialVersionUID = 4878437477807660149L;

    /**
     * A thread local instance of this class.
     */
    private static transient ThreadLocal<ClusteringUtils> instance = new ThreadLocal<ClusteringUtils>() {
        protected ClusteringUtils initialValue() {
            return new ClusteringUtils();
        }
    };

    private ClusteringProblem clusteringProblem = null;
    private ClusterableDataSet clusterableDataSet = null;
    // ArrayList of Vectors
    private ArrayList<Vector> originalCentroids = null;
    private ArrayList<Vector> arrangedCentroids = null;
    // ArrayList of Hashtables, mapping indices to patterns
    // index i refers to the i'th Pattern in the DataSet
    private ArrayList<Hashtable<Integer, Pattern>> originalClusters = null;
    private ArrayList<Hashtable<Integer, Pattern>> arrangedClusters = null;

    /**
     * Private because this is a Singleton class. When starting an {@link Algorithm}, the
     * {@link ClusteringFitnessFunction} (that is used to optimise the
     * {@link ClusteringProblem}) does not know about the problem it was assigned to and
     * therefore it does not know about the dataset that contains the patterns for which a
     * clustering should be found. This method finds the current executing {@link Algorithm}
     * via the <code>Algorithm.get()</code> construct. Then it configures the
     * {@link ClusteringProblem} and then it configures the {@link DataSetBuilder}. This
     * method will be called only once for every new {@link Thread}, in other words, each
     * {@link ClusteringUtils} object is associated with a single {@link Thread}.
     */
    private ClusteringUtils() {
        if (clusteringProblem == null || clusterableDataSet == null) {
            try {
                Algorithm algorithm = AbstractAlgorithm.get();
                clusteringProblem = (ClusteringProblem) algorithm.getOptimisationProblem();
                clusterableDataSet = (ClusterableDataSet) clusteringProblem.getDataSetBuilder();

                System.out.println("Initialised Algorithm found: " + ClusteringUtils.class.getSimpleName() + " is now configured");
            }
            catch (EmptyStackException ese) {
                System.out.println("Preliminary: Algorithm not initialised yet");
                // there is no active algorithm when running the unit test
                // we need to return, otherwise it will break
                return;
            }
        }
    }

    /**
     * Return the current instance of this class.
     *
     * @return the current {@link #instance} of this class.
     */
    public static ClusteringUtils get() {
        return instance.get();
    }

    /**
     * This class only deals with {@link ClusteringProblem}s. Other problems are not
     * allowed.
     *
     * @param cp the {@link ClusteringProblem} used throughout the current clustering
     */
    public void setClusteringProblem(ClusteringProblem cp) {
        clusteringProblem = cp;
    }

    /**
     * Get the {@link ClusteringProblem} used throughout the current clustering.
     *
     * @return the {@link #clusteringProblem}
     */
    public ClusteringProblem getClusteringProblem() {
        return clusteringProblem;
    }

    /**
     * This class only deals with {@link ClusterableDataSet}s. Other datasets/dataset
     * builders are not allowed.
     *
     * @param cds the {@link ClusterableDataSet} used throughout the current clustering
     */
    public void setClusterableDataSet(ClusterableDataSet cds) {
        clusterableDataSet = cds;
    }

    /**
     * Get the {@link ClusterableDataSet} used throughout the current clustering.
     *
     * @return the {@link #clusterableDataSet}
     */
    public ClusterableDataSet getClusterableDataSet() {
        return clusterableDataSet;
    }

    /**
     * A central point where distances can be calculated. This is to prevent various
     * different distance measures from being used in the same clustering algorithm/problem.
     *
     * @param lhs the one {@link Vector}
     * @param rhs the other {@link Vector}
     * @return the distance between the two given vectors calculated using the current
     *         {@link #distanceMeasure}
     */
    public double calculateDistance(Vector lhs, Vector rhs) {
        return clusteringProblem.getDistanceMeasure().distance(lhs, rhs);
    }

    /**
     * A central point where the cached distance between the two given patterns can be
     * retrieved.
     *
     * @param x index of the one pattern
     * @param y index of the other pattern
     * @return the cached distance between the two given patterns
     */
    public double calculateDistance(int x, int y) {
        return clusterableDataSet.getCachedDistance(x, y);
    }

    /**
     * The three methods called in this method must be called in that specific order, i.e.
     * <ol>
     * <li>Arrange the centroids (split them up to be manageable)</li>
     * <li>Arrange the clusters (assign patterns to their closest centroids) (depends on
     * Step 1)</li>
     * <li>Remove the empty clusters and their associated centroids from the <i>arranged
     * lists</i>, thereby finalizing the arranging of clusters (depends on both Steps 1 & 2)</li>
     * </ol>
     *
     * @param centroids the @{@linkplain Vector} that represents the centroids
     */
    public void arrangeClustersAndCentroids(Vector centroids) {
        arrangeCentroids(centroids);
        arrangeClusters();
        removeEmptyClustersAndCentroids();
    }

    /**
     * Take the given centroids {@linkplain Vector}, split it up and construct an
     * {@linkplain ArrayList} of {@linkplain Vector}s so that each element in this list
     * consist of a single centroid.
     *
     * @param centroids the centriods {@linkplain Vector} that should be split up and arranged
     */
    private void arrangeCentroids(Vector centroids) {
        int numberOfClusters = clusteringProblem.getNumberOfClusters();
        originalCentroids = new ArrayList<Vector>(numberOfClusters);
        int dimension = centroids.size() / numberOfClusters;

        for (int i = 0; i < numberOfClusters; i++) {
            Vector list = centroids.subList(i * dimension, (i * dimension) + dimension - 1);
            originalCentroids.add(list);
        }
    }

    /**
     * Assign all patterns to their closest centroid, building up an {@link ArrayList} that
     * contains {@link Hashtable}s that link a {@link Pattern} to its index in the DataSet.
     * When this method is done, all patterns will <i>belong</i> to it's closest centroid.
     */
    private void arrangeClusters() {
        int numberOfClusters = clusteringProblem.getNumberOfClusters();
        ArrayList<Pattern> patterns = clusterableDataSet.getPatterns();
        originalClusters = new ArrayList<Hashtable<Integer, Pattern>>(numberOfClusters);

        for (int i = 0; i < numberOfClusters; i++) {
            originalClusters.add(new Hashtable<Integer, Pattern>(patterns.size() / numberOfClusters));
        }

        for (int i = 0; i < patterns.size(); i++) {
            Pattern pattern = patterns.get(i);
            double minimum = Double.MAX_VALUE;
            for (int j = 0; j < numberOfClusters; j++) {
                double distance = calculateDistance(pattern.data, originalCentroids.get(j));

                if (distance < minimum) {
                    minimum = distance;
                    for (Hashtable<Integer, Pattern> all : originalClusters) {
                        all.remove(i);
                    }
                    originalClusters.get(j).put(i, pattern);
                }
            }
        }

        //    for (Hashtable<Integer, Pattern> cluster : originalClusters) {
        //        logger.debug("begin cluster: " + cluster.size());
        //        Enumeration<Integer> e = cluster.keys();
        //        while(e.hasMoreElements()) {
        //            Integer i = e.nextElement();
        //            logger.debug(i + " -> " + cluster.get(i).data);
        //        }
        //        logger.debug("done");
        //    }
    }

    /**
     * Empty clusters are caused due to centroids that are not associated with any of the
     * patterns in the dataset. Empty clusters should not be included in the calculation of
     * fitness functions or validity indices. This method constructs the
     * {@link #arrangedClusters} list which is basically the same as the
     * {@link #originalClusters} list, with the only difference that the empty clusters are
     * removed. It also constructs the {@link #arrangedCentroids} list which is basically the
     * same as the {@link #originalCentroids} list, with the only difference that the
     * centroids that do not have any patterns associated with them are removed.
     */
    private void removeEmptyClustersAndCentroids() {
        arrangedCentroids = new ArrayList<Vector>(originalCentroids.size());
        arrangedCentroids.addAll(originalCentroids);
        arrangedClusters = new ArrayList<Hashtable<Integer, Pattern>>(originalClusters.size());
        arrangedClusters.addAll(originalClusters);

        // traverse list of clusters in reverse due to the way in which the remove(i) method works
        // i.e to prevent skipping elements, because all elements after i are shift left after a remove
        for (int i = arrangedClusters.size() - 1; i >= 0; i--) {
            if (arrangedClusters.get(i).isEmpty()) {
                arrangedClusters.remove(i);
                arrangedCentroids.remove(i);
            }
        }
        arrangedClusters.trimToSize();
        arrangedCentroids.trimToSize();
    }

    /**
     * Get the patterns in the {@link #clusterableDataSet}.
     *
     * @return the patterns in the {@link #clusterableDataSet}
     */
    public ArrayList<Pattern> getPatternsInDataSet() {
        return clusterableDataSet.getPatterns();
    }

    /**
     * Get the number of patterns in the {@link #clusterableDataSet}.
     *
     * @return the number of patterns in the {@link #clusterableDataSet}.
     */
    public int getNumberOfPatternsInDataSet() {
        return clusterableDataSet.getNumberOfPatterns();
    }

    /**
     * Get the structure that represents the split-up centroids <em>before</em> the
     * non-associated centroids were removed.
     *
     * @return an {@link ArrayList} of {@link Vector}s that may contain centroids that are
     *         not associated with any patterns
     */
    public ArrayList<Vector> getOriginalCentroids() {
        return originalCentroids;
    }

    /**
     * Get the structure that represents the split-up centroids <em>after</em> the
     * non-associated centroids have been removed.
     *
     * @return an {@link ArrayList} of {@link Vector}s that does NOT contain centroids that
     *         are NOT associated with any patterns
     */
    public ArrayList<Vector> getArrangedCentroids() {
        return arrangedCentroids;
    }

    /**
     * Get the structure that represents the seperate clusters <em>before</em> the empty
     * clusters were removed.
     *
     * @return an {@link ArrayList} of {@link Hashtable}s that may contain empty clusters
     */
    public ArrayList<Hashtable<Integer, Pattern>> getOriginalClusters() {
        return originalClusters;
    }

    /**
     * Get the structure that represents the seperate clusters <em>after</em> the empty
     * clusters have been removed.
     *
     * @return an {@link ArrayList} of {@link Hashtable}s that does NOT contain empty
     *         clusters
     */
    public ArrayList<Hashtable<Integer, Pattern>> getArrangedClusters() {
        return arrangedClusters;
    }

    /**
     * Get the mean {@link Vector} that has been cached by the {@link #clusterableDataSet}.
     *
     * @return a {@link Vector} that represents the mean of all the patterns inside the
     *         {@link #clusterableDataSet}
     */
    public Vector getDataSetMean() {
        return clusterableDataSet.getMean();
    }

    /**
     * Get the variance (scalar) thas been cached by the {@link #clusterableDataSet}.
     *
     * @return a double that represents the variance of all the patterns inside the
     *         {@link #clusterableDataSet}
     */
    public double getDataSetVariance() {
        return clusterableDataSet.getVariance();
    }
}
