/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.StandardDataTable;


public abstract class DataDependantInitialisationStrategy <E extends Entity> implements InitialisationStrategy<E>{
    protected DataTableBuilder tableBuilder;
    protected InitialisationStrategy<E> initialisationStrategy;
    protected DataTable dataset;
    protected int windowSize;
    protected ArrayList<ControlParameter[]> bounds;

    public DataDependantInitialisationStrategy() {
        initialisationStrategy = new RandomBoundedInitialisationStrategy<E>();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        dataset = new StandardDataTable();
        windowSize = 0;
        bounds = new ArrayList<ControlParameter[]>();
    }

    public DataDependantInitialisationStrategy(DataDependantInitialisationStrategy copy) {
        initialisationStrategy = copy.initialisationStrategy;
        tableBuilder = copy.tableBuilder;
        dataset = copy.dataset;
        windowSize = copy.windowSize;
        bounds = copy.bounds;
    }

    @Override
    public abstract void initialise(Enum<?> key, E entity);

    public void setBounds(ArrayList<ControlParameter[]> newBounds) {
        bounds = newBounds;
    }

    public void setInitialisationStrategy(InitialisationStrategy strategy) {
        initialisationStrategy = strategy;
    }

    public InitialisationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }

    public void setDataTableBuilder(DataTableBuilder builder) {
        tableBuilder = builder;
    }

    public DataTableBuilder getDataTableBuilder() {
        return tableBuilder;
    }

}
