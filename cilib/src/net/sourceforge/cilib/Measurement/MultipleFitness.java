package net.sourceforge.cilib.Measurement;

import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import java.util.Collection;
import net.sourceforge.cilib.Problem.OptimisationSolution;
import java.util.Vector;
import net.sourceforge.cilib.Algorithm.Algorithm;
import java.util.Iterator;

/**
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */

public class MultipleFitness implements Measurement {

	public MultipleFitness() {
	}

	public String getDomain() {
		return "?^N";
	}
	
	public Object getValue() {
		Vector fitnessValues = new Vector();
		Collection solutions = ((OptimisationAlgorithm) Algorithm.get()).getSolutions();
		for (Iterator i=solutions.iterator(); i.hasNext(); ) {
			Object fitness = ((OptimisationSolution)i.next()).getFitness().getValue();
			fitnessValues.add(fitness);
		}
		return fitnessValues.toArray();
	}
  
}