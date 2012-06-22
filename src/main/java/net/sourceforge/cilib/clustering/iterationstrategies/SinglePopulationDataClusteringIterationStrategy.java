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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author Kristina
 */
public abstract class SinglePopulationDataClusteringIterationStrategy extends AbstractIterationStrategy<DataClusteringPSO>{
    protected DataTable dataset;
    protected EuclideanDistanceMeasure distanceMeasure;
    protected SlidingWindow window;
    protected int reinitialisationInterval;
    protected int dimensions;
    protected FileOutputStream fileStream;
    protected PrintStream writer;
    protected boolean reinitialized;
    protected String fileName;
    
    public SinglePopulationDataClusteringIterationStrategy() {
        dataset = new StandardPatternDataTable();
        reinitialized = false;
        distanceMeasure = new EuclideanDistanceMeasure();
        boundaryConstraint = new CentroidBoundaryConstraint();
        window = new SlidingWindow();
        reinitialisationInterval = 1;
        fileName = "TestResults.txt";
        dimensions = 0;
        try {
            fileStream = new FileOutputStream(fileName, true);
            writer = new PrintStream(fileStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataClusteringPSO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SinglePopulationDataClusteringIterationStrategy(SinglePopulationDataClusteringIterationStrategy copy) {
        dataset = copy.dataset;
        distanceMeasure = copy.distanceMeasure;
        boundaryConstraint = copy.boundaryConstraint;
        window = copy.window;
        reinitialisationInterval = copy.reinitialisationInterval;
        dimensions = copy.dimensions;
        writer = copy.writer;
        fileStream = copy.fileStream;
        reinitialized = copy.reinitialized;
    }
    
    @Override
    public AbstractIterationStrategy<DataClusteringPSO> getClone() {
        throw new UnsupportedOperationException("Performed on lower level");
    }

    @Override
    public void performIteration(DataClusteringPSO algorithm) {
        throw new UnsupportedOperationException("Performed on lower level");
    }
    
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
    
    public void setCentroidBoundaryConstraint(CentroidBoundaryConstraint constraint) {
        boundaryConstraint = constraint;
    }
    
    public void setReinitialisationInterval(int interval) {
        reinitialisationInterval = interval;
    }
    
    public int getReinitialisationInterval() {
        return reinitialisationInterval;
    }
    
    public void setDimensions(int dim) {
        dimensions = dim;
    }
    
    public boolean reinitialized() {
        return reinitialized;
    }
    
    public void setFileName(String name) {
        fileName = name;
        try {
            fileStream = new FileOutputStream(fileName, true);
            writer = new PrintStream(fileStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataClusteringPSO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setWindow(SlidingWindow slidingWindow) {
        window = slidingWindow;
        dataset = window.getCurrentDataset();
    }
    
    public SlidingWindow getWindow() {
        return window;
    }
}
