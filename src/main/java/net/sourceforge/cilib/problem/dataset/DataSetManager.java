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
package net.sourceforge.cilib.problem.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import net.sourceforge.cilib.problem.Problem;

/**
 * This class is a Singleton and is responsible for managing all the {@link DataSet}s and
 * {@link DataSetBuilder}s that might be instantiated by a {@link Simulation},
 * {@link Problem} or {@link Thread}. It prevents the same dataset or dataset builder from
 * being parsed or initialised more than once. It achieves this by maintaining two
 * {@link Hashtable}s. The first, {@link #datasets} (with its <code>key</code> the
 * identifier of the dataset and its <code>value</code> an {@link ArrayList} of
 * {@link Pattern}s that have been returned by the {@link LocalDataSet#parseDataSet()}
 * method) makes sure that a specific dataset is parsed and instantiated only once. <br/>
 * The second, {@link #builders} (with its <code>key</code> the identifier of the dataset
 * builder and its <code>value</code> the {@link StaticDataSetBuilder} object)
 * makes sure that a specific dataset builder is built and initialised only once.<br/> The
 * concrete {@link LocalDataSet} is used, but only for now, because I didn't want to change
 * the more generic {@link DataSet}.<br/> The concrete {@link StaticDataSetBuilder} is
 * used, but only for now, because I didn't want to change the more generic
 * {@link DataSetBuilder}.
 */
public final class DataSetManager implements Serializable {
    private static final long serialVersionUID = 6735187580654161651L;

    private static volatile DataSetManager instance = null;
    private Hashtable<String, ArrayList<Pattern>> datasets = null;
    private Hashtable<String, StaticDataSetBuilder> builders = null;

    private DataSetManager() {
        datasets = new Hashtable<String, ArrayList<Pattern>>();
        builders = new Hashtable<String, StaticDataSetBuilder>();
    }

    public static synchronized DataSetManager getInstance() {
        if (instance == null) {
            instance = new DataSetManager();
        }
        return instance;
    }

    /**
     * Either parse and retrieve or just retrieve the list of patterns that represents the
     * requested dataset. The dataset's identifier (filename in the case of a
     * {@link LocalDataSet}) is used as the key into the {@link #datasets} {@link Hashtable}.
     *
     * @param dataset a {@link LocalDataSet} that may or may not have been
     *        parsed/instantiated before
     * @return an {@link ArrayList} of {@link Pattern}s representing the given dataset
     */
    public synchronized ArrayList<Pattern> getDataFromSet(DataSet dataset) {
        String identifier = dataset.getIdentifier();

        System.out.println("Requesting " + identifier);
        if (!datasets.containsKey(identifier)) {
            System.out.println("Parsing " + identifier);
            datasets.put(identifier, dataset.parseDataSet());
        }
        System.out.println("Returning " + identifier);
        return datasets.get(identifier);
    }

    /**
     * Either initialise and retrieve or just retrieve the object that represents the
     * requested built up dataset. The dataset builder's identifier is used as the key into
     * the {@link #builders} {@link Hashtable}.
     *
     * @param datasetBuilder an {@link StaticDataSetBuilder} that may or may not have
     *        been built/instantiated before
     * @return an {@link StaticDataSetBuilder} that represent the requested built up
     *         dataset
     */
    public synchronized StaticDataSetBuilder getDataSetBuilder(StaticDataSetBuilder datasetBuilder) {
        String identifier = datasetBuilder.getIdentifier();

        System.out.println("Requesting " + identifier);
        if (!builders.containsKey(identifier)) {
            datasetBuilder.initialise();
            builders.put(identifier, datasetBuilder);
        }
        System.out.println("Returning " + identifier);
        return builders.get(identifier);
    }
}
