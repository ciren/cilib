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

import java.util.ArrayList;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This strategy initializes the centroids of a clustering to random positions in the search space.
 *
 * @author Theuns Cloete
 */
public class RandomCentroidsInitialisationStrategy implements CentroidsInitialisationStrategy {
    private static final long serialVersionUID = -7509467292180867974L;

    private RandomProvider randomProvider;

    public RandomCentroidsInitialisationStrategy() {
        this.randomProvider = new MersenneTwister();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomCentroidsInitialisationStrategy getClone() {
        return new RandomCentroidsInitialisationStrategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Vector> initialise(DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int numberOfCentroids) {
        ArrayList<Vector> centroids = Lists.newArrayList();
        Vector.Builder centroid = Vector.newBuilder().copyOf(domainRegistry.getBuiltRepresenation());

        for (int i = 0; i < numberOfCentroids; ++i) {
            // TODO: pass in RandomProvider
            centroids.add(centroid.buildRandom());
        }
        return centroids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector reinitialise(ArrayList<Vector> centroids, DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int which) {
        // TODO: pass in RandomProvider
        return Vector.newBuilder().copyOf(centroids.get(which)).buildRandom();
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
