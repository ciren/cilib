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
package net.sourceforge.cilib.measurement.single.clustering;

import java.util.ArrayList;
import java.util.Hashtable;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.dataset.Pattern;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;

/**
 * Combines and measures the standard deviation vectors of the clusters optimised by the given algorithm.
 *
 * @author Theuns Cloete
 */
public class ClusterStandardDeviations implements Measurement {
    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "(R^?)^?";
    }

    @Override
    public Type getValue(Algorithm algorithm) {
        ClusteringUtils helper = ClusteringUtils.get();
        ArrayList<Hashtable<Integer, Pattern>> clusters = helper.getArrangedClusters();
        ArrayList<Vector> centroids = helper.getArrangedCentroids();
        Vector.Builder combined = Vector.newBuilder();

        for (int i = 0; i < clusters.size(); ++i) {
            combined.copyOf(Stats.stdDeviationVector(clusters.get(i).values(), centroids.get(i)));
        }
        return combined.build();
    }
}
