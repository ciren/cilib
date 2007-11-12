/*
 * DunnIndex.java
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
package net.sourceforge.cilib.functions.clustering;

/**
 * This is the Dunn Validity Index as given in:<br/>
 * @Article{ dunn1974vi, title = "Well Separated Clusters and Optimal Fuzzy Partitions", author =
 *           "J. C. Dunn", journal = "Journal of Cybernetics", pages = "95--104", volume = "4", year =
 *           "1974" }
 * @author Theuns Cloete
 */
public class DunnIndex extends GeneralisedDunnIndex {
	private static final long serialVersionUID = -7440453719679272149L;

	public DunnIndex() {
		super();
	}

	@Override
	protected double calculateWithinClusterScatter(int k) {
		return calculateClusterDiameter(k);
	}

	@Override
	protected double calculateBetweenClusterSeperation(int i, int j) {
		return calculateMinimumSetDistance(i, j);
	}
}
