/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
