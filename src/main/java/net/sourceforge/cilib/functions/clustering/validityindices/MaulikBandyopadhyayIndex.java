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

import com.google.common.base.Preconditions;

import java.util.List;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.MaximumSeparationFunction;
import net.sourceforge.cilib.functions.clustering.TotalCompactnessFunction;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * The MaulikBandyopadhyayIndex is the I(K) Validity Index as given in Equation 13 in Section IV on page 124 of:<br/>
 * @Article{ 923275, title = "Nonparametric Genetic Clustering: Comparison of Validity Indices",
 *           author = "Ujjwal Maulik and Sanghamitra Bandyopadhyay", journal = "IEEE Transactions on
 *           Systems, Man, and Cybernetics, Part C: Applications and Reviews", pages = "120--125",
 *           volume = "31", number = "1", month = feb, year = "2001", issn = "1094-6977", }
 * NOTE: I(K) isn't really a name, so I'm calling it the Maulik-Bandyopadhyay Validity Index
 * @author Theuns Cloete
 */
public class MaulikBandyopadhyayIndex implements ClusteringFunction<Double> {
    private static final long serialVersionUID = -1094819834873604274L;
    private static final int DEF_P = 1;

    private final ClusteringFunction<Double> totalCompactness;
    private final ClusteringFunction<Double> maximumSeparation;
    private int p;

    public MaulikBandyopadhyayIndex() {
        this.totalCompactness = new TotalCompactnessFunction();
        this.maximumSeparation = new MaximumSeparationFunction();
        this.p = DEF_P;
    }

    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        return Math.pow(this.termOne(clusters.size()) * this.termTwo(clusters, dataTable, distanceMeasure, clusterCenterStrategy, dataSetMean, dataSetVariance, zMax) * this.termThree(clusters, dataTable, distanceMeasure, clusterCenterStrategy, dataSetMean, dataSetVariance, zMax), this.p);
    }

    private double termOne(int clustersFormed) {
        return 1.0 / clustersFormed;
    }

    /**
     * E_1 refers to the intra-cluster distance when the dataset is clustered using only one cluster. In this case, the
     * data set mean can be thought of as the data set's centroid.
     */
    private double termTwo(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        // This is the normalizing factor, E_1 which they talk about in the article.
        double intraDatasetDistance = 0.0;


        for (StandardPattern pattern : dataTable) {
            intraDatasetDistance += distanceMeasure.distance(pattern.getVector(), dataSetMean);
        }

        return intraDatasetDistance / this.totalCompactness.apply(clusters, dataTable, distanceMeasure, clusterCenterStrategy, dataSetMean, dataSetVariance, zMax);
    }

    private double termThree(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        return this.maximumSeparation.apply(clusters, dataTable, distanceMeasure, clusterCenterStrategy, dataSetMean, dataSetVariance, zMax);
    }

    public void setP(int p) {
        Preconditions.checkArgument(p >= 1, "The p-value cannot be < 1");

        this.p = p;
    }
}
