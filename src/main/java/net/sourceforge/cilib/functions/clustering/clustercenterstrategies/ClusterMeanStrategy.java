/*
 * ClusterMeanStrategy.java
 *
 * Copyright (C) 2003 - 2008
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

import java.util.ArrayList;

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The <i>center of a cluster</i> is interpreted as the <i>mean of a cluster</i>.
 * @author Theuns Cloete
 */
public class ClusterMeanStrategy extends ClusterCenterStrategy {
	private static final long serialVersionUID = 9080168372118441393L;

	public ClusterMeanStrategy(ClusteringFitnessFunction cff) {
		super(cff);
	}

	/**
	 * In this case, we are interested in the mean of the cluster.
	 * @param i The integer representing the cluster for which the mean should be returned 
	 * @return the mean of cluster i
	 */
	@Override
	public Vector getCenter(int i) {
		ArrayList<Pattern> cluster = clusteringFitnessFunction.getArrangedClusters().get(i);

		if (cluster.isEmpty())
			throw new IllegalArgumentException("Cannot calculate the mean for an empty cluster");

		Vector mean = cluster.get(0).data.getClone();
		mean.reset();

		for (Pattern pattern : cluster) {
			mean = mean.plus(pattern.data);
		}
		return mean.divide(cluster.size());
	}
}
