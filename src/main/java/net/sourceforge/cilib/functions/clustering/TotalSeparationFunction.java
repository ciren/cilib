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

import java.util.ArrayList;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Calculate the total separation between all the given clusters. In other words, the sum of all the distances
 * between the centers of all the clusters.
 *
 * @author Theuns Cloete
 */
public class TotalSeparationFunction extends ClusteringErrorFunction {
    private static final long serialVersionUID = 8755897695568016001L;

    /**
     * Calculate the total separation between all the given clusters. In other words, the sum of all the distances
     * between the centers of all the clusters.
     *
     * @return the total separation of the given clusters
     */
    @Override
    public Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        int clustersFormed = clusters.size();
        double separation = 0.0;

        for (int i = 0; i < clustersFormed - 1; ++i) {
            Vector leftCenter = this.clusterCenterStrategy.getCenter(clusters.get(i));

            for (int j = i + 1; j < clustersFormed; ++j) {
                Vector rightCenter = this.clusterCenterStrategy.getCenter(clusters.get(j));
                separation += distanceMeasure.distance(leftCenter, rightCenter);
            }
        }
        return separation;
    }
}
