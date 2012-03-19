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

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;

/**
 * The ScatterSeperationRatio class was created due to the fact that some validity indices make use
 * of the cluster scatter and cluster seperation concepts. This class caches these values and
 * defines abstract methods (calculateWithinClusterScatter and calculateBetweenClusterSeperation)
 * which sub-classes have to implement to specify how their specific values are calculated.
 */
public abstract class ScatterSeperationRatio extends ClusteringFitnessFunction {
    private static final long serialVersionUID = 6758442782079174817L;

    protected ArrayList<Double> withinClusterScatterCache = null;
    protected ArrayList<Double> betweenClusterSeperationCache = null;

    protected void cacheWithinClusterScatter() {
        withinClusterScatterCache = new ArrayList<Double>();

        for (int i = 0; i < clustersFormed; i++) {
            withinClusterScatterCache.add(calculateWithinClusterScatter(i));
        }
    }

    protected abstract double calculateWithinClusterScatter(int k);

    protected double getWithinClusterScatter(int i) {
        return withinClusterScatterCache.get(i);
    }

    protected void cacheBetweenClusterSeperation() {
        betweenClusterSeperationCache = new ArrayList<Double>();

        for(int i = 0; i < clustersFormed - 1; i++) {
            for(int j = i + 1; j < clustersFormed; j++) {
                betweenClusterSeperationCache.add(calculateBetweenClusterSeperation(i, j));
            }
        }
    }

    protected abstract double calculateBetweenClusterSeperation(int i, int j);

    protected double getBetweenClusterSeperation(int i, int j) {
        if (i < 0 || j < 0)
            throw new IllegalArgumentException("The seperation between clusters " + i + " and " + j + " does not exist");

        if (i == j)
            return 0.0;

        if (j > i) {    // swap the i and j values
            int tmp = i;
            i = j;
            j = tmp;
        }
        return betweenClusterSeperationCache.get(i + (clustersFormed * j) - (((j + 1) * (j + 2)) / 2));
    }
}
