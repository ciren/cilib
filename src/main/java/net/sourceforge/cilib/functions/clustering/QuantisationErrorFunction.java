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

import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Calculate the Quantisation Error as illustrated in Section 4.1.1 on pages 104 & 105 of:<br/>
 *
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern
 *             Recognition and Image Processing", author = "Mahamed G.H. Omran",
 *             institution = "University Of Pretoria", school = "Computer Science", year =
 *             "2004", month = nov, address = "Pretoria, South Africa", note =
 *             "Supervisor: A. P. Engelbrecht", }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCentroidStrategy}.
 * @author Theuns Cloete
 */
public class QuantisationErrorFunction extends ClusteringErrorFunction {
    private static final long serialVersionUID = -7008338250315442786L;

    /**
     * Calculate the Quantisation error of the given clusters.
     * @return the Quantisation error of the given clusters.
     */
    @Override
    public Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        double quantisationError = 0.0;

        for (Cluster cluster : clusters) {
            double averageCompactness = 0.0;
            Vector center = this.clusterCenterStrategy.getCenter(cluster);

            for (StandardPattern pattern : cluster) {
                averageCompactness += distanceMeasure.distance(pattern.getVector(), center);
            }
            averageCompactness /= cluster.size();
            quantisationError += averageCompactness;
        }
        return quantisationError / clusters.size();
    }
}
