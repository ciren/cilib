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
package net.sourceforge.cilib.algorithm.initialisation;

import java.util.ArrayList;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.initialization.StandardCentroidInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.DataPatternInitializationStrategy;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class DataDependantPopulationInitializationStrategy <E extends Entity> implements PopulationInitialisationStrategy<E>{
    private PopulationInitialisationStrategy delegate;
    private Entity prototypeEntity;
    private int entityNumber;
    private DataTableBuilder tableBuilder;
    DataTable dataset;
    
    public DataDependantPopulationInitializationStrategy() {
        delegate = new ClonedPopulationInitialisationStrategy();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        entityNumber = 20;
        prototypeEntity = null;
        dataset = new StandardDataTable();
    }
    
    public DataDependantPopulationInitializationStrategy(DataDependantPopulationInitializationStrategy copy) {
        delegate = copy.delegate;
        tableBuilder = copy.tableBuilder;
        this.entityNumber = copy.entityNumber;
        this.prototypeEntity = copy.prototypeEntity.getClone();
        dataset = copy.dataset;
    }
    
    @Override
    public DataDependantPopulationInitializationStrategy<E> getClone() {
        return new DataDependantPopulationInitializationStrategy<E>(this);
    }

    @Override
    public void setEntityType(Entity entity) {
        this.prototypeEntity = entity;
    }

    @Override
    public net.sourceforge.cilib.entity.Entity getEntityType() {
       return prototypeEntity;
    }

    @Override
    public Iterable<E> initialise(OptimisationProblem problem) {
        if(((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyCandidate() instanceof StandardCentroidInitializationStrategy) {
            StandardCentroidInitializationStrategy strategy = (StandardCentroidInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyCandidate();
            strategy.setBounds(getBounds());
        } else{
            DataPatternInitializationStrategy strategy = (DataPatternInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyCandidate();
            strategy.setDataset(dataset);
            DataPatternInitializationStrategy strategy2 = (DataPatternInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyVelocity();
            strategy2.setDataset(dataset);
            DataPatternInitializationStrategy strategy3 = (DataPatternInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyBest();
            strategy3.setDataset(dataset);
        }
        
        delegate.setEntityType(prototypeEntity);
        return delegate.initialise(problem);
    }

    /**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     */
    @Override
    public int getEntityNumber() {
        return this.entityNumber;
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        this.entityNumber = entityNumber;
    }
    
    public void setDelegate(PopulationInitialisationStrategy newDelegate) {
        delegate = newDelegate;
    }
    
    public PopulationInitialisationStrategy getDellegate() {
        return delegate;
    }
    
    public void setDataTableBuilder(DataTableBuilder builder) {
        tableBuilder = builder;
    }
    
    public DataTableBuilder getDataTableBuilder() {
        return tableBuilder;
    }
    
    public ArrayList<ControlParameter[]> getBounds() {
//        tableBuilder = new DataTableBuilder(tableBuilder.getDataReader());
//        tableBuilder.addDataOperator(new TypeConversionOperator());
//        tableBuilder.addDataOperator(new PatternConversionOperator());
//        try {
//            tableBuilder.buildDataTable();
//            
//        } catch (CIlibIOException ex) {
//            Logger.getLogger(DataClusteringPSO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        dataset = tableBuilder.getDataTable();

        ArrayList<ControlParameter[]> bounds  = new ArrayList<ControlParameter[]>();
        
        int size = ((StandardPattern) dataset.getRow(0)).getVector().size();
        for(int j = 0; j < size; j++) {
            double minValue = Double.POSITIVE_INFINITY;
            double maxValue = Double.NEGATIVE_INFINITY;
            for(int i = 0; i < dataset.size(); i++) {
                Vector row = ((StandardPattern) dataset.getRow(i)).getVector();
                if(row.get(j).doubleValue() > maxValue) {
                    maxValue = row.get(j).doubleValue();
                }
                
                if(row.get(j).doubleValue() < minValue) {
                    minValue = row.get(j).doubleValue();
                }
            }
            
            ControlParameter[] array = {ConstantControlParameter.of(minValue), ConstantControlParameter.of(maxValue)};
            bounds.add(array);
        }
        
        return bounds;
    }
    
    public void setDataset(DataTable table) {
        dataset = table;
    }
    
}
