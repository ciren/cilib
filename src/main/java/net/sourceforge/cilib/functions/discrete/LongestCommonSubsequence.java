package net.sourceforge.cilib.functions.discrete;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.dataset.TextDataSetBuilder;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the Longest Common Subsequence problem.
 * 
 * @author gpampara
 */
public class LongestCommonSubsequence extends DiscreteFunction {

	private static final long serialVersionUID = -3586259608521073084L;
	
	public LongestCommonSubsequence getClone() {
		return new LongestCommonSubsequence();
	}


	public double evaluate(Vector x) {
		double v = 0.0;
		double l = length(x);
		double m = matches(x);
		double K = this.getDataSetSize();
		
		v = l + (30*m);
		
		if (l == getShortestString().length())
			v += 50;
		
		if (m == K)
			v *= 3000;
		else
			v *= -1000*(K-m);
		
		return v;
	}
	
	
	/**
	 * Returns the lengh of the shortest string or the length of the first
	 * string
	 * 
	 * @return The shortest length
	 */
	private String getShortestString() {
		PopulationBasedAlgorithm popAlgorithm = (PopulationBasedAlgorithm) Algorithm.get();
		OptimisationProblem problem = popAlgorithm.getOptimisationProblem();
		TextDataSetBuilder dataSetBuilder = (TextDataSetBuilder) problem.getDataSetBuilder();
		
		return dataSetBuilder.getShortestString();
	}
	
	private int getDataSetSize() {
		PopulationBasedAlgorithm popAlgorithm = (PopulationBasedAlgorithm) Algorithm.get();
		OptimisationProblem problem = popAlgorithm.getOptimisationProblem();
		TextDataSetBuilder dataSetBuilder = (TextDataSetBuilder) problem.getDataSetBuilder();
		
		return dataSetBuilder.size();
	}
	
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	private int length(Vector x) {
		int count = 0;
		
		for (Iterator<Type> i = x.iterator(); i.hasNext(); ) {
			Numeric n = (Numeric) i.next();
			if (n.getBit() == true)
				count++;
		}
		
		return count;
	}
	
	
	private int matches(Vector x) {
		PopulationBasedAlgorithm popAlgorithm = (PopulationBasedAlgorithm) Algorithm.get();
		OptimisationProblem problem = popAlgorithm.getOptimisationProblem();
		TextDataSetBuilder dataSetBuilder = (TextDataSetBuilder) problem.getDataSetBuilder();
		
		int count = 0;
		
		String targetSubSequence = this.getSubSequence(x, this.getShortestString());
		
		for (int i = 0; i < dataSetBuilder.size(); i++) {
			String tmp = this.getSubSequence(x, dataSetBuilder.get(i));
			
			if (tmp.equals(targetSubSequence))
				count++;
		}
				
		return count;
	}
	
	
	private String getSubSequence(Vector x, String target) {
		String result = "";
		
		for (int i = 0; i < x.getDimension(); i++) {
			Numeric n = (Numeric) x.get(i);
			if (n.getBit() == true)
				result += target.charAt(i);
		}
		
		return result;
	}
	

}
