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
package net.sourceforge.cilib.functions.clustering.validityindices;

import java.util.ArrayList;

import net.sourceforge.cilib.functions.clustering.AverageCompactnessFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringErrorFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.MinimumSeparationFunction;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This is the Turi Validity Index as given in Section 3.1.4 on page 66 of Mahamed G. H. Omran's PhD thesis, titled
 * <tt>Particle Swarm Optimization Methods for Pattern Recognition and Image Processing</tt>,
 * November 2004
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCentroidStrategy}.
 * @author Theuns Cloete
 */
public class TuriIndex extends ClusteringErrorFunction {
    private static final long serialVersionUID = 2457356424874462741L;

//    private double c = 0.0;
//    private RandomNumber random = null;
//    private double gaussian = 0.0;
    private final ClusteringFunction averageCompactness;
    private final ClusteringFunction minimumSeparation;

    public TuriIndex() {
//        random = new RandomNumber();
//        gaussian = random.getGaussian(2, 1);
        this.averageCompactness = new AverageCompactnessFunction();
        this.minimumSeparation = new MinimumSeparationFunction();
    }

    @Override
    public Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
//        gaussian = random.getGaussian(2, 1);

        return /*(c * gaussian + 1) * */this.averageCompactness.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax) / this.minimumSeparation.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax);
    }

    @Override
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.clusterCenterStrategy = clusterCenterStrategy;
        this.averageCompactness.setClusterCenterStrategy(this.clusterCenterStrategy);
        this.minimumSeparation.setClusterCenterStrategy(this.clusterCenterStrategy);
    }

//    public void setC(double c) {
//        this.c = c;
//    }
}
