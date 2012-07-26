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
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *This class holds the methods that are shared by certain clustering iteration strategies
 */
public abstract class SinglePopulationDataClusteringIterationStrategy extends AbstractIterationStrategy<DataClusteringPSO>{
    protected DataTable dataset;
    protected EuclideanDistanceMeasure distanceMeasure;
    protected SlidingWindow window;
    protected int reinitialisationInterval;
    protected int dimensions;
    protected boolean reinitialized;
    protected String fileName;
    
    /*
     * Default constructor for SinglePopulationDataClusteringIterationStrategy
     */
    public SinglePopulationDataClusteringIterationStrategy() {
        dataset = new StandardPatternDataTable();
        reinitialized = false;
        distanceMeasure = new EuclideanDistanceMeasure();
        boundaryConstraint = new CentroidBoundaryConstraint();
        window = new SlidingWindow();
        reinitialisationInterval = 1;
        dimensions = 0;
        
    }
    
    /*
     * Copy constructor for SinglePopulationDataClusteringIterationStrategy
     * @param vopy The SinglePopulationDataClusteringIterationStrategy to be copied
     */
    public SinglePopulationDataClusteringIterationStrategy(SinglePopulationDataClusteringIterationStrategy copy) {
        dataset = copy.dataset;
        distanceMeasure = copy.distanceMeasure;
        boundaryConstraint = copy.boundaryConstraint;
        window = copy.window;
        reinitialisationInterval = copy.reinitialisationInterval;
        dimensions = copy.dimensions;
        reinitialized = copy.reinitialized;
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
    
    /**
     * Get the Distance Measure
     * @return the Distance Measure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }
    
    /**
     * Get the Dataset
     * @return the Dataset
     */
    public DataTable getDataset() {
        return dataset;
    }
    
    /*
     * Sets the boundary constraint
     * @param constraint The new boundary constraint
     */
    public void setCentroidBoundaryConstraint(CentroidBoundaryConstraint constraint) {
        boundaryConstraint = constraint;
    }
    
    /*
     * Sets the itnerval at which the particles will be re-initialized if reinitialization 
     * due to change in environment is required. In other words, every how-many particles
     * must be initialized? To initialize all, the interval is 1
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
    
    /*
     * Sets the sliding window to the one provided as a parameter
     * @param slidingWindow The new sliding window 
     */
    public void setWindow(SlidingWindow slidingWindow) {
        window = slidingWindow;
        dataset = window.getCurrentDataset();
    }
    
    /*
     * Returns the current sliding window  
     * @return window The current sliding window
     */
    public SlidingWindow getWindow() {
        return window;
    }
    
    /*
     * Adds the data patterns closest to a centrid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    public void assignDataPatternsToParticle(CentroidHolder candidateSolution, DataTable dataset) {
        double euclideanDistance;
        Vector addedPattern;
        DistanceMeasure aDistanceMeasure = new EuclideanDistanceMeasure();
        
        for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    if(aDistanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = aDistanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
    }
}
