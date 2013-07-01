/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.entity.initialisation;

import net.cilib.entity.Entity;
import net.cilib.io.ARFFFileReader;
import net.cilib.io.DataTable;
import net.cilib.io.DataTableBuilder;
import net.cilib.io.StandardDataTable;
import net.cilib.io.pattern.StandardPattern;
import net.cilib.io.transform.PatternConversionOperator;
import net.cilib.math.random.ProbabilityDistributionFunction;
import net.cilib.math.random.UniformDistribution;
import net.cilib.type.types.container.CentroidHolder;
import net.cilib.type.types.container.ClusterCentroid;

/**
 * This class initialises a ClusterParticle to contain a CentroidHolder as the candidate solution, velocity and best position
 * It initialises ClusterCentroids to the positions of existing data patterns
 */
public class DataPatternInitialisationStrategy <E extends Entity> extends DataDependantInitialisationStrategy<E> {
    private ProbabilityDistributionFunction random;
    private PatternConversionOperator patternConversionOperator;

    /*
     * Default constructor for the DataPatternInitialisationStrategy
     */
    public DataPatternInitialisationStrategy() {
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        random = new UniformDistribution();
        dataset = new StandardDataTable();
        patternConversionOperator = new PatternConversionOperator();
    }

    /*
     * Copy constructor for the DataPatternInitialisationStrategy
     * @param copy The DataPatternInitialisationStrategy to be copied
     */
    public DataPatternInitialisationStrategy(DataPatternInitialisationStrategy copy) {
        tableBuilder = copy.tableBuilder;
        random = copy.random;
        dataset = copy.dataset;
        patternConversionOperator = copy.patternConversionOperator;
    }

    /*
     * The clone method of the DataPatternInitialisationStrategy
     */
    @Override
    public InitialisationStrategy getClone() {
        return new DataPatternInitialisationStrategy(this);
    }

    /*
     * Initialises the entity's centroids to the positions of existing data patterns
     * @param key The key stating which property of the entity must be initialised
     * @param entity The entity to be initialised
     */
    @Override
    public void initialise(Enum<?> key, E entity) {
        int index = (int) random.getRandomNumber(0, dataset.size());
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid;

        for(int i=0; i< ((CentroidHolder) entity.getProperties().get(key)).size(); i++) {
            centroid = new ClusterCentroid();
            centroid.copy(((StandardPattern) dataset.getRow(index)).getVector());
            holder.add(centroid);
        }

        entity.getProperties().put(key, holder);
    }

    /*
     * Sets the dataset that will be used to initialise the entity
     * @param table The dataset that will be used to initialise the entity
     */
    public void setDataset(DataTable table) {
        dataset = table;
    }

    /*
     * Returns the dataset that was used to initialise the entity
     * @return dataset The dataset that was used to initialise the entity
     */
    public DataTable getDataset() {
        return dataset;
    }
}
