/*
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
	 * Generate the required factorial of the number <code>x</code>.
	 * @param x The number to generate the factorial from.
	 * @return The factorial of <code>x</code>.
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
	 * Return the combination of <code>n</code> and <code>r</code>.
	 * @param n The ??? 
	 * @param r >???
	 * @return The combination of <code>n</code> and <code>r</code>
	 */
	public static double combination(double n, double r) {
		if (n < r)
			throw new IllegalArgumentException("In a combination the following must hold: n >= x");
		
		return permutation(n, r) / factorial(r);
	}
	
	
	/**
	 * This is a convienience method providing an alias to <code>combination</code>.
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
	 * General sigmoid function. The steepness of the function is defined to the <code>1.0</code>, with
	 * the <code>offset</code> defined to be <code>0.0</code>.
	 * @param v The value to be evaluated by the sigmoid function.
	 * @return The returned evaluation of the function.
	 */
	public static double sigmoid(double v) {
		return sigmoid(1.0, 0.0, v);
	}
	
	/**
	 * The generalised sigmoid function. The function is the general case of the sigmoid function
	 * with the ability to specify the steepness of the function as well as an offest that should
	 * be taken into consideration. 
	 * @param steepness The value of the steepness coefficient. The large this value the greater the
	 *                  potential rate of change for the function will be. The value <tt><b>MUST</b></tt>
	 *                  be <code>&gt;= 0</code>.
	 * @param offset The offset that should be added or subtracted from the value provided in <code>value</code>.
	 * @param value The value to be evaluated by the sigmoid function.
	 * @return The value of the sigmoid function. The value returned is: <code>0 &lt;= y &lt;= 1</code>.
	 */
	public static double sigmoid(double steepness, double offset, double value) {
		return (1.0 / (1.0+Math.pow(Math.E, -1.0*steepness*(value-offset))));
	}
	
	
	/**
	 * 
	 * @param pf
	 * @return
	 */
	public static int flip(double prob) {
		if (randomiser.nextDouble() <= prob)
			return 1;

		return 0;
	}
	
	/**
	 * Determine the log of the specified <code>value</code> with the provided <code>base</code>.
	 * @param base The base of the log operation.
	 * @param value The value to determine the log of.
	 * @return The log value of <code>value</code> using the base value of <code>base</code>.
	 */
	public static double log(double base, double value) {
		return Math.log(value) / Math.log(base);
	}

}
