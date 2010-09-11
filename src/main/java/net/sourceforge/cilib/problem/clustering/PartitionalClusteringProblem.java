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
package net.sourceforge.cilib.problem.clustering;

import java.util.List;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.fitnessfactory.FitnessFactory;
import net.sourceforge.cilib.problem.fitnessfactory.MinimisationFitnessFactory;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * This class represents a Partitional Clustering Problem that is capable of clustering the
 * {@link StandrdPattern patterns} in a {@link DataTable data set}. Partitional clustering is the process of
 * {@link OptimisationProblemAdapter optimising} the centroids of the clustering. This optimisation process is driven by
 * an error or clustering function. This class therefore has a {@link ClusteringFunction} whose output should either be
 * minimised or maximised by making use of the appropriate {@link FitnessFactory}.
 *
 * The centroids of a clustering are <em>concatenated</em> to represent a single
 * {@link net.sourceforge.cilib.entity.Entity} such as a {@link net.sourceforge.cilib.entity.Particle} or an
 * {@link net.sourceforge.cilib.ec.Individual}. The domain of the data set therefore needs to be repeated a number of
 * times equal to the number of desired clusters. The entities that represent the centroids should be initialised using
 * this repeated domain. The domain of the data set and the number of clusters therefore create a dependency between
 * each other. Whenever the domain or the number of clusters are (re)configured, the repeated domain is regenerated.
 *
 * This class also provides a <em>central point</em> for specifying the {@link #distanceMeasure distance measure} and
 * the {@link #clusterCenterStrategy cluster center strategy} that should be used when calculating distances throughout
 * the entire clustering process.
 *
 * The following lists the methods that should be called (usually from XML) to correctly configure the clustering
 * problem (in this order):
 * <ol>
 * <li>{@link #setDomain(String)}, e.g. <code>domain="R(0.0,1.0)^5"</code></li>
 * <li>{@link #setNumberOfClusters(numberOfClusters)}, e.g. <code>numberOfClusters="4"</code></li>
 * <li>{@link #setClusteringFunction(net.sourceforge.cilib.functions.clustering.ClusteringFunction)}</li>
 * <li>{@link #setDataTableBuilder(net.sourceforge.cilib.io.DataTableBuilder)}</li>
 * <li>{@link #setDistanceMeasure(net.sourceforge.cilib.util.DistanceMeasure)}; default is
 * {@link EuclideanDistanceMeasure}</li>
 * <li>{@link #setClusterCenterStrategy(net.sourceforge.cilib.funtions.clustering.clustercenterstrategies.ClusterCenterStrategy};
 * default is {@link ClusterCentroidStrategy}</li>
 * </ol>
 *
 * @see #regenerateDomain()
 * @author Theuns Cloete
 */
public class PartitionalClusteringProblem extends OptimisationProblemAdapter implements ClusteringProblem {
    private static final long serialVersionUID = 7027242527499147957L;
    private static final int UNINITIALISED = -1;

    private ClusteringFunction<Double> clusteringFunction;
    private DataTableBuilder dataTableBuilder;
    private DataTable<StandardPattern, TypeList> dataTable;
    private DistanceMeasure distanceMeasure;
    private ClusterCenterStrategy clusterCenterStrategy;
    private DomainRegistry standardDomain;
    private DomainRegistry repeatedDomain;
    private FitnessFactory<Double> fitnessFactory;
    private int numberOfClusters;
    private Vector dataSetMean;
    private double dataSetVariance;
    private double zMax;  // the maximum value in the domain

    public PartitionalClusteringProblem() {
        this.clusteringFunction = null;
        this.dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.clusterCenterStrategy = new ClusterCentroidStrategy();
        this.standardDomain = new StringBasedDomainRegistry();
        this.repeatedDomain = new StringBasedDomainRegistry();
        this.fitnessFactory = new MinimisationFitnessFactory();
        this.numberOfClusters = UNINITIALISED;
        this.zMax = UNINITIALISED;
    }

    public PartitionalClusteringProblem(PartitionalClusteringProblem rhs) {
        super(rhs);
        this.clusteringFunction = rhs.clusteringFunction;
        this.dataTableBuilder = rhs.dataTableBuilder;
        this.standardDomain = rhs.standardDomain.getClone();
        this.repeatedDomain = rhs.repeatedDomain.getClone();
        this.distanceMeasure = rhs.distanceMeasure;
        this.fitnessFactory = rhs.fitnessFactory;
        this.numberOfClusters = rhs.numberOfClusters;
        this.zMax = rhs.zMax;
    }

    @Override
    public PartitionalClusteringProblem getClone() {
        return new PartitionalClusteringProblem(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClusteringFunction(ClusteringFunction<Double> clusteringFunction) {
        this.clusteringFunction = clusteringFunction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClusteringFunction<Double> getClusteringFunction() {
        return this.clusteringFunction;
    }

    /**
     * Retrieve the {@link #standardDomain standard domain} of the problem's data set, i.e. NOT the
     * {@link #repeatedDomain repeated domain}. For the repeated domain string, use {@link #getDomain()}.
     *
     * @return the {@link #standardDomain standard domain} of this clustering problem
     */
    @Override
    public DomainRegistry getDomainRegistry() {
        return this.standardDomain;
    }

    /**
     * Specify the {@link #standardDomain standard domain} of the problem's data set. Once this domain registry is set
     * (changed), the {@link #repeatedDomain repeated domain} is {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param domainRegistry the {@link #standardDomain standard domain} of this clustering problem
     */
    @Override
    public void setDomainRegistry(DomainRegistry domainRegistry) {
        this.standardDomain = domainRegistry;
        this.regenerateDomain();
    }

    /**
     * Repeat the standard domain string based on the number of clusters that have been specified. For example, if the
     * {@link #setDomain(String)} method has been called with <code>"R(-1.0,1.0),R(-1.0,1.0)"</code> as parameter and
     * the {@link #setNumberOfClusters(int)} method has been called with <code>3</code> as parameter, then the
     * repeated domain will automatically be regenerated to be
     * <code>"R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0)"</code>.
     *
     * This regeneration of the domain string will occur whenever any of the following methods are called:
     * {@link #setNumberOfClusters(int)}, {@link #setDomain(String)} or {@link #setDomainRegistry(DomainRegistry)}.
     */
    private void regenerateDomain() {
        if (this.standardDomain == null || this.numberOfClusters == UNINITIALISED) {
            System.out.println("#Preliminary: ClusteringProblem not completely configured yet");
            return;
        }

        StringBuilder repeated = new StringBuilder(this.standardDomain.getDomainString());

        for (int i = 1; i < this.numberOfClusters; ++i) {
            repeated.append(",").append(this.standardDomain.getDomainString());
        }

        this.repeatedDomain.setDomainString(repeated.toString());
        this.zMax = ClusteringFunctions.zMax((Vector) this.standardDomain.getBuiltRepresenation());
    }

    /**
     * Specify the domain of the data set being clustered. Once this domain string is set (changed), the repeated domain
     * is automatically {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param domain a {@link String} representing the domain of the data set being clustered
     */
    public void setDomain(String domain) {
        this.standardDomain.setDomainString(domain);
        this.regenerateDomain();
    }

    /**
     * Retrieve the domain as used by the algorithms, i.e. the repeated domain and NOT the standard domain. For the
     * standard domain, see {@link #getDomainRegistry()}.
     *
     * @return the repeated domain registry
     */
    @Override
    public DomainRegistry getDomain() {
        return this.repeatedDomain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DistanceMeasure getDistanceMeasure() {
        return this.distanceMeasure;
    }

    /**
     * Specify the {@link ClusterCenterStrategy} that should be used to represent the center of the clusters.
     * @param clusterCenterStrategy the strategy that should be used to determine the center of the clusters.
     */
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.clusterCenterStrategy = clusterCenterStrategy;
    }

    /**
     * Retrieve the {@link ClusterCenterStrategy} that is used to represent the center of the clusters.
     * @return the configured {@link ClusterCenterStrategy} used to determine the center of the clusters.
     */
    public ClusterCenterStrategy getClusterCenterStrategy() {
        return this.clusterCenterStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFitnessFactory(FitnessFactory<Double> fitnessFactory) {
        this.fitnessFactory = fitnessFactory;
    }

    /**
     * The <em>expert</em> uses this method to set the number of clusters that should be used to optimise this
     * clustering. Once the number of clusters is specified (changed), the {@link #repeatedDomain repeated domain} is
     * automatically {@link #regenerateDomain() regenerated}.
     *
     * @param numberOfClusters the user-specified <em>number of clusters</em> that should be used to optimise the
     * clustering
     */
    @Override
    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
        this.regenerateDomain();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfClusters() {
        return this.numberOfClusters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getZMax() {
        return this.zMax;
    }

    /**
     * @throws UnsupportedOperationException
     * @deprecated rather use {@link #setDataTableBuilder(dataTableBuilder)}
     */
    @Override
    public void setDataSetBuilder(DataSetBuilder dsb) {
        throw new UnsupportedOperationException("Replaced by DataTableBuilder");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDataTableBuilder(DataTableBuilder dataTableBuilder) {
        this.dataTableBuilder = dataTableBuilder;

        try {
            this.dataTable = dataTableBuilder.buildDataTable();
        }
        catch (CIlibIOException cioe) {
            throw new RuntimeException(cioe);
        }

        this.dataSetMean = Stats.meanVector(this.dataTable);
        this.dataSetVariance = Stats.variance(this.dataTable, this.dataSetMean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataTable<StandardPattern, TypeList> getDataTable() {
        return this.dataTable;
    }

    /**
     * Calculate the fitness of the given solution that represents a possible clustering by doing the following:
     * <ol>
     * <li>{@link ClusteringFunctions#disassembleCentroids(dataSetMean, numberOfClusters) Disassemble} the individual
     * centroids from the given solution (a {@link Vector}).</li>
     * <li>{@link ClusteringFunctions#cluster(java.util.List, net.sourceforge.cilib.io.DataTable, net.sourceforge.cilib.util.DistanceMeasure, int) Cluster}
     * all the patterns of the {@link DataTable} into the configured number of clusters using the disassembled
     * centroids.</li>
     * <li>{@link ClusteringFunctions#isValidClustering(java.util.List) Validate} the clustering.</li>
     * <li>Calculate the fitness/error using the configured {@link ClusteringFunction} if the clustering is valid.</li>
     * </ol>
     *
     * @param solution the solution that represents the clustering; expected to be a {@link Vector}
     * @return the {@link Fitness fitness} of the current clustering or the {@link InferiorFitness#instance} when the
     * clustering is {@link ClusteringFunctions#isValidClustering(java.util.List) invalid}
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        Vector combinedCentroids = (Vector) solution;
        List<Vector> separateCentroids = ClusteringFunctions.disassembleCentroids(combinedCentroids, this.numberOfClusters);
        List<Cluster> clusters = ClusteringFunctions.cluster(separateCentroids, this.dataTable, this.distanceMeasure, this.numberOfClusters);

        if (!ClusteringFunctions.isValidClustering(clusters)) { // partitional clustering does not permit empty clusters
            return InferiorFitness.instance();
        }
        return this.fitnessFactory.newFitness(this.clusteringFunction.apply(clusters, this.dataTable, this.distanceMeasure, this.clusterCenterStrategy, this.dataSetMean, this.dataSetVariance, this.zMax));
    }
}
