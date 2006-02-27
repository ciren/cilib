/*
 * EuclideanDistanceMeasure.java
 * 
 * Created on Apr 14, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.util;

import net.sourceforge.cilib.type.types.Vector;

/**
 * @author Edwin Peer
 */
public class EuclideanDistanceMeasure implements DistanceMeasure {

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Util.DistanceMeasure#distance(double[], double[])
     */
    //public double distance(double[] x, double[] y) {
	public double distance(Vector x, Vector y) {
        /*if (x.length != y.length) {
            throw new IllegalArgumentException("Unmatched argument lengths");
        }
        
        double distance = 0;
        for (int i = 0; i < x.length; ++i) {
            double tmp = x[i] - y[i];
            distance += tmp * tmp; 
        }
        
        return Math.sqrt(distance);*/
		
		if (x.getDimension() != y.getDimension())
			throw new IllegalArgumentException("Unmatched argument lengths");
		
		double distance = 0;
		for (int i = 0; i < x.getDimension(); ++i) {
			double tmp = x.getReal(i) - y.getReal(i);
			distance += tmp * tmp;
		}
		
		return distance;
    }

}
