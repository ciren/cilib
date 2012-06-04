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

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class CentroidInitializationStrategy <E extends Entity> implements InitializationStrategy<E>{

    private DataTableBuilder tableBuilder;
    private InitializationStrategy<E> initialisationStrategy;
    DataTable dataset;
    int windowSize;
    ArrayList<ControlParameter[]> bounds;
    
    public CentroidInitializationStrategy() {
        initialisationStrategy = new RandomInitializationStrategy<E>();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        dataset = new StandardDataTable();
        windowSize = 0;
        bounds = new ArrayList<ControlParameter[]>();
    }
    
    public CentroidInitializationStrategy(CentroidInitializationStrategy copy) {
        initialisationStrategy = copy.initialisationStrategy;
        tableBuilder = copy.tableBuilder;
        dataset = copy.dataset;
        windowSize = copy.windowSize;
        bounds = copy.bounds;
    }
    
    @Override
    public CentroidInitializationStrategy getClone() {
        return new CentroidInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, E entity) {
        CentroidHolder centroidHolder = (CentroidHolder) entity.getProperties().get(key);
        Entity particle;
        
        if(initialisationStrategy instanceof RandomBoundedInitializationStrategy) {
            //setBounds();
            ((RandomBoundedInitializationStrategy) initialisationStrategy).setBoundsPerDimension(bounds);
            
            for(ClusterCentroid centroid : centroidHolder) {
                int index = 0;
                for(Numeric n : centroid) {
                    Real r = Real.valueOf(n.doubleValue(), new Bounds(bounds.get(index)[0].getParameter(), bounds.get(index)[1].getParameter()));
                    n = r.getClone();
                    index++;
                }
            }
        }
        
        for(ClusterCentroid centroid : centroidHolder) {
            particle = new StandardParticle();
            
            particle.setCandidateSolution(centroid.toVector());
            
            initialisationStrategy.initialize(key, (E) particle);
            
            centroid.copy((Vector) particle.getCandidateSolution());
        }
        
        entity.getProperties().put(key, (Type) centroidHolder);
        
    }
    
    public void reinitialize(Enum<?> key, E entity) {
        CentroidHolder centroidHolder = (CentroidHolder) entity.getProperties().get(key);
        Entity particle;
        
        for(ClusterCentroid centroid : centroidHolder) {
            particle = new StandardParticle();
            particle.setCandidateSolution(centroid.toVector());
            
            initialisationStrategy.initialize(key, (E) particle);
            
            centroid.copy((Vector) particle.getCandidateSolution());
        }
        
        entity.getProperties().put(key, (Type) centroidHolder);
    }
    
    public void setBounds(ArrayList<ControlParameter[]> newBounds) {
        bounds = newBounds;
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
//
//        RandomBoundedInitializationStrategy copiedStrategy = (RandomBoundedInitializationStrategy) initialisationStrategy.getClone();
//        bounds  = new ArrayList<ControlParameter[]>();
//        
//        int size = ((StandardPattern) dataset.getRow(0)).getVector().size();
//        for(int j = 0; j < size; j++) {
//            double minValue = Double.POSITIVE_INFINITY;
//            double maxValue = Double.NEGATIVE_INFINITY;
//            for(int i = 0; i < dataset.size(); i++) {
//                Vector row = ((StandardPattern) dataset.getRow(i)).getVector();
//                if(row.get(j).doubleValue() > maxValue) {
//                    maxValue = row.get(j).doubleValue();
//                }
//                
//                if(row.get(j).doubleValue() < minValue) {
//                    minValue = row.get(j).doubleValue();
//                }
//            }
//            
//            ControlParameter[] array = {ConstantControlParameter.of(minValue), ConstantControlParameter.of(maxValue)};
//            bounds.add(array);
//        }
//        
//        copiedStrategy.setBoundsPerDimension(bounds);
//        initialisationStrategy = copiedStrategy;
    }
    
    public void setInitialisationStrategy(InitializationStrategy strategy) {
        initialisationStrategy = strategy;
    }
    
    public InitializationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }
    
    public void setDataTableBuilder(DataTableBuilder builder) {
        tableBuilder = builder;
    }
    
    public DataTableBuilder getDataTableBuilder() {
        return tableBuilder;
    }
    
    
}
