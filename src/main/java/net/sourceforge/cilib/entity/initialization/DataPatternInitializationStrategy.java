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
package net.sourceforge.cilib.entity.initialization;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.cilib.clustering.PSOClusteringAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 *
 * @author Kristina
 */
public class DataPatternInitializationStrategy<E extends Entity> implements InitializationStrategy<E> {
    private DataTableBuilder tableBuilder;
    private ProbabilityDistributionFuction random;
    private DataTable dataset;
    private PatternConversionOperator patternConversionOperator;
    
    public DataPatternInitializationStrategy() {
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        random = new UniformDistribution();
        dataset = new StandardDataTable();
        patternConversionOperator = new PatternConversionOperator();
    }
    
    public DataPatternInitializationStrategy(DataPatternInitializationStrategy copy) {
        tableBuilder = copy.tableBuilder;
        random = copy.random;
        dataset = copy.dataset;
        patternConversionOperator = copy.patternConversionOperator;
    }
    
    @Override
    public InitializationStrategy getClone() {
        return new DataPatternInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, E entity) {
        int index = (int) random.getRandomNumber(0, dataset.size());
        entity.getProperties().put(key, ((StandardPattern) dataset.getRow(index)).getVector());
    }
    
    public void setDataTableBuilder(DataTableBuilder newDataset) {
        tableBuilder = newDataset;
        
        tableBuilder.addDataOperator(new TypeConversionOperator());
        tableBuilder.addDataOperator(patternConversionOperator);
        try {
            tableBuilder.buildDataTable();
            
        } catch (CIlibIOException ex) {
            Logger.getLogger(PSOClusteringAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dataset = tableBuilder.getDataTable();
    }
    
    public DataTableBuilder getDataTableBuilder() {
        return tableBuilder;
    }
    
    public void setProbabilityDistribution(ProbabilityDistributionFuction distribution) {
        random = distribution;
    }
    
    public ProbabilityDistributionFuction getProbabilityDistribution() {
        return random;
    }
    
    public void setPatternConversionOperator(PatternConversionOperator operator) {
        patternConversionOperator = operator;
    }
    
    public PatternConversionOperator getPatternConversionOperator() {
        return patternConversionOperator;
    }
    
}
