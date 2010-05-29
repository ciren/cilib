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

import java.util.ArrayList;
import java.util.Set;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.dataset.DataSetManager;
import net.sourceforge.cilib.problem.fitnessfactory.FitnessFactory;
import net.sourceforge.cilib.problem.fitnessfactory.MinimisationFitnessFactory;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * <p>
 * This class is used to setup/configure a problem that is capable of clustering the data in
 * a dataset, more specifically the data contained in an
 * {@link net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder}. Clustering is an
 * {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter optimisation} problem. The process of optimising a
 * clustering is driven by a fitness function that determines the fitness of a specific
 * clustering. This class therefore wraps a {@link net.sourceforge.cilib.problem.FunctionOptimisationProblem} (called the
 * {@link #innerProblem}) that may either be a {@link net.sourceforge.cilib.problem.FunctionMinimisationProblem} or a
 * {@link net.sourceforge.cilib.problem.FunctionMaximisationProblem}. The
 * {@link net.sourceforge.cilib.problem.FunctionOptimisationProblem} in turn
 * makes use of a {@link net.sourceforge.cilib.functions.Function function} that determines
 * the fitness of the problem being optimised. Because we are clustering data in a dataset,
 * this function should be a {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction}.
 * </p>
 * <p>
 * The following is a list of methods that should
 * be called (usually from XML in this order) to correctly configure a clustering problem:
 * </p>
 * <ol>
 * <li>{@link #setDomain(String)}</li>
 * <li>{@link #setInnerProblem(FunctionOptimisationProblem)}</li>
 * <li>{@link net.sourceforge.cilib.problem.FunctionOptimisationProblem#setFunction(Function)} on the
 * {@link #innerProblem}</li>
 * <li>{@link #setDataSetBuilder(DataSetBuilder)}</li>
 * <li>{@link net.sourceforge.cilib.problem.dataset.DataSetBuilder#addDataSet(DataSet)} on the dataset builder</li>
 * </ol>
 * <p>
 * One <b>important</b> aspect that should be noted is that the domain of the dataset (or
 * this clustering problem), the number of clusters and the fitness function used to
 * optimise the clustering are all dependant on one another. The reason for this is that the
 * domain of the dataset is duplicated a number of times and then used as the domain of the
 * clustering fitness function. The number of clusters determines the number of times the
 * domain string is duplicated. See {@link #regenerateDomain()} for more detail. The reason
 * for this is because the centroids of a clustering is represented by a single
 * {@link net.sourceforge.cilib.entity.Entity} such as a {@link net.sourceforge.cilib.entity.Particle} or
 * {@link net.sourceforge.cilib.ec.Individual} (that have an internal
 * representation of a {@link net.sourceforge.cilib.type.types.container.Vector}) and these entities are
 * initialised by using the domain of the {@link net.sourceforge.cilib.problem.FunctionOptimisationProblem}
 * which effectively turns out to be the domain of the {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction}.
 * </p>
 * <p>
 * This class also provides a <em>central point</em> for specifying the {@link #distanceMeasure} that should be used
 * for calculating distances throughout the entire clustering process.
 * </p>
 *
 * @see #regenerateDomain()
 * @author Theuns Cloete
 */
public class PartitionalClusteringProblem extends OptimisationProblemAdapter implements ClusteringProblem {
    private static final long serialVersionUID = 7027242527499147957L;

    private static final int UNINITIALISED = -1;

    private ClusteringFunction clusteringFunction;
    private DomainRegistry standardDomain;
    private DomainRegistry repeatedDomain;
    private DistanceMeasure distanceMeasure;
    private FitnessFactory fitnessFactory;
    private int numberOfClusters;
    private double zMax;  // the maximum value in the domain

    public PartitionalClusteringProblem() {
        this.clusteringFunction = null;
        this.standardDomain = new StringBasedDomainRegistry();
        this.repeatedDomain = new StringBasedDomainRegistry();
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.fitnessFactory = new MinimisationFitnessFactory();
        this.numberOfClusters = UNINITIALISED;
        this.zMax = UNINITIALISED;
    }

    public PartitionalClusteringProblem(PartitionalClusteringProblem rhs) {
        super(rhs);
        this.clusteringFunction = rhs.clusteringFunction;
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
     * Sets the {@link ClusteringFunction} that will be used to optimise the clustering. Once the function is set
     * (changed), the {@link #repeatedDomain} is automatically {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param clusteringFunction the {@link ClusteringFunction} that should be used to optimise the clustering
     */
    @Override
    public void setClusteringFunction(ClusteringFunction clusteringFunction) {
        this.clusteringFunction = clusteringFunction;
        this.regenerateDomain();
    }

    @Override
    public ClusteringFunction getClusteringFunction() {
        return this.clusteringFunction;
    }

    /**
     * Return the actual domain of the problem's dataset, i.e. NOT the {@link #repeatedDomain}. For the repeated domain
     * string, see {@link #getDomain()}.
     *
     * @return the {@link #standardDomain} of this clustering problem
     */
    @Override
    public DomainRegistry getDomainRegistry() {
        return this.standardDomain;
    }

    /**
     * Set the actual domain of the problem's data set. Once this domain registry is set
     * (changed), the {@link #repeatedDomain} is {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param domainRegistry the {@link #standardDomain} of this clustering problem
     */
    @Override
    public void setDomainRegistry(DomainRegistry domainRegistry) {
        this.standardDomain = domainRegistry;
        this.regenerateDomain();
    }

    /**
     * Repeat the domain string of this clustering problem's data set based on the number of clusters that have been
     * specified. For example, if the {@link #setDomain(String)} method has been called with
     * <code>"R(-1.0,1.0),R(-1.0,1.0)"</code> as parameter and the {@link #setNumberOfClusters(int)} method has been
     * called with <code>3</code> as parameter, then the clustering fitness function's domain will automatically be
     * regenerated to be "R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0)".<br/>This
     * regeneration of the domain string will occur whenever any of the following methods are called:
     * {@link #setClusteringFunction(net.sourceforge.cilib.functions.clustering.ClusteringFunction)},
     * {@link #setNumberOfClusters(int)},
     * {@link #setDomain(String)} or {@link #setDomainRegistry(DomainRegistry)}.
     */
    private void regenerateDomain() {
        if (this.clusteringFunction == null || this.standardDomain == null || this.numberOfClusters == UNINITIALISED) {
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
     * Sets the domain of the dataset being clustered. Once this domain string is set
     * (changed), the domain of the {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction}
     * is automatically {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param domainRepresentation a {@link String} representing the domain of the dataset being
     *        clustered
     */
    public void setDomain(String domainRepresentation) {
        this.standardDomain.setDomainString(domainRepresentation);
        this.regenerateDomain();
    }

    /**
     * Return the domain as used by the algorithms, i.e. the repeated domain and NOT the standard domain. For the
     * standard domain, see {@link #getDomainRegistry()}.
     *
     * @return the repeated domain registry
     */
    @Override
    public DomainRegistry getDomain() {
        return this.repeatedDomain;
    }

    /**
     * Set the {@link net.sourceforge.cilib.util.DistanceMeasure} that will be used for all distance calculations
     * throughout a clustering.
     *
     * @param distanceMeasure the desired {@link net.sourceforge.cilib.util.DistanceMeasure}
     */
    @Override
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * Retrieve the configured {@link DistanceMeasure} that should be used for all similarity measurements during the
     * clustering.
     *
     * @return the configured {@link DistanceMeasure}
     */
    @Override
    public DistanceMeasure getDistanceMeasure() {
        return this.distanceMeasure;
    }

    /**
     * Specify the {@link FitnessFactory} that should be used to create a {@link Fitness} object when the
     * {@link #calculateFitness(net.sourceforge.cilib.type.types.Type)} method is called.
     * @param the {@link FitnessFactory} should should be used
     */
    @Override
    public void setFitnessFactory(FitnessFactory fitnessFactory) {
        this.fitnessFactory = fitnessFactory;
    }

    /**
     * The <em>expert</em> uses this method to set the number of clusters that should be used to optimise this
     * clustering. Once the number of clusters is set (changed), the {@link #repeatedDomain} is automatically
     * {@link #regenerateDomain() regenerated}.
     *
     * @param numberOfClusters the user-specified <em>number of clusters</em> that should be used to optimise this
     * clustering
     */
    @Override
    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
        this.regenerateDomain();
    }

    /**
     * Return the number of clusters used throughout this clustering problem.
     * @return the configured number of clusters
     */
    @Override
    public int getNumberOfClusters() {
        return this.numberOfClusters;
    }

    /**
     * Retrieve the maximum value possible in the configured domain. Whenever the domain is changed, the zMax will also
     * be re-calculated.
     * @return the maximum value possible in the domain
     */
    @Override
    public double getZMax() {
        return this.zMax;
    }

    /**
     * Use the {@link DataSetManager} singleton to parse and/or retrieve the given
     * {@link DataSetBuilder}. Then use the {@link ClusteringUtils} per-thread singleton to
     * set the {@link DataSetBuilder} as the current dataset for this clustering.
     *
     * @throws IllegalArgumentException when the given {@link net.sourceforge.cilib.problem.dataset.DataSetBuilder} is not a
     *         {@link net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder}. This is only temporary, because I
     *         didn't want to change the more generic {@link net.sourceforge.cilib.problem.dataset.DataSetBuilder} too much.
     * @param dsb the {@link net.sourceforge.cilib.problem.dataset.DataSetBuilder} that represents the dataset that should be
     *        clustered
     */
    @Override
    public void setDataSetBuilder(DataSetBuilder dsb) {
        if (!(dsb instanceof StaticDataSetBuilder)) {
            throw new IllegalArgumentException("This ClusteringProblem expects a StaticDataSetBuilder\nONLY FOR NOW\nBECAUSE I didn't want to change the more generic DataSetBuilder");
        }

        StaticDataSetBuilder builder = (StaticDataSetBuilder) dsb;

        this.dataSetBuilder = DataSetManager.getInstance().getDataSetBuilder(builder);
    }

    /**
     * Calculate the fitness of the given solution that represents a possible clustering by doing the following:
     * <ol>
     * <li>Extract (disassemble) the individual centroids from the given solution (usually a {@link Vector}).</li>
     * <li>Cluster all the patterns of the data set into the configured number of clusters using the disassembled centroids.</li>
     * <li>{@link ClusteringFunctions#isValidClustering(java.util.ArrayList) Validate} the clustering.</li>
     * <li>Calculate the fitness using the configured {@link ClusteringFunction}</li>
     *
     * @param solution the solution that represents the clustering; expected to be {@link Vector}
     * @return the fitness of the current clustering or the {@link InferiorFitness#instance} when the number of clusters
     * formed does not equal the required number of clusters
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        Vector combinedCentroids = (Vector) solution;
        StaticDataSetBuilder staticDataSetBuilder = (StaticDataSetBuilder) this.dataSetBuilder;
        Set<Pattern<Vector>> patterns = staticDataSetBuilder.getPatterns();
        ArrayList<Vector> separateCentroids = ClusteringFunctions.disassembleCentroids(combinedCentroids, this.numberOfClusters);
        ArrayList<Cluster<Vector>> clusters = ClusteringFunctions.cluster(separateCentroids, patterns, this.distanceMeasure, this.numberOfClusters);
        Vector dataSetMean = staticDataSetBuilder.getMean();
        double dataSetVariance = staticDataSetBuilder.getVariance();

        if (!ClusteringFunctions.isValidClustering(clusters)) { // partitional clustering does not permit empty clusters
            return InferiorFitness.instance();
        }
        return this.fitnessFactory.newFitness(this.clusteringFunction.apply(clusters, patterns, this.distanceMeasure, dataSetMean, dataSetVariance, zMax));
    }
}
