/*
 * DiscreteRandomNumber.java
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
package net.sourceforge.cilib.math.random;

import net.sourceforge.cilib.math.MathUtil;

/**
 * 
 * @author Gary Pampara
 *
 */
public class DiscreteRandomNumber {

	public double getPoisson(double x, double lambda) {
		double numerator = Math.pow(Math.E, -lambda) * Math.pow(lambda, x);
		double denominator = MathUtil.factorial(x);
		return numerator / denominator;
	}
	
	public double getBinomial(double x, double p, double n) {
		return MathUtil.combination(n, x) * Math.pow(p, x) * Math.pow((1-p), (n-x));		
	}

}
