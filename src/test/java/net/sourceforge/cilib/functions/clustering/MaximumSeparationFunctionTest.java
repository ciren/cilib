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

import java.util.List;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.util.DistanceMeasure;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * @author Theuns Cloete
 */
public class MaximumSeparationFunctionTest {

    @Test
    public void testApply() {
        ClusteringFunction<Double> function = new MaximumSeparationFunction();
        DistanceMeasure distanceMeasure = ClusteringFunctionTests.getDistanceMeasure();
        DataTable<StandardPattern, TypeList> dataTable = ClusteringFunctionTests.getDataTable();
        List<Cluster> clusters = ClusteringFunctionTests.getClusters();

        assertThat(function.apply(clusters, dataTable, distanceMeasure, new ClusterCentroidStrategy(), null, 0.0, 0.0), closeTo(14.142135623731, ClusteringFunctionTests.EPSILON));
    }
}
