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
package net.sourceforge.cilib.functions.clustering.clustercenterstrategies;

import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The <i>center of the cluster</i> is interpreted as the <i>centroid of the cluster</i>.
 *
 * @author Theuns Cloete
 */
public class ClusterCentroidStrategy implements ClusterCenterStrategy {
    private static final long serialVersionUID = -7831635507079248268L;

    public ClusterCentroidStrategy() {
    }

    /**
     * In this case, we are interested in the centroid of the cluster.
     * @param cluster the cluster whose center should be returned
     * @return the centroid of the provided {@link Cluster}
     */
    @Override
    public Vector getCenter(Cluster cluster) {
        return cluster.getCentroid();
    }
}
