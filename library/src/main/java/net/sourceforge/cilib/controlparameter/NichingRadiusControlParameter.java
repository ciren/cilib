/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.NichingFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.tuning.TuningAlgorithm;
import net.sourceforge.cilib.tuning.TuningProblem;

public class NichingRadiusControlParameter implements ControlParameter {
	
	private double defaultRadius = 0.01;

    @Override
    public double getParameter() {
    	Algorithm alg = AbstractAlgorithm.get();
    	Problem prob = alg.getOptimisationProblem();
    	
        if (prob instanceof TuningProblem) {
    		prob = ((TuningProblem) prob).getCurrentProblem();
    	}
    	
    	if (((FunctionOptimisationProblem) prob).getFunction() instanceof NichingFunction) {
    		return ((NichingFunction) ((FunctionOptimisationProblem) prob).getFunction()).getNicheRadius();
    	} else {
            System.out.println("Warning: using default niching radius. Function is not of type NichingFunction.");
			return defaultRadius;
        }
    }

    @Override
    public double getParameter(double min, double max) {
        return getParameter();
    }

    @Override
    public ControlParameter getClone() {
        return this;
    }
    
    public void setDefaultRadius(double defaultRadius) {
		this.defaultRadius = defaultRadius;
	}

}
