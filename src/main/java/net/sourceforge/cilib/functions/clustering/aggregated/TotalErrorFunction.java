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
package net.sourceforge.cilib.functions.clustering.aggregated;

import java.util.List;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.TotalCompactnessFunction;
import net.sourceforge.cilib.functions.clustering.TotalSeparationFunction;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * @author Theuns Cloete
 */
public class TotalErrorFunction implements ClusteringFunction<Double> {
    private static final long serialVersionUID = 3925553803564320678L;

    private final ClusteringFunction<Double> compactnessFunction;
    private final ClusteringFunction<Double> separationFunction;

    public TotalErrorFunction() {
        this.compactnessFunction = new TotalCompactnessFunction();
        this.separationFunction = new TotalSeparationFunction();
    }

    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        return this.compactnessFunction.apply(clusters, dataTable, distanceMeasure, clusterCenterStrategy, dataSetMean, dataSetVariance, zMax) - this.separationFunction.apply(clusters, dataTable, distanceMeasure, clusterCenterStrategy, dataSetMean, dataSetVariance, zMax);
    }
}
