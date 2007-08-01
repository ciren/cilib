/*
 * ClusterableDataSet.java
 * 
 * Created on May 24, 2007
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
package net.sourceforge.cilib.problem.dataset;

import java.util.ArrayList;

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * All datasets that will be clustered have to implement this interface.
 * @author Theuns Cloete
 */
public interface ClusterableDataSet {
	public void arrangeClustersAndCentroids(Vector centroids);
	public ArrayList<ArrayList<Pattern>> getArrangedClusters();
	public ArrayList<Vector> getArrangedCentroids();

	public int getNumberOfPatterns();

	public Pattern getPattern(int index);

	public ArrayList<Pattern> getPatterns();

	public void setDistanceMeasure(DistanceMeasure distanceMeasure);

	public DistanceMeasure getDistanceMeasure();

	public double calculateDistance(Vector lhs, Vector rhs);

	public double calculateDistance(int lhs, int rhs);

	public void setNumberOfClusters(int clusters);

	public int getNumberOfClusters();

	public Vector getSetMean(ArrayList<Pattern> set);

	public Vector getSetVariance(ArrayList<Pattern> set);

	public Vector getMean();

	public Vector getVariance();

	public class Pattern {
		public int index = 0;
		public int clas = 0;
		public Vector data = null;

		protected Pattern(int i, int c, Vector d) {
			index = i;
			clas = c;
			data = d;
		}

		protected Pattern(Pattern rhs) {
			index = rhs.index;
			clas = rhs.clas;
			data = rhs.data;
		}

		protected Pattern clone() {
			return new Pattern(this);
		}

		public String toString() {
			return "Index " + index + "; Class " + clas + "; Data " + data;
		}
	}
}
