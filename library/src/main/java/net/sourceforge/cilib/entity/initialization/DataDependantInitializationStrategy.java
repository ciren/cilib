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


public abstract class DataDependantInitializationStrategy <E extends Entity> implements InitializationStrategy<E>{
    protected DataTableBuilder tableBuilder;
    protected InitializationStrategy<E> initialisationStrategy;
    protected DataTable dataset;
    protected int windowSize;
    protected ArrayList<ControlParameter[]> bounds;
    
    public DataDependantInitializationStrategy() {
        initialisationStrategy = new RandomBoundedInitializationStrategy<E>();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        dataset = new StandardDataTable();
        windowSize = 0;
        bounds = new ArrayList<ControlParameter[]>();
    }
    
    public DataDependantInitializationStrategy(DataDependantInitializationStrategy copy) {
        initialisationStrategy = copy.initialisationStrategy;
        tableBuilder = copy.tableBuilder;
        dataset = copy.dataset;
        windowSize = copy.windowSize;
        bounds = copy.bounds;
    }
    
    @Override
    public abstract void initialize(Enum<?> key, E entity);
    
    public void setBounds(ArrayList<ControlParameter[]> newBounds) {
        bounds = newBounds;
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
