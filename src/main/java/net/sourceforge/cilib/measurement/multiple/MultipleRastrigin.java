/*
 * MultipleRastrigin.java
 *
 * Created on 12 March, 2007, 11:22 AM
 *
 * Copyright (C) 2007
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
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author Edrich
 */
public class MultipleRastrigin implements Measurement {
	private static final long serialVersionUID = -587326267567384750L;
	private double threshold = 1.0d;

	public MultipleRastrigin() {
	}

	public MultipleRastrigin(MultipleRastrigin copy) {
	}

	public MultipleRastrigin clone() {
		return new MultipleRastrigin(this);
	}

	public String getDomain() {
		return "T";
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public Type getValue() {

		Vector v = new MixedVector();
		Collection<OptimisationSolution> p = (Algorithm.get()).getSolutions();

		ArrayList<Vector> solutionsFound = new ArrayList<Vector>();

		for (Iterator<OptimisationSolution> i = p.iterator(); i.hasNext();) {
			Vector solution = (Vector) i.next().getPosition();

			if (DomainParser.getInstance().isInsideBounds(solution)) {
				if (!HasSolution(solutionsFound, solution))
					solutionsFound.add(solution);
			}
		}

		StringType t = new StringType();
		t.setString(solutionsFound.size() + "");
		v.append(t);

		/*
		 * Vector s = new MixedVector(); s.add(new Real(-1.0));
		 * 
		 * for (int i = 0; i < s.size(); i++) { t = new StringType();
		 * t.setString(ComputeDerivative(s.getReal(i)) + ""); v.append(t); }
		 */

		for (int index = 0; index < solutionsFound.size(); index++) {
			Vector s = (Vector) solutionsFound.get(index);

			for (int i = 0; i < s.size(); i++) {
				t = new StringType();
				t.setString(ComputeDerivative(s.getReal(i)) + "");
				v.append(t);
			}
		}

		return v;
	}

	private boolean HasSolution(ArrayList<Vector> list, Vector sol1) {
		for (int i = 0; i < list.size(); i++) {
			Vector sol2 = (Vector) list.get(i);
			ArrayList<Integer> checks = new ArrayList<Integer>();

			for (int d = 0; d < sol1.size(); d++) {
				if (sol2.getReal(d) > (sol1.getReal(d) - threshold)
						&& sol2.getReal(d) < (sol1.getReal(d) + threshold))
					checks.add(1);
				else
					checks.add(0);
			}

			int countSimilar = 0;
			for (int index = 0; index < checks.size(); index++)
				if (checks.get(index) == 1)
					countSimilar++;

			if (countSimilar == checks.size())
				return true;

		}

		return false;
	}

	private double ComputeDerivative(double xi) {
		return (2.0 * xi) + (20.0 * Math.PI * Math.sin(2.0 * Math.PI * xi));
	}
}