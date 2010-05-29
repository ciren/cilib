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
import java.util.Set;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctionTests;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * @author Theuns Cloete
 */
public class HalkidiVazirgiannisIndexTest {

    @Test
    public void testApply() {
        ClusteringFunction function = new HalkidiVazirgiannisIndex();
        DistanceMeasure distanceMeasure = ClusteringFunctionTests.getDistanceMeasure();
        Set<Pattern<Vector>> patterns = ClusteringFunctionTests.getPatterns();
        ArrayList<Cluster<Vector>> clusters = ClusteringFunctionTests.getClusters();
        double dataSetVariance = ClusteringFunctionTests.getDataSetVariance();

        // stddev = 0.569289564320157
        // withinClusterScatter = 0.0547486720108598
        // betweenClusterSeparation = 0.0 (makes sense, because the clusters are not very dense)
        assertThat(function.apply(clusters, patterns, distanceMeasure, null, dataSetVariance, 0.0), closeTo(0.0547486720108598, ClusteringFunctionTests.EPSILON));
    }
}
