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
package net.sourceforge.cilib.functions.clustering;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the k-harmonic means clustering fitness function.
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 */
public class KHarmonicMeansFunction extends ClusteringFitnessFunction {
    private static final long serialVersionUID = 2680037315045146954L;

    public KHarmonicMeansFunction() {
        super();
    }

    @Override
    public double calculateFitness() {
        double harmonicMean = 0.0;

        for (Pattern pattern : helper.getPatternsInDataSet()) {
            double sumOfReciprocals = 0.0;

            for (int i = 0; i < arrangedClusters.size(); i++) {
                Vector center = clusterCenterStrategy.getCenter(i);
                sumOfReciprocals += 1.0 / Math.max(helper.calculateDistance(pattern.data, center), Double.MIN_VALUE);        // if the distance == 0.0, use a very small value
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
