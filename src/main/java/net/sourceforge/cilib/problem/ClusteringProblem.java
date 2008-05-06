/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.dataset.DataSetManager;
import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.util.ClusteringUtils;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

import org.apache.log4j.Logger;

/**
 * This class is used to setup/configure a problem that is capable of clustering the data in
 * a dataset, more specifically the data contained in an
 * {@link AssociatedPairDataSetBuilder}. Clustering is an
 * {@link OptimisationProblemAdapter optimisation} problem. The process of optimising a
 * clustering is driven by a fitness function that determines the fitness of a specific
 * clustering. This class therefore wraps a {@link FunctionOptimisationProblem} (called the
 * {@link #innerProblem}) that may either be a {@link FunctionMinimisationProblem} or a
 * {@link FunctionMaximisationProblem}. The {@link FunctionOptimisationProblem} in turn
 * makes use of a {@link Function} that determines the fitness of the problem being
 * optimised. Because we are clustering data in a dataset, this function should be a
 * {@link ClusteringFitnessFunction}.<br/>The following is a list of methods that should
 * be called (usually from XML in this order) to correctly configure a clustering problem:
 * <ol>
 * <li>{@link #setDomain(String)}</li>
 * <li>{@link #setInnerProblem(FunctionOptimisationProblem)}</li>
 * <li>{@link FunctionOptimisationProblem#setFunction(Function)} on the
 * {@link #innerProblem}</li>
 * <li>{@link #setDataSetBuilder(DataSetBuilder)}</li>
 * <li>{@link DataSetBuilder#addDataSet(DataSet)} on the dataset builder</li>
 * </ol>
 * One <b>important</b> aspect that should be noted is that the domain of the dataset (or
 * this clustering problem), the number of clusters and the fitness function used to
 * optimise the clustering are all dependant on one another. The reason for this is that the
 * domain of the dataset is duplicated a number of times and then used as the domain of the
 * clustering fitness function. The number of clusters determines the number of times the
 * domain string is duplicated. See {@link #regenerateDomain()} for more detail. The reason
 * for this is because the centroids of a clustering is represented by a single
 * {@link Entity} such as a {@link Particle} or {@link Individual} (that have an internal
 * representation of a {@link Vector}) and these entities are initialised by using the
 * domain of the {@link FunctionOptimisationProblem} which effectively turns out to be the
 * domain of the {@link ClusteringFitnessFunction}. <br/>This class also provides a
 * <em>central point</em> for specifying the {@link #distanceMeasure} that should be used
 * for calculating distances throughout the entire clustering process.
 * 
 * @see #regenerateDomain()
 * @author Theuns Cloete
 */
public class ClusteringProblem extends OptimisationProblemAdapter {
	private static final long serialVersionUID = 7027242527499147957L;
	private static Logger log = Logger.getLogger(ClusteringProblem.class);

	private FunctionOptimisationProblem innerProblem;
	private int numberOfClusters;
	private DomainRegistry domainRegistry;
	private DistanceMeasure distanceMeasure;
	private static final int UNINITIALISED = -1;

