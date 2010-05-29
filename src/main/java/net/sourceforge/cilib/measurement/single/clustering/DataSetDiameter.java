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

import java.util.Set;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.clustering.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Calculates the data set diameter as the maximum distance between any two patterns in the data set.
 *
 * @author Theuns Cloete
 */
public class DataSetDiameter implements Measurement {
    private static final long serialVersionUID = -2057778074502089769L;

    private Real diameter = null;

    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "R";
    }

    /**
     * TODO: When we start using Guice, this method should be refactored
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        // we only have to calculate it once as long as the data set remains static/unchanged
        if (diameter == null) {
            ClusteringProblem problem = (ClusteringProblem) algorithm.getOptimisationProblem();
            StaticDataSetBuilder dataSetBuilder = (StaticDataSetBuilder) problem.getDataSetBuilder();
            Set<Pattern<Vector>> patterns = dataSetBuilder.getPatterns();
            Pattern<Vector>[] array = patterns.toArray(new Pattern[] {});
            DistanceMeasure distanceMeasure = problem.getDistanceMeasure();
            int numPatterns = patterns.size();
            double maxDistance = 0.0;

            for (int y = 0; y < numPatterns - 1; ++y) {
                for (int x = y + 1; x < numPatterns; ++x) {
                    maxDistance = Math.max(maxDistance, distanceMeasure.distance(array[x].getData(), array[y].getData()));
                }
            }
            this.diameter = Real.valueOf(maxDistance);
        }
        return this.diameter;
    }
}
