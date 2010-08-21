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

import java.io.Serializable;

import java.util.ArrayList;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.ClusteringProblem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This strategy allows for different ways of initializing the centroids of a clustering. It also allows for a specific
 * centroid to be {@link #reinitialise(java.util.ArrayList, int) reinitialised}.
 * The following approaches have already been implemented:
 * <ul>
 * <li>Randomly ({@link RandomCentroidsInitialisationStrategy}); or</li>
 * <li>Based on random patterns chosen from the dataset ({@link DataSetBasedCentroidsInitialisationStrategy})</li>
 * <li>Based on the contribution that each centroid contributes towards the overall potential
 * ({@link KMeansPlusPlusCentroidsInitialisationStrategy})</li>
 * </ul>
 *
 * @author Theuns Cloete
 */
public interface CentroidsInitialisationStrategy extends Serializable, Cloneable {
    @Override
    public CentroidsInitialisationStrategy getClone();

    /**
     * Initialise the centroid vectors for a clustering. Each centroid is individually initialised and then added to an
     * {@link ArrayList} that represents all the centroids. This structure is then returned. The problem and/or dataset
     * that are currently being clustered can be used to get information about the clustering, such as the dimension of
     * the search space and centroids.
     *
     * TODO: When we start using Guice, then only the required parameters have to be injected when the class is
     * instantiated and this method will not need all these parameters.
     *
     * @param problem the {@link ClusteringProblem} currently being optimized
     * @param dataset the {@link StaticDataSetBuilder} currently being clustered
     * @return an {@link ArrayList} of {@link Vector}s that represent all the centroids
     */
    public abstract ArrayList<Vector> initialise(DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int numberOfCentroids);

    /**
     * Reinitialise the specified centroid (residing in the given list of centroids) and return it.
     *
     * TODO: When we start using Guice, then only the required parameters have to be injected when the class is
     * instantiated and this method will not need all these parameters.
     *
     * @param centroids The list of centroid vectors containing the centroid that needs to be reinitialised.
     * @param which The index of the centroid that should be reinitialised.
     * @return the reinitialised centroid for convenience
     */
    public abstract Vector reinitialise(ArrayList<Vector> centroids, DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int which);
}
