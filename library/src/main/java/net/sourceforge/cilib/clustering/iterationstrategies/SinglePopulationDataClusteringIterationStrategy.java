/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 *This class holds the methods that are shared by certain clustering iteration strategies
 */
public abstract class SinglePopulationDataClusteringIterationStrategy extends AbstractIterationStrategy<DataClusteringPSO>{
    protected int reinitialisationInterval;
    protected int dimensions;
    protected boolean reinitialised;
    protected String fileName;

    /*
     * Default constructor for SinglePopulationDataClusteringIterationStrategy
     */
    public SinglePopulationDataClusteringIterationStrategy() {
        reinitialised = false;
        boundaryConstraint = new CentroidBoundaryConstraint();
        reinitialisationInterval = 1;
        dimensions = 0;
    }

    /*
     * Copy constructor for SinglePopulationDataClusteringIterationStrategy
     * @param copy The SinglePopulationDataClusteringIterationStrategy to be copied
     */
    public SinglePopulationDataClusteringIterationStrategy(SinglePopulationDataClusteringIterationStrategy copy) {
        boundaryConstraint = copy.boundaryConstraint;
        reinitialisationInterval = copy.reinitialisationInterval;
        dimensions = copy.dimensions;
        reinitialised = copy.reinitialised;
    }

    /*
     * Abstract clone method for SinglePopulationDataClusteringIterationStrategy
     */
    @Override
    public abstract SinglePopulationDataClusteringIterationStrategy getClone();

    /*
     * Abstract method to perform an iteration of the iteration strategy
     */
    @Override
    public abstract void performIteration(DataClusteringPSO algorithm);

    /*
     * Sets the boundary constraint
     * @param constraint The new boundary constraint
     */
    public void setCentroidBoundaryConstraint(CentroidBoundaryConstraint constraint) {
        boundaryConstraint = constraint;
    }

    /*
     * Sets the interval at which the particles will be re-initialised if reinitialisation
     * due to change in environment is required. In other words, every how-many particles
     * must be initialised? To initialise all, the interval is 1
     * @param interval the new interval
     */
    public void setReinitialisationInterval(int interval) {
        reinitialisationInterval = interval;
    }

    /*
     * Returns the value of the reinitialisation interval
     * @return reinitialisationInterval The value of the reinitialisation interval
     */
    public int getReinitialisationInterval() {
        return reinitialisationInterval;
    }

    /*
     * Sets the dimensions of the centroids (determined by the dataset)
     * @param dim The new dimensions
     */
    public void setDimensions(int dim) {
        dimensions = dim;
    }
}
