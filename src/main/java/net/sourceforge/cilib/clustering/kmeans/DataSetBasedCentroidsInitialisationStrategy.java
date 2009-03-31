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
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.Pattern;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This strategy initializes the parts of the returned centroids {@link Vector} from
 * randomly chosen patterns in the dataset.
 *
 * @author Theuns Cloete
 */
public class DataSetBasedCentroidsInitialisationStrategy implements CentroidsInitialisationStrategy {
    private static final long serialVersionUID = -3016201656688883387L;

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSetBasedCentroidsInitialisationStrategy getClone() {
        return new DataSetBasedCentroidsInitialisationStrategy();
    }

    /**
     * Initialize the centroid vectors for a clustering from randomly chosen patterns in the
     * given dataset.
     *
     * @param problem the {@link ClusteringProblem} currently being optimized
     * @param dataset the {@link StaticDataSetBuilder} currently being clustered
     * @return an {@link ArrayList} of {@link Vector}s that represents all the centroids
     */
    @Override
    public ArrayList<Vector> initialise(ClusteringProblem problem, StaticDataSetBuilder dataset) {
        int numberOfCentroids = problem.getNumberOfClusters();
        ArrayList<Vector> centroids = new ArrayList<Vector>(numberOfCentroids);
        ArrayList<Pattern> patterns = dataset.getPatterns();
        RandomProvider random = new MersenneTwister();

        for (int i = 0; i < numberOfCentroids; ++i) {
            Vector centroid = patterns.get(Math.round(random.nextInt(patterns.size()))).data.getClone();

            centroids.add(centroid);
        }
        return centroids;
    }
}
