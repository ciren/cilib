/*
 * MultipleModalF5.java
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
package net.sourceforge.cilib.measurement.multiple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author Edrich van Loggerenberg
 */
public class MultipleModalF5 implements Measurement {
	private static final long serialVersionUID = 1915400477090241468L;

	/**
	 * Create a new instance of {@linkplain MultipleModalF5}.
	 */
	public MultipleModalF5() {
	}

	/**
	 * Create a copy of the provided instance.
	 * @param copy The instance to copy.
	 */
	public MultipleModalF5(MultipleModalF5 copy) {
	}

	/**
	 * {@inheritDoc}
	 */
	public MultipleModalF5 getClone() {
		return new MultipleModalF5(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDomain() {
		return "T";
	}

	/**
	 * {@inheritDoc}
	 */
	public Type getValue() {
		Vector v = new Vector();
		Collection<OptimisationSolution> p = (Algorithm.get()).getSolutions();

		Hashtable<String, Vector> solutionsFound = new Hashtable<String, Vector>();
		ArrayList<Double[]> opt = new ArrayList<Double[]>();
		Double[] ar = new Double[2];
		ar[0] = -2.81;
		ar[1] = 3.13;
		opt.add(ar);

		ar = new Double[2];
		ar[0] = 3.0;
		ar[1] = 2.0;
		opt.add(ar);

		ar = new Double[2];
		ar[0] = 3.58;
		ar[1] = -1.85;
		opt.add(ar);

		ar = new Double[2];
		ar[0] = -3.78;
		ar[1] = -3.28;
		opt.add(ar);

		for (Iterator<OptimisationSolution> i = p.iterator(); i.hasNext();) {
			Vector solution = (Vector) i.next().getPosition();
			for (int count = 0; count < opt.size(); count++) {
				Double[] sol = (Double[]) opt.get(count);
				if (testNear(solution.getReal(0), sol[0]) && testNear(solution.getReal(1), sol[1])) {
					if (!solutionsFound.containsKey(sol[0] + ";" + sol[1]))
						solutionsFound.put(sol[0] + ";" + sol[1], solution);
					break;
				}
			}
		}

		Enumeration<String> sols = solutionsFound.keys();
		while (sols.hasMoreElements()) {
			Vector s = solutionsFound.get(sols.nextElement());
			v.append(s.get(0));
			v.append(s.get(1));
			
//			StringType t = new StringType();
//			t.setString(ComputeDerivativeX(s.getReal(0), s.getReal(1)) + "");
//			v.append(t);
//			
//			v.append(s.get(1));
//			t = new StringType();
//			t.setString(ComputeDerivativeY(s.getReal(0), s.getReal(1)) + "");
//			v.append(t);
		}
		/*
		 * for (Iterator<OptimisationSolution> i = p.iterator(); i.hasNext(); ) {
		 * Vector solution = (Vector) i.next().getPosition();
		 * //v.append(solution.get(0)); //v.append(solution.get(1));
		 * v.append(solution); }
		 */
		return v;
	}

	private boolean testNear(double solution, double val) {
		if (val >= (solution - 0.05) && val <= (solution + 0.05))
			return true;
		return false;
	}
//
//	private double ComputeDerivativeX(double x, double y) {
//		return -4.0 * x * (x * x + y - 11.0) - 2.0 * (x + y * y - 7.0);
//	}
//
//	private double ComputeDerivativeY(double x, double y) {
//		return -2.0 * (x * x + y - 11.0) - 4.0 * y * (x + y * y - 7.0);
//	}
}
