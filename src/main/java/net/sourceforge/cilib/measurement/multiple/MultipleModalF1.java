/*
 * MultipleModealF1.java
 *
 * Created on 7 September, 2006, 3:50 PM
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
public class MultipleModalF1 implements Measurement {
	private static final long serialVersionUID = -8316746632433525105L;

	public MultipleModalF1() {
	}

	public MultipleModalF1(MultipleModalF2 copy) {
	}

	public MultipleModalF2 clone() {
		return new MultipleModalF2(this);
	}

	public String getDomain() {
		return "T";
	}

	public Type getValue() {

		Vector v = new MixedVector();
		Collection<OptimisationSolution> p = (Algorithm.get()).getSolutions();

		Hashtable<Double, Vector> solutionsFound = new Hashtable<Double, Vector>();

		for (Iterator<OptimisationSolution> i = p.iterator(); i.hasNext();) {
			Vector solution = (Vector) i.next().getPosition();

			double sol = 0.1;
			for (int count = 0; count < 5; count++) {
				if (testNear(solution.getReal(0), sol)) {
					if (!solutionsFound.containsKey(sol))
						solutionsFound.put(sol, solution);

					break;
				}
				sol += 0.2;
			}
		}

		// StringType t = new StringType();
		// t.setString(solutionsFound.size() "");
		// v.append(t);

		Enumeration<Double> sols = solutionsFound.keys();
		while (sols.hasMoreElements()) {
			Vector s = (net.sourceforge.cilib.type.types.container.Vector) solutionsFound
					.get(sols.nextElement());

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
		double df1 = 30.0 * Math.PI
				* Math.pow(Math.sin(5.0 * Math.PI * x), 5.0)
				* Math.cos(5.0 * Math.PI * x);

		return df1;
	}
}
