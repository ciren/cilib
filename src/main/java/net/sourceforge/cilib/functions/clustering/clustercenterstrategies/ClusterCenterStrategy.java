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

import java.io.Serializable;

import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A strategy to determine what is meant by the <i>center of a cluster</i>. Sometimes,
 * <i>cluster center</i> refers to a cluster's {@link ClusterCentroidStrategy centroid} and other times it
 * refers to a cluster's {@link ClusterMeanStrategy mean}.
 * @author Theuns Cloete
 */
public interface ClusterCenterStrategy extends Serializable {
    /**
     * Sub-classes should implement this method to return the desired center of the given cluster.
     * @param cluster the cluster whose center should be returned
     * @return a {@link Vector} representing the center of the given {@link Cluster}
     */
    Vector getCenter(Cluster cluster);
}
