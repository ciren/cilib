/*
 * MathUtil.java
 * 
 * Created on Aug 5, 2005
 *
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.math;

import java.util.Random;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * This class provides helper functions in addtion to the standard <code>java.lang.Math</code>
 * class.
 * 
 * These utility functions further are necessary for the various distributions and selections
 * required within CIlib as a whole.
 * 
 * @author Gary Pampara
 *
 */
public final class MathUtil {
	
	private static Random randomiser;
	
	private MathUtil() {
	}
	
		
	/**
	 * Generate the required factorial of the number <code>x</code>
	 * @param x The number to generate the factorial from
	 * @return The factorial of <code>x</code>
	 */
	public static double factorial(double x) {
		if (x < 0)
			throw new IllegalArgumentException("Factorial is defined to work on numbers >= 0 only");
		
		if (x == 0.0)
			return 1.0;
		else if (x == 1.0)
			return 1.0;
		else
			return x * factorial(x-1);
	}
	
	
	/**
	 * Return the combination of <code>n</code> and <code>r</code>
	 * @param n The ??? 
	 * @param r >???
	 * @return The combination of <code>n</code> and <code>r</code>
	 */
	public static double combination(double n, double r) {
		if (n < r)
			throw new IllegalArgumentException("In a combination the following must hold: n >= x");
		
		return permutation(n,r) / factorial(r);
	}
	
	
	/**
	 * This is a convienience method providing an alias to <code>combination</code>
	 * @param n
	 * @param r
	 * @return The value of the operation "<code>n</code> choose <code>x</code>"
	 */
	public static double choose(double n, double r) {
		return combination(n, r);
	}
	
	
	/**
	 * 
	 * @param n
	 * @param r
	 * @return
	 */
	public static double permutation(double n, double r) {
		return factorial(n) / factorial(n-r);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public static synchronized double random() {
		if (randomiser == null)
			randomiser = new MersenneTwister();
		
		return randomiser.nextDouble();
	}
	
	
	
	/**
	 * 
	 * @param v
	 * @return
	 */
	public static double sigmoid(double v) {
		return ( 1/(1+Math.pow(Math.E, -1.0*v)) ); 
	}
	
	
	/**
	 * 
	 * @param pf
	 * @return
	 */
	public static int flip(double prob) {
		if (randomiser.nextDouble() <= prob)
			return 1;
		else 
			return 0;
	}	

}
