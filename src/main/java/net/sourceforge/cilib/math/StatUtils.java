/*
 * StatUtils.java
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
package net.sourceforge.cilib.math;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Some simple methods for determining some statistics.
 *
 */
public final class StatUtils {
	
	private StatUtils() {
	}
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	public static double mean(Vector vector) {
		return org.apache.commons.math.stat.StatUtils.mean(unwrap(vector));
	}
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	public static double variance(Vector vector) {
		return org.apache.commons.math.stat.StatUtils.variance(unwrap(vector));
	}
	
	private static double[] unwrap(Vector vector) {
		Double [] a = new Double[vector.getDimension()];
		a = vector.toArray(a);
		
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++)
			result[i] = a[i].doubleValue();
		
		return result;
	}

	/**
	 * 
	 * @param vector
	 * @return
	 */
	public static double stdDeviation(Vector vector) {
		return Math.sqrt(variance(vector));
	}

}
