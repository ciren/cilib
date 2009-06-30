/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.clustering.kmeans;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
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
    public RandomCentroidsInitialisationStrategy getClone() {
        return new RandomCentroidsInitialisationStrategy();
    }

    /**
     * Initialize the centroid vectors for a clustering to random positions in the search
     * space. The built-representation of the domain of the given problem is used to build a
     * {@link Vector} that will house the centroids.
     *
     * @param problem the {@link ClusteringProblem} currently being optimized
     * @param dataset the {@link ClusterableDataSet} currently being clustered
     * @return a {@link Vector} that represents all the centroids
     */
    public Vector initialise(ClusteringProblem problem, ClusterableDataSet dataset) {
        Vector centroids = (Vector) problem.getDomain().getBuiltRepresenation().getClone();

        centroids.randomize(new MersenneTwister());
        return centroids;
    }
}