	public ClusteringProblem() {
		super();
		innerProblem = null;
		numberOfClusters = UNINITIALISED;
		domainRegistry = new DomainRegistry();
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
	 * {@link FunctionOptimisationProblem} that either optimises the fitness as calculated by
	 * a {@link ClusteringFitnessFunction}. Once the problem is set (changed), the domain of
	 * the {@link ClusteringFitnessFunction} is automatically
	 * {@link #regenerateDomain() regenerated}.
	 * 
	 * @see #regenerateDomain()
	 * @param fop a {@link FunctionOptimisationProblem} that should take a
	 *        {@link ClusteringFitnessFunction} that drives the optimisation process
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
	 * domain of the {@link ClusteringFitnessFunction} is automatically
	 * {@link #regenerateDomain() regenerated}.
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
	 * string as used by the clustering fitness function.
	 * 
	 * @return the {@link #domainRegistry} of this clustering problem
	 */
	public DomainRegistry getDomainRegistry() {
		return domainRegistry;
	}

	/**
	 * Set the actual domain of the problem's dataset. Once this domain registry is set
	 * (changed), the domain of the {@link ClusteringFitnessFunction} is automatically
	 * {@link #regenerateDomain() regenerated}.
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
	 * {@link #setDomain(String)} or {@link #setDomainRegistry(DomainRegistry)}.
	 */
	private void regenerateDomain() {
		if (innerProblem == null || innerProblem.getFunction() == null || domainRegistry == null || numberOfClusters == UNINITIALISED) {
			log.warn("Preliminary: ClusteringProblem not completely configured yet");
			return;
		}

		String duplicated = new String(domainRegistry.getDomainString());

		for (int i = 1; i < numberOfClusters; i++) {
			duplicated += "," + domainRegistry.getDomainString();
		}
		innerProblem.getFunction().setDomain(duplicated);
	}

	/**
	 * Return the actual domain of the problem's dataset, i.e. NOT the duplicated domain
	 * string as used by the clustering fitness function.
	 * 
	 * @return the {@link #domainRegistry} of this clustering problem
	 */
	public DomainRegistry getBehaviouralDomain() {
		return domainRegistry;
	}

	/**
	 * Sets the domain of the dataset being clustered. Once this domain string is set
	 * (changed), the domain of the {@link ClusteringFitnessFunction} is automatically
	 * {@link #regenerateDomain() regenerated}.
	 * 
	 * @see #regenerateDomain()
	 * @param representation a {@link String} representing the domain of the dataset being
	 *        clustered
	 */
	public void setDomain(String representation) {
		DomainParser parser = DomainParser.getInstance();
		parser.parse(representation);

		domainRegistry.setDomainString(representation);
		domainRegistry.setExpandedRepresentation(parser.expandDomainString(representation));
		domainRegistry.setBuiltRepresenation(parser.getBuiltRepresentation());
		regenerateDomain();
	}

	/**
	 * Return the domain as used by the configured fitness function, i.e. NOT the simplified
	 * domain string of the problem's dataset.
	 * 
	 * @return the {@link #innerProblem}'s {@link FunctionOptimisationProblem#function}'s
	 *         domain registry
	 */
	public DomainRegistry getDomain() {
		return innerProblem.getFunction().getDomainRegistry();
	}

	/**
	 * Use the {@link DataSetManager} singleton to parse and/or retrieve the given
	 * {@link DataSetBuilder}. Then use the {@link ClusteringUtils} per-thread singleton to
	 * set the {@link DataSetBuilder} as the current dataset for this clustering.
	 * 
	 * @throws IllegalArgumentException when the given {@link DataSetBuilder} is not an
	 *         {@link AssociatedPairDataSetBuilder}. This is only temporary, because I
	 *         didn't want to change the more generic {@link DataSetBuilder} too much.
	 * @param dsb the {@link DataSetBuilder} that represents the dataset that should be
	 *        clustered
	 */
	@Override
	public void setDataSetBuilder(DataSetBuilder dsb) {
		if (!(dsb instanceof AssociatedPairDataSetBuilder))
			throw new IllegalArgumentException("This ClusteringProblem expects an AssociatedPairDataSet\nONLY FOR NOW\nBECAUSE I didn't want to change the more generic DataSetBuilder");

		AssociatedPairDataSetBuilder builder = (AssociatedPairDataSetBuilder) dsb;

		dataSetBuilder = DataSetManager.getInstance().getDataSetBuilder(builder);
		ClusteringUtils.get().setClusterableDataSet((ClusterableDataSet) dataSetBuilder);
	}

	/**
	 * Set the {@link DistanceMeasure} that will be used for all distance calculations
	 * throughout a clustering.
	 * 
	 * @param dm the desired {@link DistanceMeasure}
	 */
	public void setDistanceMeasure(DistanceMeasure dm) {
		distanceMeasure = dm;
	}

	/**
	 * This method will be called from
	 * {@link ClusteringUtils#calculateDistance(Vector, Vector)} which is the
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
	protected Fitness calculateFitness(Object solution) {
		return innerProblem.calculateFitness(solution);
	}
}
