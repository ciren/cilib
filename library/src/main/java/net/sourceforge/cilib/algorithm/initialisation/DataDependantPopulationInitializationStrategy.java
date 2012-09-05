/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class handles a dataset in order to initialize the population of the algorithm
 * using this dataset depending on which initializationStrategy is being used.
 * It sets the bounds of the StandardCentroidInitializationStrategy and the dataset of the
 * DataPatternInitializationStrategy and then initializes the population accordingly.
 */
public class DataDependantPopulationInitializationStrategy <E extends Entity> implements PopulationInitialisationStrategy<E>{
    private PopulationInitialisationStrategy delegate;
    private Entity prototypeEntity;
    private int entityNumber;
    private DataTableBuilder tableBuilder;
    DataTable dataset;

    /*
     * Default constructor for DataDependantPopulationInitializationStrategy
     */
    public DataDependantPopulationInitializationStrategy() {
        delegate = new ClonedPopulationInitialisationStrategy();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        entityNumber = 20;
        prototypeEntity = null;
        dataset = new StandardDataTable();
    }

    /*
     * Copy constructor for DataDependantPopulationInitializationStrategy
     * @param copy The DataDependantPopulationInitializationStrategy to be copied
     */
    public DataDependantPopulationInitializationStrategy(DataDependantPopulationInitializationStrategy copy) {
        delegate = copy.delegate;
        tableBuilder = copy.tableBuilder;
        this.entityNumber = copy.entityNumber;
        this.prototypeEntity = copy.prototypeEntity.getClone();
        dataset = copy.dataset;
    }

    /*
     * Clone method of the DataDependantPopulationInitializationStrategy
     * @return the new instance of the DataDependantPopulationInitializationStrategy
     */
    @Override
    public DataDependantPopulationInitializationStrategy<E> getClone() {
        return new DataDependantPopulationInitializationStrategy<E>(this);
    }

    /*
     * Sets the value of the prototypeEntity in order to inform the initialization
     * algorithm about what type of entity one wants to use.
     * @param entity The new prototype entity
     */
    @Override
    public void setEntityType(Entity entity) {
        this.prototypeEntity = entity;
    }

    /*
     * Returns the prototype entity
     * @return prototypeEntity The prototype entity
     */
    @Override
    public net.sourceforge.cilib.entity.Entity getEntityType() {
       return prototypeEntity;
    }

    /*
     * Sets the bounds of a StandardCentroidInitializationStrategy or the dataset of the
     * DataPatternInitializationStrategy and calls the initialise method of the delegate
     * strategy.
     * @param problem The optimization problem of the algorithm
     * @return The newly initialized population
     */
    @Override
    public Iterable<E> initialise(Problem problem) {
        if(((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyCandidate() instanceof StandardCentroidInitializationStrategy) {
            StandardCentroidInitializationStrategy strategy = (StandardCentroidInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyCandidate();
            strategy.setBounds(getBounds());
            StandardCentroidInitializationStrategy strategy2 = (StandardCentroidInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyVelocity();
            strategy2.setBounds(getBounds());
            StandardCentroidInitializationStrategy strategy3 = (StandardCentroidInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyBest();
            strategy3.setBounds(getBounds());
        } else{
            DataPatternInitializationStrategy strategy = (DataPatternInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyCandidate();
            strategy.setDataset(dataset);
            DataPatternInitializationStrategy strategy2 = (DataPatternInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyVelocity();
            strategy2.setDataset(dataset);
            DataPatternInitializationStrategy strategy3 = (DataPatternInitializationStrategy) ((ClusterParticle) prototypeEntity).getCentroidInitializationStrategyBest();
            strategy3.setDataset(dataset);
        }

        delegate.setEntityType(prototypeEntity);
        delegate.setEntityNumber(entityNumber);
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
     * Set the number of {@code Entity} instances to clone, i.e. the population size
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        this.entityNumber = entityNumber;
    }

    /*
     * Sets the delegate InitialzationStrategy to the one received as a parameter
     * @param delegate The new delegate InitialzationStrategy
     */
    public void setDelegate(PopulationInitialisationStrategy newDelegate) {
        delegate = newDelegate;
    }

    /*
     * Returns the delegate InitialzationStrategy
     * @return delegate The delegate InitialzationStrategy
     */
    public PopulationInitialisationStrategy getDelegate() {
        return delegate;
    }


    /*
     * Determines the upper and lower bounds for each dimension of the dataset and
     * returns an arraylist holding these.
     * @return bounds An Arraylist containing the upper and lower bound for each
     * dimension of a data pattern
     */
    public ArrayList<ControlParameter[]> getBounds() {
        ArrayList<ControlParameter[]> bounds  = new ArrayList<ControlParameter[]>();

        int size = ((StandardPattern) dataset.getRow(0)).getVector().size();
        double minValue;
        double maxValue;
        Vector row;
        
        for(int j = 0; j < size; j++) {
            minValue = Double.POSITIVE_INFINITY;
            maxValue = Double.NEGATIVE_INFINITY;
            for(int i = 0; i < dataset.size(); i++) {
                row = ((StandardPattern) dataset.getRow(i)).getVector();
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

    /*
     * Sets the dataset to be used to initialize the entities
     * @param table The dataset to be used
     */
    public void setDataset(DataTable table) {
        dataset = table;
    }

}
