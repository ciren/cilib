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
package net.sourceforge.cilib.clustering.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.StructuredTypeFitnessCalculator;

/**
 * This algorithm is an implementation of the KMeans Clustering algorithm.
 * <p>
 * This is the implementation as described in Section 2 of:
 *
 * @inproceedings{ 2000.Ray.jul, title = "Determination of Number of Clusters in
 *                 K-Means Clustering and Application in Colour Image
 *                 Segmentation", author = "Siddheswar Ray and Rose H. Turi",
 *                 year = "2000", month = jul, booktitle = "Proceedings of the
 *                 Fourth International Conference on Advances in Pattern
 *                 Recognition and Digital Techniques", pages = "137--143",
 *                 address = "Calcutta, India"}
 *
 * This class makes use of a {@link CentroidsInitialisationStrategy} to initialise the
 * centroids in the desired manner. The default centroid initialisation strategy is the
 * {@link RandomCentroidsInitialisationStrategy}.
 *
 * @author Theuns Cloete
 * @TODO: Check that removing the FitnessCalculator does not break the functionality of this class.
 */
public class KMeans extends AbstractAlgorithm implements SingularAlgorithm {
    private static final long serialVersionUID = -3301123926538450441L;

    private CentroidsInitialisationStrategy centroidsInitialisationStrategy = null;
    private Vector centroids = null;
    private FitnessCalculator<Vector> calculator;

    /**
     * Create an instance of {@linkplain KMeans}.
     */
    public KMeans() {
        calculator = new StructuredTypeFitnessCalculator<Vector>();
        centroidsInitialisationStrategy = new RandomCentroidsInitialisationStrategy();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public KMeans(KMeans copy) {
        super(copy);
        centroids = copy.centroids.getClone();
        calculator = copy.calculator.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KMeans getClone() {
        return new KMeans(this);
    }

    /**
     * This initialisation of the centroids is handled by the chosen
     * {@link CentroidsInitialisationStrategy}.
     */
    @Override
    public void performInitialisation() {
        ClusteringUtils helper = ClusteringUtils.get();

        centroids = centroidsInitialisationStrategy.initialise(helper.getClusteringProblem(), helper.getClusterableDataSet());
    }

    /**
     * Performs a single iteration of the KMeans algorithm.
     */
    @Override
    public void algorithmIteration() {
        calculator.getFitness(centroids);

        // the fitness calculation step already arranged the clusters and centroids for us
        ClusteringUtils helper = ClusteringUtils.get();
        ArrayList<Hashtable<Integer, Pattern>> clusters = helper.getOriginalClusters();

        for (int i = 0; i < clusters.size(); i++) {
            Hashtable<Integer, Pattern> cluster = clusters.get(i);
            // get the i'th centroid; only used to determine the dimension of a single centroid
            Vector centroid = helper.getOriginalCentroids().get(i);

            // TODO: I don't know if this is part of the original KMeans algorithm
            if (cluster.isEmpty()) {    // reinitialise the centroid if no patterns "belong" to it
                centroid = reinitialiseCentroid(centroid);
            }
            else {                        // the centroid becomes the mean of cluster i
                centroid = Stats.meanVector(cluster.values());
            }
            updateCentroid(centroid, i);
        }
    }

    /**
     * Update the correct centroid inside the {@link #centroids} {@link Vector} with the
     * contents of the new provided position.
     * @param newPosition a {@link Vector} representing the new position of the centroid
     * @param cluster the cluster number that is used to determine the correct position in
     *        the {@link #centroids} {@link Vector}
     */
    private void updateCentroid(Vector newPosition, int cluster) {
        int dimension = newPosition.getDimension();

        for (int i = 0; i < dimension; i++) {
            centroids.set(cluster * dimension + i, newPosition.get(i));
        }
    }

    /**
     * Reinitialise the centroid based on the chosen {@link CentroidsInitialisationStrategy}.
     * The {@link CentroidsInitialisationStrategy} returns an entire centroids {@link Vector},
     * but we only need a single centroid. The given parameter is only used to determine the
     * size of a single centroid.
     * @param centroid the centroid that should be reinitialised
     * @return a {@link Vector} representing a new (reinitialised) centroid
     */
    private Vector reinitialiseCentroid(Vector centroid) {
        ClusteringUtils helper = ClusteringUtils.get();
        Vector tmp = centroidsInitialisationStrategy.initialise(helper.getClusteringProblem(), helper.getClusterableDataSet());

        // this first centroid will do
        return tmp.subList(0, centroid.getDimension() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        return new OptimisationSolution(centroids.getClone(), getOptimisationProblem().getFitness(centroids));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
    }

    /**
     * Get the centroid initialisation strategy.
     * @return The current {@linkplain CentroidsInitialisationStrategy}.
     */
    public CentroidsInitialisationStrategy getCentroidsInitialisationStrategy() {
        return centroidsInitialisationStrategy;
    }

    /**
     * Set the current {@linkplain CentroidsInitialisationStrategy}.
     * @param centroidsInitialisationStrategy The value to set.
     */
    public void setCentroidsInitialisationStrategy(CentroidsInitialisationStrategy centroidsInitialisationStrategy) {
        this.centroidsInitialisationStrategy = centroidsInitialisationStrategy;
    }
}
