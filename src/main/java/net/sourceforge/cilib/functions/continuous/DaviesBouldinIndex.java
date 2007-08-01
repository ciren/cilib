/*
 * DaviesBouldinIndex.java
 * 
 * Created on July 18, 2007
 *
 * Copyright (C) 2003 - 2007
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
package net.sourceforge.cilib.functions.continuous;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the Davies-Bouldin Validity Index as given in
 * @Article{ daviesbouldin1979vi, title = "A Cluster Seperation Measure", author = "David L. Davies
 *           and Donald W. Bouldin", journal = "IEEE Transactions on Pattern Analysis and Machine
 *           Intelligence", volume = "1", number = "2", year = "1979", pages = "224--227", month =
 *           apr, issn = "0162-8828" }
 * @author Theuns Cloete
 */
public class DaviesBouldinIndex extends ScatterSeperationRatio {
	private static final long serialVersionUID = -5167494843653998358L;
	private int q = 2;

	public DaviesBouldinIndex() {
		super();
	}

	@Override
	public double calculateFitness() {
		double db = 0.0, max = -Double.MAX_VALUE;

		cacheWithinClusterScatter();
		cacheBetweenClusterSeperation();

		for (int i = 0; i < clustersFormed; i++) {
			double withinScatterLeft = getWithinClusterScatter(i);
			for (int j = 0; j < clustersFormed; j++) {
				if (i != j) {
					double withinScatterRight = getWithinClusterScatter(j);
					double betweenSeperation = getBetweenClusterSeperation(i, j);
					max = Math.max(max, (withinScatterLeft + withinScatterRight) / betweenSeperation);
				}
			}
			db += max;
			max = -Double.MAX_VALUE;
		}
		return db / clustersFormed;
	}

	@Override
	protected double calculateWithinClusterScatter(int k) {
		double withinClusterScatter = 0.0;
		ArrayList<Pattern> cluster = arrangedClusters.get(k);
		Vector centroid = arrangedCentroids.get(k);

		for (Pattern pattern : cluster) {
			for (int i = 0; i < centroid.size(); i++) {
				withinClusterScatter += Math.pow(Math.abs(pattern.data.getReal(i) - centroid.getReal(i)), q);
			}
		}
		withinClusterScatter /= cluster.size();
		return Math.pow(withinClusterScatter, 1.0 / q);
	}

	@Override
	protected double calculateBetweenClusterSeperation(int i, int j) {
		return dataset.calculateDistance(arrangedCentroids.get(i), arrangedCentroids.get(j));
	}
}
