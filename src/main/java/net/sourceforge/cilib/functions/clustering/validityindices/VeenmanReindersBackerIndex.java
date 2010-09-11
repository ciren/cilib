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

import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterMeanStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This is the Veenman-Reinders-Backer Validity Index. as given in Section 1 of:<br/>
 * @Article{ 628823, author = "Cor J. Veenman and Marcel J. T. Reinders and Eric Backer", title = "A
 *           Maximum Variance Cluster Algorithm", journal = "IEEE Transactions on Pattern Analysis
 *           and Machine Intelligence", volume = "24", number = "9", year = "2002", issn =
 *           "0162-8828", pages = "1273--1280", doi =
 *           "http://dx.doi.org/10.1109/TPAMI.2002.1033218", publisher = "IEEE Computer Society",
 *           address = "Washington, DC, USA", }
 * NOTE: The {@link ClusterMeanStrategy} should be used for this function to adhere to the implementation as specified
 * in the original paper.
 * @author Theuns Cloete
 */
public class VeenmanReindersBackerIndex implements ClusteringFunction<Double> {
    private static final long serialVersionUID = 5683593481233814465L;
    private static final double DEF_VARIANCE_LIMIT = 1.0;

    /** The best value for the varianceLimit should be determined empirically */
    private ControlParameter maximumVariance = null;

    public VeenmanReindersBackerIndex() {
        this.maximumVariance = new ConstantControlParameter(DEF_VARIANCE_LIMIT);    // default variance limit is 1.0
    }

    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        if (!this.holdsConstraint(dataSetMean, clusters)) {
            return Double.MAX_VALUE;
        }

        double sumOfSquaredError = 0.0;

        for (Cluster cluster : clusters) {
            Vector center = clusterCenterStrategy.getCenter(cluster);

            // H(Y) in the paper refers to the homogeneity of Y (not variance, because we do not divide by |Y|)
            for (StandardPattern pattern : cluster) {
                double error = distanceMeasure.distance(pattern.getVector(), center);

                sumOfSquaredError += error * error;
            }
        }
        return sumOfSquaredError / dataTable.size();
    }

    private boolean holdsConstraint(Vector dataSetMean, List<Cluster> clusters) {
        int clustersFormed = clusters.size();

        for (int i = 0; i < clustersFormed - 1; ++i) {
            for (int j = i + 1; j < clustersFormed; ++j) {
                Cluster union = new Cluster();

                union.addAll(clusters.get(i));
                union.addAll(clusters.get(j));

                if (union.getVariance(dataSetMean) < this.getMaximumVariance()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setMaximumVariance(ControlParameter maximumVariance) {
        this.maximumVariance = maximumVariance;
    }

    private double getMaximumVariance() {
        return this.maximumVariance.getParameter();
    }

    public void updateControlParameters() {
        this.maximumVariance.updateParameter();
    }
}
