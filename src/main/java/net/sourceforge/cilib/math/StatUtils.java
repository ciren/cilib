package net.sourceforge.cilib.math;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Some simple methods for determining some statistics;
 *
 */
public final class StatUtils {
	
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
