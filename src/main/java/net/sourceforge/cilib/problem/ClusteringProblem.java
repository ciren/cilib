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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.dataset.DataSetManager;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.ClusteringUtils;
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
public class ClusteringProblem extends OptimisationProblemAdapter {
    private static final long serialVersionUID = 7027242527499147957L;

    private FunctionOptimisationProblem innerProblem;
    private int numberOfClusters;
    private DomainRegistry domainRegistry;
    private DistanceMeasure distanceMeasure;
    private static final int UNINITIALISED = -1;

    public ClusteringProblem() {
        innerProblem = null;
        numberOfClusters = UNINITIALISED;
        domainRegistry = new StringBasedDomainRegistry();
        distanceMeasure = new EuclideanDistanceMeasure();
        // needed so that the unit tests don't get NullPointerExceptions
        ClusteringUtils.get().setClusteringProblem(this);
    }

    public ClusteringProblem(ClusteringProblem rhs) {
        super(rhs);
        innerProblem = rhs.innerProblem.getClone();
        numberOfClusters = rhs.numberOfClusters;
        domainRegistry = rhs.domainRegistry.getClone();
        distanceMeasure = rhs.distanceMeasure;
        // not sure if it is needed, but it can't hurt
        ClusteringUtils.get().setClusteringProblem(this);
    }

    @Override
    public ClusteringProblem getClone() {
        return new ClusteringProblem(this);
    }

    /**
     * Sets the problem that will be used to optimise the clustering. This is in most cases a
     * {@link net.sourceforge.cilib.problem.FunctionOptimisationProblem} that either optimises
     * the fitness as calculated by a {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction}.
     * Once the problem is set (changed), the domain of the
     * {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction} is automatically
     * {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param fop a {@linkplain net.sourceforge.cilib.problem.FunctionOptimisationProblem} that should take a
     *        {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction} that drives the
     *        optimisation process.
     */
    public void setInnerProblem(FunctionOptimisationProblem fop) {
        innerProblem = fop;
        regenerateDomain();
    }

    /**
     * Return the number of clusters used throughout this clustering problem.
     * @return the {@link #numberOfClusters}
     */
    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    /**
     * The <em>expert</em> uses this method to set the number of clusters that should be
     * used to optimise this clustering. Once the number of clusters is set (changed), the
     * domain of the {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction}
     * is automatically {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param noc the user-specified <em>number of clusters</em> that should be used to
     *        optimise this clustering
     */
    public void setNumberOfClusters(int noc) {
        numberOfClusters = noc;
        regenerateDomain();
    }

    /**
     * Return the actual domain of the problem's dataset, i.e. NOT the duplicated domain
     * string as used by the clustering fitness function. For the duplicated domain string, see {@link #getDomain()}.
     *
     * @return the {@link #domainRegistry} of this clustering problem
     */
    public DomainRegistry getDomainRegistry() {
        return domainRegistry;
    }

    /**
     * Set the actual domain of the problem's dataset. Once this domain registry is set
     * (changed), the domain of the {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction}
     * is automatically {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param dr the {@link #domainRegistry} of this clustering problem
     */
    public void setDomainRegistry(DomainRegistry dr) {
        domainRegistry = dr;
        regenerateDomain();
    }

    /**
     * Duplicate the domain string of this clustering problem's dataset based on the number
     * of clusters that have been specified. For example, if the {@link #setDomain(String)}
     * method has has been called with <code>"R(-1.0,1.0),R(-1.0,1.0)"</code> as parameter
     * and the {@link #setNumberOfClusters(int)} method has been called with <code>3</code>
     * as parameter, then the clustering fitness function's domain will automatically be
     * regenerated to be
     * "R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0),R(-1.0,1.0)". <br/>This
     * regeneration of the domain string will occur whenever any of the following methods are
     * called:
     * {@link #setInnerProblem(FunctionOptimisationProblem), {@link #setNumberOfClusters(int)},
     * {@link #setDomain(String)} or {@link #setDomainRegistry(StringBasedDomainRegistry)}.
     */
    private void regenerateDomain() {
        if (innerProblem == null || innerProblem.getFunction() == null || domainRegistry == null || numberOfClusters == UNINITIALISED) {
            System.out.println("Preliminary: ClusteringProblem not completely configured yet");
            return;
        }

        StringBuilder duplicated = new StringBuilder(domainRegistry.getDomainString());

        for (int i = 1; i < numberOfClusters; i++) {
            duplicated.append(",").append(domainRegistry.getDomainString());
        }
        innerProblem.getFunction().setDomain(duplicated.toString());
    }

    /**
     * Sets the domain of the dataset being clustered. Once this domain string is set
     * (changed), the domain of the {@link net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction}
     * is automatically {@link #regenerateDomain() regenerated}.
     *
     * @see #regenerateDomain()
     * @param representation a {@link String} representing the domain of the dataset being
     *        clustered
     */
    public void setDomain(String representation) {
//        DomainParser parser = new DomainParser();
//        parser.parse(representation);

        domainRegistry.setDomainString(representation);
        regenerateDomain();
    }

    /**
     * Return the domain as used by the configured fitness function, i.e. NOT the basic
     * domain string of the problem's dataset. For the basic domain string, see {@link #getBehaviouralDomain()}.
     *
     * @return the {@link #innerProblem}'s {@linkplain net.sourceforge.cilib.problem.FunctionOptimisationProblem#function function's}
     *         domain registry
     */
    @Override
    public DomainRegistry getDomain() {
        return innerProblem.getFunction().getDomainRegistry();
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
        ClusteringUtils.get().setDataSetBuilder((StaticDataSetBuilder) this.dataSetBuilder);
    }

    /**
     * Set the {@link net.sourceforge.cilib.util.DistanceMeasure} that will be used for all distance calculations
     * throughout a clustering.
     *
     * @param dm the desired {@link net.sourceforge.cilib.util.DistanceMeasure}
     */
    public void setDistanceMeasure(DistanceMeasure dm) {
        distanceMeasure = dm;
    }

    /**
     * This method will be called from
     * {@link net.sourceforge.cilib.util.ClusteringUtils#calculateDistance(Vector, Vector)} which is the
     * <em>central point</em> for distance calculations during a clustering.
     *
     * @return the {@link #distanceMeasure}
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * We are actually optimising the {@link #innerProblem}, so use it to calculate the
     * fitness.
     *
     * @return the fitness of the current clustering
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        return innerProblem.calculateFitness(solution);
    }
}
