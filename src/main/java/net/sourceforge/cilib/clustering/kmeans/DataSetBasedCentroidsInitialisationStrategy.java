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
package net.sourceforge.cilib.clustering.kmeans;

import com.google.common.collect.Lists;

import java.util.List;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.problem.clustering.ClusteringProblem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This strategy initializes the parts of the returned centroids {@link Vector} from randomly chosen patterns in the
 * data set.
 *
 * @author Theuns Cloete
 */
public class DataSetBasedCentroidsInitialisationStrategy implements CentroidsInitialisationStrategy {
    private static final long serialVersionUID = -3016201656688883387L;

    private RandomProvider randomProvider;

    public DataSetBasedCentroidsInitialisationStrategy() {
        this.randomProvider = new MersenneTwister();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSetBasedCentroidsInitialisationStrategy getClone() {
        return new DataSetBasedCentroidsInitialisationStrategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vector> initialise(DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int numberOfCentroids) {
        List<Vector> centroids = Lists.newArrayList();

        for (int i = 0; i < numberOfCentroids; ++i) {
            Vector centroid = Vector.copyOf(dataTable.getRow(this.randomProvider.nextInt(dataTable.size())).getVector());

            centroids.add(centroid);
        }
        return centroids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector reinitialise(List<Vector> centroids, DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int which) {
        Vector reinitialised = Vector.copyOf(dataTable.getRow(this.randomProvider.nextInt(dataTable.size())).getVector());

        centroids.set(which, reinitialised);
        return reinitialised;
    }

    /**
     * Specify the {@link RandomProvider} that should be used to randomize the centroids&apos; features.
     * @param randomProvider the {@link RandomProvider} to use
     */
    public void setRandomProvider(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    /**
     * Retrieve the {@link RandomProvider} that will be used to randomize the centroids&apos; features.
     * @return the {@link RandomProvider} that will be used
     */
    public RandomProvider getRandomProvider() {
        return this.randomProvider;
    }
}
