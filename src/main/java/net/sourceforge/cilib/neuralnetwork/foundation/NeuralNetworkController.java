/*
 * Created on 2004/12/06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;

import java.io.IOException;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.neuralnetwork.foundation.postSimulation.PostMeasurementSuite;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.util.UnimplementedMethodException;

/**
 * @author stefanv
 *
 */
public class NeuralNetworkController extends Algorithm {
	private static final long serialVersionUID = -904395696777716473L;
	protected NeuralNetworkProblem problem = null;
	protected NNError[] errorDt = null;
	protected PostMeasurementSuite measures;

	public NeuralNetworkController() {
		super();
		this.problem = null;
		this.errorDt = null;
		this.measures = null;
	}

	public NeuralNetworkController(NeuralNetworkController rhs) {
//		super(rhs);
		throw new UnimplementedMethodException("public NeuralNetworkController(NeuralNetworkController rhs)");
	}

	public NeuralNetworkController clone() {
		return new NeuralNetworkController(this);
	}

	public void performInitialisation() {
    	    	
		if (this.problem == null){
			throw new IllegalArgumentException("NeuralNetworkController: Required NNProblem object was null during initialization");
		}
		
		this.problem.initialize();
	}

	public void performUninitialisation() {
		
		if (this.measures != null){
			try {
				measures.performMeasurement();
			} catch (IOException e) {
				throw new IllegalStateException("Problem writing Simulation measures to file");
			}
		}
		
	}

	public void algorithmIteration() {
		
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

	public List<OptimisationSolution> getSolutions() {
		return null;
	}

	@Override
	public Algorithm getCurrentAlgorithm() {
		return this;
	}
}
