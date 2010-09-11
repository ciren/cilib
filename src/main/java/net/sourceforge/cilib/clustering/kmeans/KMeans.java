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
package net.sourceforge.cilib.clustering.kmeans;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.clustering.ClusteringProblem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.StructuredTypeFitnessCalculator;

/**
 * This algorithm is an implementation of the KMeans Clustering algorithm. This is the implementation as described in
 * Section 2 of:
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
 * centroids in the desired manner. The default centroids initialisation strategy is the
 * {@link RandomCentroidsInitialisationStrategy}. It also makes use of a {@link CentroidsDiversificationStrategy} to add
 * diversity at certain moments defined by the strategy. The default centroids diversification strategy is the
 * {@link NullCentroidsDiversificationStrategy} which does not add any diversity, i.e. adhering to the normal KMeans
 * algorithm.
 *
 * @author Theuns Cloete
 */
public class KMeans extends AbstractAlgorithm implements SingularAlgorithm {
    private static final long serialVersionUID = -3301123926538450441L;

    private CentroidsInitialisationStrategy centroidsInitialisationStrategy;
    private CentroidsDiversificationStrategy centroidsDiversificationStrategy;
    private FitnessCalculator<Vector> calculator;
    private List<Vector> centroids;

    /**
     * Create an instance of {@linkplain KMeans}.
     */
    public KMeans() {
        this.centroidsInitialisationStrategy = new RandomCentroidsInitialisationStrategy();
        this.centroidsDiversificationStrategy = new NullCentroidsDiversificationStrategy();
        this.calculator = new StructuredTypeFitnessCalculator<Vector>();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public KMeans(KMeans rhs) {
        super(rhs);
        this.centroidsInitialisationStrategy = rhs.centroidsInitialisationStrategy.getClone();
        this.centroidsDiversificationStrategy = rhs.centroidsDiversificationStrategy.getClone();
        this.calculator = rhs.calculator.getClone();
        this.centroids = Lists.newArrayList();

        for (Vector centroid : rhs.centroids) {
            this.centroids.add(Vector.copyOf(centroid));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KMeans getClone() {
        return new KMeans(this);
    }

    /**
     * The initialisation of the centroids is handled by the chosen {@link CentroidsInitialisationStrategy}.
     * TODO: When we start using Guice, this method should be refactored
     */
    @Override
    public void performInitialisation() {
        ClusteringProblem problem = (ClusteringProblem) this.getOptimisationProblem();
        DataTable<StandardPattern, TypeList> dataTable = problem.getDataTable();
        DomainRegistry standardDomain = problem.getDomainRegistry();
        DistanceMeasure distanceMeasure = problem.getDistanceMeasure();
        int numberOfClusters = problem.getNumberOfClusters();

        this.centroids = this.centroidsInitialisationStrategy.initialise(dataTable, standardDomain, distanceMeasure, numberOfClusters);
        this.centroidsDiversificationStrategy.initialise(this.centroids);
    }

    /**
     * Performs a single iteration of the KMeans algorithm.
     * TODO: When we start using Guice, this method should be refactored
     */
    @Override
    public void algorithmIteration() {
        calculator.getFitness(ClusteringFunctions.assembleCentroids(this.centroids));

        ClusteringProblem problem = (ClusteringProblem) this.getOptimisationProblem();
        DataTable<StandardPattern, TypeList> dataTable = problem.getDataTable();
        DomainRegistry standardDomain = problem.getDomainRegistry();
        DistanceMeasure distanceMeasure = problem.getDistanceMeasure();
        int numberOfClusters = problem.getNumberOfClusters();
        List<Cluster> clusters = ClusteringFunctions.cluster(centroids, dataTable, distanceMeasure, numberOfClusters);

        for (int i = 0; i < clusters.size(); ++i) {
            Cluster cluster = clusters.get(i);
            Vector centroid = null;

            // TODO: I don't know if this if-else is part of the original KMeans algorithm
            if (cluster.isEmpty()) {
                // reinitialise the centroid if no patterns "belong" to it
                centroid = this.centroidsInitialisationStrategy.reinitialise(centroids, dataTable, standardDomain, distanceMeasure, i); // might return unbounded Vector
            }
            else {
                // the centroid becomes the mean of the cluster
                centroid = cluster.getMean();   // returns unbounded Vector
            }

            // we need to set the bounds of the centroid, because some centroids might be unbounded Vectors
            Vector builtRepresentation = ClusteringFunctions.disassembleCentroids((Vector) problem.getDomain().getBuiltRepresenation(), problem.getNumberOfClusters()).get(0);

            // TODO: This should not be necessary if we load the data set correctly from file
//            centroid = Vectors.setBounds(centroid, Vectors.lowerBoundVector(builtRepresentation), Vectors.upperBoundVector(builtRepresentation));
            this.centroids.set(i, centroid);
            cluster.setCentroid(centroid);
            this.centroidsDiversificationStrategy.diversify(centroids, i);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Vector assembled = ClusteringFunctions.assembleCentroids(centroids);

        return new OptimisationSolution(assembled, this.getOptimisationProblem().getFitness(assembled));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
    }

    /**
     * Get the current centroids initialisation strategy.
     * @return The current {@linkplain CentroidsInitialisationStrategy}.
     */
    public CentroidsInitialisationStrategy getCentroidsInitialisationStrategy() {
        return this.centroidsInitialisationStrategy;
    }

    /**
     * Set the current centroids initialisation strategy.
     * @param cis The {@linkplain CentroidsInitialisationStrategy} to use.
     */
    public void setCentroidsInitialisationStrategy(CentroidsInitialisationStrategy cis) {
        this.centroidsInitialisationStrategy = cis;
    }

    /**
     * Get the current centroids diversification strategy.
     * @return The current {@linkplain CentroidsInitialisationStrategy}.
     */
    public CentroidsDiversificationStrategy getCentroidsDiversificationStrategy() {
        return this.centroidsDiversificationStrategy;
    }

    /**
     * Set the current centroids diversification strategy.
     * @param cds The {@link CentroidsDiversificationStrategy} to use.
     */
    public void setCentroidsDiversificationStrategy(CentroidsDiversificationStrategy cds) {
        this.centroidsDiversificationStrategy = cds;
    }
}
