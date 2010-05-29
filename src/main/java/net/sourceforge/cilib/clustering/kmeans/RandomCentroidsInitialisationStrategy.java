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
import java.util.Set;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.Pattern;
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
    public ArrayList<Vector> initialise(Set<Pattern<Vector>> patterns, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int numberOfCentroids) {
        ArrayList<Vector> centroids = Lists.newArrayList();

        for (int i = 0; i < numberOfCentroids; ++i) {
            Vector centroid = (Vector) domainRegistry.getBuiltRepresenation().getClone();

            centroid.randomize(this.randomProvider);
            centroids.add(centroid);
        }
        return centroids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector reinitialise(ArrayList<Vector> centroids, Set<Pattern<Vector>> patterns, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int which) {
        Vector reinitialised = centroids.get(which);

        reinitialised.randomize(this.randomProvider);
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
