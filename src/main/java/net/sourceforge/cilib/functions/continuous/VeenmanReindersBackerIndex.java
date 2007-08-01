/*
 * VeenmanReindersBackerIndex.java
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
 * This is the Veenman-Reinders-Backer Validity Index as given in Section 1 of<br/>
 * @Article{ 628823, author = "Cor J. Veenman and Marcel J. T. Reinders and Eric Backer", title = "A
 *           Maximum Variance Cluster Algorithm", journal = "IEEE Transactions on Pattern Analysis
 *           and Machine Intelligence", volume = "24", number = "9", year = "2002", issn =
 *           "0162-8828", pages = "1273--1280", doi =
 *           "http://dx.doi.org/10.1109/TPAMI.2002.1033218", publisher = "IEEE Computer Society",
 *           address = "Washington, DC, USA", }
 * @author Theuns Cloete
 */
public class VeenmanReindersBackerIndex extends ClusteringFitnessFunction {
	private static final long serialVersionUID = 5683593481233814465L;
	/** The best value for the varianceLimit should be determined empirically; default is 1 */
	private double varianceLimit = 1.0;
//	boolean first = true;

	public VeenmanReindersBackerIndex() {
		super();
		varianceLimit = 1.0;
	}

	@Override
	public double calculateFitness() {
//		if (first) {
//			double variance = dataset.getVariance().norm();
//			System.out.println("variance: " + variance);
//			System.out.println("10% of variance: " + variance / 100);
//			first = false;
//		}

		if (!holdsConstraint())
			return Double.MAX_VALUE;

		double sumOfSquaredError = 0.0;

		for (ArrayList<Pattern> cluster : arrangedClusters) {
			Vector mean = dataset.getSetMean(cluster); // article explicitly uses the mean
			for (Pattern pattern : cluster) {
				for (int i = 0; i < mean.size(); i++) {
					sumOfSquaredError += Math.pow(pattern.data.getReal(i) - mean.getReal(i), 2);
				}
			}
		}
		return sumOfSquaredError /= dataset.getNumberOfPatterns();
	}

	private boolean holdsConstraint() {
		for (int i = 0; i < clustersFormed - 1; i++) {
			for (int j = i + 1; j < clustersFormed; j++) {
				ArrayList<Pattern> union = new ArrayList<Pattern>();
				union.addAll(arrangedClusters.get(i));
				union.addAll(arrangedClusters.get(j));

				if (dataset.getSetVariance(union).norm() < varianceLimit) {
					return false;
				}
			}
		}
		return true;
	}

	public void setVarianceLimit(double vl) {
		varianceLimit = vl;
	}
}
