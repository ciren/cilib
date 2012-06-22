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
package net.sourceforge.cilib.clustering;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;

/**
 *
 * @author Kristina
 */
public class SlidingWindow {
    private DataTable completeDataset;
    private DataTable currentDataset;
    private DataTableBuilder tableBuilder;
    private int windowSize;
    private DataOperator patternConverstionOperator;
    private int currentIndex;
    private int slidingTime;
    private boolean isTemporal;
    private int frequency;
    
    public SlidingWindow() {
        completeDataset = new StandardPatternDataTable();
        currentDataset = new StandardPatternDataTable();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        windowSize = 0;
        patternConverstionOperator = new PatternConversionOperator();
        currentIndex = 0;
        slidingTime = 0;
        isTemporal = true;
        frequency = 0;
    }
    
    public SlidingWindow(SlidingWindow copy) {
        completeDataset = copy.completeDataset;
        currentDataset = copy.currentDataset;
        tableBuilder = copy.tableBuilder;
        windowSize = copy.windowSize;
        patternConverstionOperator = copy.patternConverstionOperator;
        currentIndex = copy.currentIndex;
        slidingTime = copy.slidingTime;
        isTemporal = copy.isTemporal;
        frequency = copy.frequency;
    }
    
    public SlidingWindow getClone() {
        return new SlidingWindow(this);
    }
    
    private boolean hasNotFinished() {
            return currentIndex < completeDataset.size();
    }
    
    public DataTable slideWindow(int iterations) {
        if(hasNotFinished()) {
            if(slidingTime == getIterationToChange(iterations)) {
                currentDataset = new StandardPatternDataTable();
                
                int upTo = currentIndex + windowSize;
                if(currentIndex + windowSize > completeDataset.size()) {
                    upTo = completeDataset.size();
                }
                
                for(int i = currentIndex; i < upTo; i++) {
                    currentDataset.addRow((StandardPattern) completeDataset.getRow(i));
                }
                
                currentIndex = upTo;
                slidingTime = 0;
            } else {
                slidingTime++;
            }
        }
        return currentDataset;
    }
    
    public DataTable initializeWindow() {
        tableBuilder.addDataOperator(new TypeConversionOperator());
        tableBuilder.addDataOperator(patternConverstionOperator);
        try {
            tableBuilder.buildDataTable();
            
        } catch (CIlibIOException ex) {
            Logger.getLogger(DataClusteringPSO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        completeDataset = tableBuilder.getDataTable();
        
        if((windowSize == 0) || windowSize == completeDataset.size()) {
            windowSize = completeDataset.size();
            isTemporal = false;
        } else if(windowSize > completeDataset.size()) {
            throw new UnsupportedOperationException("The window size provided is larger than the size of the dataset");
        }
        
        for(int i = 0; i < currentIndex + windowSize; i++) {
            currentDataset.addRow((StandardPattern) completeDataset.getRow(i));
        }
        
        currentIndex+= windowSize;
        slidingTime++;
        return currentDataset;
    }
    
    public void setWindowSize(int size) {
        windowSize = size;
    }
    
    public int getWindowSize() {
        return windowSize;
    }
    
    /**
     * Gets the datatable builder.
     * @return the datatable builder.
     */
    public DataTableBuilder getDataTableBuilder() {
        return tableBuilder;
    }

    /**
     * Sets the data table builder.
     * @param dataTableBuilder the new data table builder.
     */
    public void setDataTableBuilder(DataTableBuilder dataTableBuilder) {
        this.tableBuilder = dataTableBuilder;
    }

    /**
     * Gets the source URL of the the data table builder.
     * @return the source URL of the the data table builder.
     */
    public String getSourceURL() {
        return tableBuilder.getSourceURL();
    }

    /**
     * Sets the source URL of the the data table builder.
     * @param sourceURL the new source URL of the the data table builder.
     */
    public void setSourceURL(String sourceURL) {
        tableBuilder.setSourceURL(sourceURL);
    }

    /**
     * Get the { @link PatternConversionOperator}
     * @return the pattern conversion operator
     */
    public DataOperator getPatternConversionOperator() {
        return patternConverstionOperator;
    }

    /**
     * Set the { @link PatternConversionOperator}
     * @param patternConverstionOperator the new pattern conversion operator
     */
    public void setPatternConversionOperator(DataOperator patternConverstionOperator) {
        this.patternConverstionOperator = patternConverstionOperator;
    }
    
    /**
     * Gets the current dataset.
     * @return the current dataset builder.
     */
    public DataTable getCurrentDataset() {
        return currentDataset;
    }
    
    /**
     * Gets the current dataset.
     * @return the current dataset builder.
     */
    public DataTable getCompleteDataset() {
        return completeDataset;
    }
    
    private int getIterationToChange(int iterations) {
        if(frequency == 0) {
            double denominator = ((double) (completeDataset.size()) / (double) windowSize);
            frequency = (int) (iterations / (double) denominator);
        }
        return frequency;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    public void setFrequency(int newFrequency) {
        frequency = newFrequency;
    }
    
    public boolean hasSlid() {
        return isTemporal && slidingTime == 0;
    }
    
}
