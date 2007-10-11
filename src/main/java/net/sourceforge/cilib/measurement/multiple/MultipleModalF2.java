/*
 * MultipleModalF2.java
 *
 * Created on 12 October, 2006, 2:45 PM
 *
 * Copyright (C) 2006
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
package net.sourceforge.cilib.measurement.multiple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author Edrich van Loggerenberg
 */
public class MultipleModalF2 implements Measurement {
	private static final long serialVersionUID = 7069410059006587069L;

	public MultipleModalF2() {
	}

	public MultipleModalF2(MultipleModalF1 copy) {
	}

	public MultipleModalF1 clone() {
		return new MultipleModalF1(this);
	}

	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		Vector v = new MixedVector();
		Collection<OptimisationSolution> p = (Algorithm.get()).getSolutions();
		
		Hashtable<Double, Vector> solutionsFound = new Hashtable<Double, Vector>();
		ArrayList<Double> opt = new ArrayList<Double>();
		opt.add(0.1);
		opt.add(0.3);
		opt.add(0.5);
		opt.add(0.7);
		opt.add(0.9);
		
		for (Iterator<OptimisationSolution> i = p.iterator(); i.hasNext();) {
			Vector solution = (Vector) i.next().getPosition();
			for (int count = 0; count < opt.size(); count++) {
				double sol = (Double) opt.get(count);
				if (testNear(solution.getReal(0), sol)) {
					if (!solutionsFound.containsKey(sol))
						solutionsFound.put(sol, solution);
					break;
				}
			}
		}
		
		Enumeration<Double> sols = solutionsFound.keys();
		while (sols.hasMoreElements()) {
			// double k = (Double)sols.nextElement();
			Vector s = (Vector) solutionsFound.get(sols.nextElement());
			v.append(s);
			StringType t = new StringType();
			t.setString(String.valueOf(computeDerivative(s.getReal(0))));
			v.append(t);
		}
		
		return v;
	}

	private boolean testNear(double solution, double val) {
		if (val >= (solution - 0.05) && val <= (solution + 0.05))
			return true;
		
		return false;
	}

	private double computeDerivative(double x) {
		double k = (Math.log(0.25)) / 0.64;
		double gx = k * Math.pow((x - 0.1), 2.0);
		double dgx = 2.0 * k * (x - 0.1);
		double df1 = 30.0 * Math.PI * Math.pow(Math.sin(5.0 * Math.PI * x), 5.0) * Math.cos(5.0 * Math.PI * x);
		double df2 = Math.pow(Math.sin(5.0 * Math.PI * x), 6.0) * dgx * Math.exp(gx) + Math.exp(gx) * df1;
		
		return df2;
	}
}