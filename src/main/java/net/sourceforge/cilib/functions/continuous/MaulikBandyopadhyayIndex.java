/*
 * MaulikBandyopadhyayIndex.java
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

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the I(K) Validity Index as given in Equation 13 in Section IV on page 124 of:<br/>
 * @Article{ 923275, title = "Nonparametric Genetic Clustering: Comparison of Validity Indices",
 *           author = "Ujjwal Maulik and Sanghamitra Bandyopadhyay", journal = "IEEE Transactions on
 *           Systems, Man, and Cybernetics, Part C: Applications and Reviews", pages = "120--125",
 *           volume = "31", number = "1", month = feb, year = "2001", issn = "1094-6977", }
 * NOTE: I(K) isn't really a name, so I'm calling it the Maulik-Bandyopadhyay Validity Index
 * @author Theuns Cloete
 */
public class MaulikBandyopadhyayIndex extends ClusteringFitnessFunction {
	private static final long serialVersionUID = -1094819834873604274L;
	private int p = 1;

	public MaulikBandyopadhyayIndex() {
		super();
		p = 1;
	}

	@Override
	public double calculateFitness() {
		return Math.pow(termOne() * termTwo() * termThree(), p);
	}

	private double termOne() {
		return 1.0 / clustersFormed;
	}

	private double termTwo() {
		// This is the normalizing factor, E_1 which they talk about in the article
		double intraDatasetDistance = 0.0;

		Vector mean = dataset.getMean();
		for (Pattern pattern : dataset.getPatterns()) {
			intraDatasetDistance += dataset.calculateDistance(pattern.data, mean);
		}

		return intraDatasetDistance / calculateIntraClusterDistance();
	}

	private double termThree() {
		return calculateMaximumInterClusterDistance();
	}

	public void setP(int pu) {
		if (pu < 1)
			throw new IllegalArgumentException("The p-value cannot be <= 0");
		p = pu;
	}
}
