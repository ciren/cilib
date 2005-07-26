/*
 * Created on Aug 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.cilib.Measurement;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;

/**
 * @author espeer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Solution implements Measurement {

	public Solution() {
		
	}
	
	public String getDomain() {
		return "T";
	}

	public Object getValue() {
		double[] solution = (double[]) ((OptimisationAlgorithm) Algorithm.get()).getBestSolution().getPosition();
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(solution[0]);
		for (int i = 1; i < solution.length; ++i) {
			buffer.append(", ");
			buffer.append(solution[i]);
		}
		buffer.append("]");
		return buffer.toString();
	}

}
