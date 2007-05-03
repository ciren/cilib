/*
 * Created on 2004/12/06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;



import java.io.IOException;
import java.util.Collection;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.neuralnetwork.foundation.postSimulation.PostMeasurementSuite;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.Problem;

/**
 * @author stefanv
 *
 */
public class NeuralNetworkController extends Algorithm implements OptimisationAlgorithm{

	protected NeuralNetworkProblem problem = null;
	protected NNError[] errorDt = null;
	protected PostMeasurementSuite measures;
	
	
	
	public NeuralNetworkController() {
		super();
		this.problem = null;
		this.errorDt = null;
		this.measures = null;
	}
	
	protected void performInitialisation() {
    	    	
		if (this.problem == null){
			throw new IllegalArgumentException("NeuralNetworkController: Required NNProblem object was null during initialization");
		}
		
		this.problem.initialize();
	}
	
	
	protected void performUninitialisation() {
		
		if (this.measures != null){
			try {
				measures.performMeasurement();
			} catch (IOException e) {
				throw new IllegalStateException("Problem writing Simulation measures to file");
			}
		}
		
	}

	protected void performIteration() {
		
		errorDt = problem.learningEpoch();
		System.out.println("------------   Epoch " + this.getIterations() + " completed, error list :   ------------");
		for (int i = 0; i < errorDt.length; i++){
			System.out.println("\t" + errorDt[i].getName() + " \t\t\t" + ((Double)errorDt[i].getValue()).doubleValue());
		}
	}

	
	public NNError[] getError() {
		return errorDt;
	}
	
				
	
	public void setProblem(Problem problem_) {
		problem = (NeuralNetworkProblem)problem_;
	}

	public void setOptimisationProblem(OptimisationProblem problem_) {
		problem = (NeuralNetworkProblem)problem_;		
	}

	public OptimisationProblem getOptimisationProblem() {
		return this.problem;
	}
	
	public void setMeasures(PostMeasurementSuite measures) {
		this.measures = measures;
	}

	public OptimisationSolution getBestSolution() {
		return null;
	}

	public Collection<OptimisationSolution> getSolutions() {
		return null;
	}
}
