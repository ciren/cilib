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
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.clustering.kmeans.CentroidsInitialisationStrategy;
import net.sourceforge.cilib.clustering.kmeans.DataSetBasedCentroidsInitialisationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.clustering.ClusteringProblem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This strategy initializes the position as well as the best position of a {@link Particle}
 * using the selected {@link CentroidsInitialisationStrategy}. The default is the {@link DataSetBasedCentroidsInitialisationStrategy}.
 * The particle is therefore initialized from the current dataset. The {@link StaticDataSetBuilder dataset} is found
 * using the {@link ClusteringUtils#getDataSetBuilder()} method. The
 * {@link ClusteringProblem} is also found using the {@link ClusteringUtils#getClusteringProblem()} method.
 *
 * @param <E> The type of {@code Entity}.
 * @author Theuns Cloete
 */
public class DataSetBasedInitializationStrategy<E extends Entity> implements InitializationStrategy<E> {
    private static final long serialVersionUID = 1341622520702058537L;

    private CentroidsInitialisationStrategy centroidsInitialisationStrategy;

    public DataSetBasedInitializationStrategy() {
        centroidsInitialisationStrategy = new DataSetBasedCentroidsInitialisationStrategy();
    }

    @Override
    public DataSetBasedInitializationStrategy getClone() {
        return new DataSetBasedInitializationStrategy();
    }

    /**
     * Initialize the position and best position of the given {@link Particle} from the
     * current dataset using the selected {@link CentroidsInitialisationStrategy}.
     *
     * TODO: When we start using Guice, this method should be refactored
     *
     * @param particle the {@link Particle} that should be initialized
     * @param problem the {@link OptimisationProblem} that is currently being optimized. This
     *        should be a {@link ClusteringProblem}, but is ignored, because the clustering
     *        problem is found via the {@link ClusteringUtils#getClusteringProblem()} method.
     */
    @Override
    public void initialize(Enum<?> key, E particle) {
        ClusteringProblem clusteringProblem = (ClusteringProblem) AbstractAlgorithm.getAlgorithmList().get(0).getOptimisationProblem();
        DataTable<StandardPattern, TypeList> dataTable = clusteringProblem.getDataTable();
        DomainRegistry standardDomain = clusteringProblem.getDomainRegistry();
        DistanceMeasure distanceMeasure = clusteringProblem.getDistanceMeasure();
        int numberOfClusters = clusteringProblem.getNumberOfClusters();
        Vector centroids = ClusteringFunctions.assembleCentroids(this.centroidsInitialisationStrategy.initialise(dataTable, standardDomain, distanceMeasure, numberOfClusters));

        particle.setCandidateSolution(centroids);
    }

    public void setCentroidsInitialisationStrategy(CentroidsInitialisationStrategy cis) {
        this.centroidsInitialisationStrategy = cis;
    }
}
