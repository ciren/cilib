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

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;

/**
 * This class initializes a ClusterParticle to contain a CentroidHolder as the candidate solution, velocity and best position
 * It initializes ClusterCentroids to the positions of existing data patterns
 */
public class DataPatternInitializationStrategy <E extends Entity> extends DataDependantInitializationStrategy<E> {
    private ProbabilityDistributionFunction random;
    private PatternConversionOperator patternConversionOperator;
    
    /*
     * Default constructor for the DataPatternInitializationStrategy
     */
    public DataPatternInitializationStrategy() {
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        random = new UniformDistribution();
        dataset = new StandardDataTable();
        patternConversionOperator = new PatternConversionOperator();
    }
    
    /*
     * Copy constructor for the DataPatternInitializationStrategy
     * @param copy The DataPatternInitializationStrategy to be copied
     */
    public DataPatternInitializationStrategy(DataPatternInitializationStrategy copy) {
        tableBuilder = copy.tableBuilder;
        random = copy.random;
        dataset = copy.dataset;
        patternConversionOperator = copy.patternConversionOperator;
    }
    
    /*
     * The clone method of the DataPatternInitializationStrategy
     */
    @Override
    public InitializationStrategy getClone() {
        return new DataPatternInitializationStrategy(this);
    }

    /*
     * Initializes the entity's centroids to the positions of existing data patterns
     * @param key The key stating which property of the entity must be initialized
     * @param entity The entity to be initialized
     */
    @Override
    public void initialize(Enum<?> key, E entity) {
        int index = (int) random.getRandomNumber(0, dataset.size());
        CentroidHolder holder = new CentroidHolder();
        
        for(int i=0; i< ((CentroidHolder) entity.getProperties().get(key)).size(); i++) {
            ClusterCentroid centroid = new ClusterCentroid();
            centroid.copy(((StandardPattern) dataset.getRow(index)).getVector());
            holder.add(centroid);
        }
        
        entity.getProperties().put(key, holder);
    }
    
    /*
     * Sets the dataset that will be used to initialize the entity
     * @param table The dataset that will be used to initialize the entity
     */
    public void setDataset(DataTable table) {
        dataset = table;
    }
  
    /*
     * Returns the dataset that was used to initialize the entity
     * @return dataset The dataset that was used to initialize the entity
     */
    public DataTable getDataset() {
        return dataset;
    }
}
