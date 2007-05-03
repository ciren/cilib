/*
 * Created on 2004/11/30
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.util.UnimplementedMethodException;

/**
 * @author stefanv
 */
public class NeuralNetworkProblem implements OptimisationProblem, Initializable {
	private static final long serialVersionUID = -5790791148649131742L;
	protected int fitnessEvaluations;
	protected EvaluationMediator evaluationStrategy = null;
	
	public NeuralNetworkProblem(){
		this.evaluationStrategy = null;
		this.fitnessEvaluations = 0;	
	}

	public NeuralNetworkProblem(NeuralNetworkProblem rhs) {
//		super(rhs);
		throw new UnimplementedMethodException("public NeuralNetworkProblem(NeuralNetworkProblem rhs)");
	}

	public NeuralNetworkProblem clone() {
		return new NeuralNetworkProblem(this);
	}

	public void initialize(){
				
		if (this.evaluationStrategy == null) {
			throw new IllegalArgumentException("NeuralNetworkProblem: A required evaluationStrategy object was null during initialization");
		}
		
		this.evaluationStrategy.initialize();
	
	}

	public NNError[] learningEpoch(){
		evaluationStrategy.performLearning();
		return evaluationStrategy.getErrorDt();
	}

	public MixedVector evaluate(MixedVector in){
		
		StandardPattern p = new StandardPattern(in,null);
		return evaluationStrategy.evaluate(p);
	}

	public Fitness getFitness(Object solution, boolean count) {
		
		if (count) {
    		++fitnessEvaluations;
    	}
        this.getTopology().setWeights((MixedVector) solution);
			            
			//Defaults to first error element as the main fitness...
            return evaluationStrategy.getErrorDt()[0];
	}

	public int getFitnessEvaluations() {
		return this.fitnessEvaluations;
	}

	public NeuralNetworkTopology getTopology() {
		return evaluationStrategy.getTopology();
	}

	public void setTopology(NeuralNetworkTopology topology) {
		evaluationStrategy.setTopology(topology);
	}

	public EvaluationMediator getEvaluationStrategy() {
		return evaluationStrategy;
	}

	public DomainRegistry getDomain() {
		return null;
	}

	public DomainRegistry getBehaviouralDomain() {
		return null;
	}

	public void setEvaluationStrategy(EvaluationMediator evaluationStrategy) {
		this.evaluationStrategy = evaluationStrategy;
	}

	public DataSetBuilder getDataSetBuilder() {
		return null;
	}

	public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {
				
	}
}