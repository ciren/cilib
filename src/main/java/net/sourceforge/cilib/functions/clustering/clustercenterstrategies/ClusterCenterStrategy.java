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
package net.sourceforge.cilib.functions.clustering.clustercenterstrategies;

import java.io.Serializable;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A strategy to determine what is meant by the <i>center of a cluster</i>. Sometimes,
 * <i>cluster center</i> refers to a cluster's {@link ClusterCentroidStrategy centroid} and other times it
 * refers to a cluster's {@link ClusterMeanStrategy mean}.
 * @author Theuns Cloete
 */
public interface ClusterCenterStrategy extends Serializable {
    /**
     * Sub-classes should implement this method to return the desired center of cluster i.
     * @param i The integer representing the cluster for which a center should be returned
     * @return a {@link Vector} representing the center of cluster i
     */
    public Vector getCenter(int i);
}
