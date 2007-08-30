/*
 * HalkidiVazirgiannisIndex.java
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
 * This is the Halkidi-Vazirgiannis Validity Index as given in
 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title = "Clustering
 *                 Validity Assessment: Finding the Optimal Partitioning of a Data Set", booktitle =
 *                 "Proceedings of the IEEE International Conference on Data Mining", year = "2001",
 *                 isbn = "0-7695-1119-8", pages = "187--194", publisher = "IEEE Computer Society",
 *                 address = "Washington, DC, USA", }
 */
public class HalkidiVazirgiannisIndex extends ClusteringFitnessFunction {
	private static final long serialVersionUID = 1164537525165848345L;
	private double stdev = 0.0;

	public HalkidiVazirgiannisIndex() {
		super();
	}

	@Override
	public double calculateFitness() {
		return calculateWithinClusterScatter() + calculateBetweenClusterSeperation();
	}

	protected double calculateWithinClusterScatter() {
		double scattering = 0.0;
		Vector clusterVariance = null;
		Vector datasetVariance = dataset.getVariance();

		stdev = 0.0;
		for (ArrayList<Pattern> cluster : arrangedClusters) {
			clusterVariance = dataset.getSetVariance(cluster);
			double norm = clusterVariance.norm();
			scattering += norm;
			stdev += norm;
		}
		stdev = Math.sqrt(stdev);
		stdev /= clustersFormed;
		scattering /= datasetVariance.norm();
		return scattering /= clustersFormed;
	}

	protected double calculateBetweenClusterSeperation() {
		Vector midPoint = null, leftCentroid = null, rightCentroid = null;
		double density = 0.0;
		int midDensity = 0, leftDensity = 0, rightDensity = 0;

		for (int i = 0; i < clustersFormed; i++) {
			leftCentroid = arrangedCentroids.get(i);
			for (int j = 0; j < clustersFormed; j++) {
				if (i != j) {
					rightCentroid = arrangedCentroids.get(j);
					midPoint = leftCentroid.plus(rightCentroid);
					midPoint = midPoint.divide(2.0);
					midDensity = leftDensity = rightDensity = 0;

					for (Pattern pattern : arrangedClusters.get(i)) {
						if (calculateDistance(pattern.data, midPoint) <= stdev)
							++midDensity;
						if (calculateDistance(pattern.data, leftCentroid) <= stdev)
							++leftDensity;
					}

					for (Pattern pattern : arrangedClusters.get(j)) {
						if (calculateDistance(pattern.data, midPoint) <= stdev)
							++midDensity;
						if (calculateDistance(pattern.data, rightCentroid) <= stdev)
							++rightDensity;
					}

					// prevent devision by zero (ArithmeticExceptions)
					// leftDensity + rightDensity == 0 can mean one of two things:
					// 1. both clusters didn't have any patterns in it or
					// 2. the distance between the pattern and midPoint was not > stdev (for both
					// clusters)
					if (leftDensity + rightDensity > 0.0) {
						density += midDensity / Math.max(leftDensity, rightDensity);
					}
				}
			}
		}
		return density /= (clustersFormed * (clustersFormed - 1));
	}
}
