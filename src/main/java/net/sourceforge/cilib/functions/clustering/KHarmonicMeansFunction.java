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
package net.sourceforge.cilib.functions.clustering;

import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the k-harmonic means clustering fitness function.
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 */
public class KHarmonicMeansFunction extends ClusteringFitnessFunction {
    private static final long serialVersionUID = 2680037315045146954L;

    public KHarmonicMeansFunction() {
    }

    @Override
    public double calculateFitness() {
        double harmonicMean = 0.0;

        for (Pattern<Vector> pattern : ((StaticDataSetBuilder) this.problem.getDataSetBuilder()).getPatterns()) {
            double sumOfReciprocals = 0.0;

            for (Cluster<Vector> cluster : this.significantClusters) {
                Vector center = this.clusterCenterStrategy.getCenter(cluster);

                sumOfReciprocals += 1.0 / Math.max(this.problem.calculateDistance(pattern.getData(), center), Double.MIN_VALUE);        // if the distance == 0.0, use a very small value
            }
            harmonicMean += clustersFormed / sumOfReciprocals;
        }
        return harmonicMean;
    }

    @Override
    public KHarmonicMeansFunction getClone() {
        return new KHarmonicMeansFunction();
    }
}
