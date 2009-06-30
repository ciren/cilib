/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.functions.clustering.validityindices;

import java.util.Collection;

import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterMeanStrategy;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the Dunn Index 33.
 *
 * Due to Equations 22 and 28 in<br/>
 * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C. Bezdek and
 *           Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and Cybernetics, Part B:
 *           Cybernetics", pages = "301--315", volume = "28", number = "3", month = jun, year =
 *           "1998", issn = "1083-4419" }
 * NOTE: By default, the cluster center refers to the cluster mean. See {@link ClusterCenterStrategy}.
 * @author Theuns Cloete
 */
public class DunnIndex33 extends GeneralisedDunnIndex {
    private static final long serialVersionUID = -3307601269742583865L;

    public DunnIndex33() {
        super();
        clusterCenterStrategy = new ClusterMeanStrategy();
    }

    /**
     * This method implements Equation 28 in the above-mentioned article.
     */
    @Override
    protected double calculateWithinClusterScatter(int k) {
        double averageDistance = 0.0;
        Collection<Pattern> cluster = arrangedClusters.get(k).values();
        Vector center = clusterCenterStrategy.getCenter(k);

        for (Pattern pattern : cluster) {
            averageDistance += helper.calculateDistance(pattern.data, center);
        }
        return 2.0 * (averageDistance / cluster.size());
    }

    /**
     * This method implements Equation 22 in the above-mentioned article.
     */
    @Override
    protected double calculateBetweenClusterSeperation(int i, int j) {
        return calculateAverageSetDistance(i, j);
    }

    @Override
    public DunnIndex33 getClone() {
        return new DunnIndex33();
    }
}
