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

import java.util.ArrayList;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This strategy initializes the centroids of a clustering to random positions in the search
 * space.
 *
 * @author Theuns Cloete
 */
public class RandomCentroidsInitialisationStrategy implements CentroidsInitialisationStrategy {
    private static final long serialVersionUID = -7509467292180867974L;

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomCentroidsInitialisationStrategy getClone() {
        return new RandomCentroidsInitialisationStrategy();
    }

    /**
     * Initialize the centroid vectors for a clustering to random positions in the search
     * space. The built-representation of the behavioural domain of the given {@link ClusteringProblem} is used to build a
     * {@link Vector} that will house the centroids.
     *
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Vector> initialise(ClusteringProblem problem, StaticDataSetBuilder dataset) {
        int numberOfCentroids = problem.getNumberOfClusters();
        Vector centroids = (Vector) problem.getDomain().getBuiltRepresenation().getClone();

        centroids.randomize(new MersenneTwister());
        return ClusteringFunctions.disassembleCentroids(centroids, numberOfCentroids);
    }

    /**
     * Just randomize the centroid vector.
     * {@inheritDoc}
     */
    @Override
    public Vector reinitialise(ArrayList<Vector> centroids, int which) {
        Vector reinitialised = centroids.get(which);

        reinitialised.randomize(new MersenneTwister());

        return reinitialised;
    }
}
