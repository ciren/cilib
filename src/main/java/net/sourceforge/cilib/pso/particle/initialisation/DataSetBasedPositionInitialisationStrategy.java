/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso.particle.initialisation;

import net.sourceforge.cilib.clustering.kmeans.CentroidsInitialisationStrategy;
import net.sourceforge.cilib.clustering.kmeans.DataSetBasedCentroidsInitialisationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;

/**
 * This strategy initializes the position as well as the best position of a {@link Particle}
 * using the {@link DataSetBasedCentroidsInitialisationStrategy}. The particle is therefore
 * initialized from the current dataset. The {@link ClusterableDataSet dataset} is found
 * using the {@link ClusteringUtils#getClusterableDataSet()} method. The
 * {@link ClusteringProblem} is also found using the
 * {@link ClusteringUtils#getClusteringProblem()} method.
 *
 * @author Theuns Cloete
 */
public class DataSetBasedPositionInitialisationStrategy implements PositionInitialisationStrategy {
    private static final long serialVersionUID = 1341622520702058537L;

    private CentroidsInitialisationStrategy centroidsInitialisationStrategy = null;

    public DataSetBasedPositionInitialisationStrategy() {
        centroidsInitialisationStrategy = new DataSetBasedCentroidsInitialisationStrategy();
    }

    public DataSetBasedPositionInitialisationStrategy getClone() {
        return new DataSetBasedPositionInitialisationStrategy();
    }

    /**
     * Initialize the position and best position of the given {@link Particle} from the
     * current dataset using the {@link DataSetBasedCentroidsInitialisationStrategy}.
     *
     * @param particle the {@link Particle} that should be initialized
     * @param problem the {@link OptimisationProblem} that is currently being optimized. This
     *        should be a {@link ClusteringProblem}, but is ignored, because the clustering
     *        problem is found via the {@link ClusteringUtils#getClusteringProblem()} method.
     */
    public void initialise(Particle particle, OptimisationProblem problem) {
        ClusteringUtils helper = ClusteringUtils.get();
        Vector centroids = centroidsInitialisationStrategy.initialise(helper.getClusteringProblem(), helper.getClusterableDataSet());

        particle.setCandidateSolution(centroids);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, centroids.getClone());
    }
}
