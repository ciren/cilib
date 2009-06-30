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

import java.util.ArrayList;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This strategy initializes the parts of the returned centroids {@link Vector} from
 * randomly chosen patterns in the dataset.
 *
 * @author Theuns Cloete
 */
public class DataSetBasedCentroidsInitialisationStrategy implements CentroidsInitialisationStrategy {
    private static final long serialVersionUID = -3016201656688883387L;

    private Random random = null;

    /**
     * Create a new instance of {@linkplain DataSetBasedCentroidsInitialisationStrategy}.
     */
    public DataSetBasedCentroidsInitialisationStrategy() {
        random = new MersenneTwister();
    }

    /**
     * {@inheritDoc}
     */
    public DataSetBasedCentroidsInitialisationStrategy getClone() {
        return new DataSetBasedCentroidsInitialisationStrategy();
    }

    /**
     * Initialize the centroid vectors for a clustering from randomly chosen patterns in the
     * given dataset.
     *
     * @param problem the {@link ClusteringProblem} currently being optimized
     * @param dataset the {@link ClusterableDataSet} currently being clustered
     * @return a {@link Vector} that represents all the centroids
     */
    public Vector initialise(ClusteringProblem problem, ClusterableDataSet dataset) {
        ArrayList<Pattern> patterns = dataset.getPatterns();
        int numberOfCentroids = problem.getNumberOfClusters();
        Vector centroids = new Vector(problem.getDomain().getDimension());

        for (int i = 0; i < numberOfCentroids; i++) {
            Vector centroid = patterns.get((int) Math.round(random.nextInt(patterns.size()))).data;
            centroids.append(centroid.getClone());
        }
        return centroids;
    }
}
