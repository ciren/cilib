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

import java.io.Serializable;

import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This strategy allows for different ways of initializing the centroids of a clustering.
 * The two main approaches are either:
 * <ul>
 * <li>Randomly ({@link RandomCentroidsInitialisationStrategy}); or</li>
 * <li>Based on random patterns chosen from the dataset ({@link DataSetBasedCentroidsInitialisationStrategy})</li>
 * </ul>
 *
 * @author Theuns Cloete
 */
public interface CentroidsInitialisationStrategy extends Serializable, Cloneable {

    /**
     * Initialize the centroid vectors for a clustering. Each centroid is appended to a
     * {@link Vector} that represents all the centroids. This {@link Vector} is then
     * returned. The problem and/or dataset that are currently being clustered can be used to
     * get information about the clustering, such as the dimension of the search space and
     * centroids.
     *
     * @param problem the {@link ClusteringProblem} currently being optimized
     * @param dataset the {@link ClusterableDataSet} currently being clustered
     * @return a {@link Vector} that represents all the centroids
     */
    public Vector initialise(ClusteringProblem problem, ClusterableDataSet dataset);

}
