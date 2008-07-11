package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.neuralnetwork.testarea.NNFunctionAdapter;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.ProblemVisitor;

public class NeuralNetworkRetrievalVisitor extends ProblemVisitor {

	private EvaluationMediator mediator;

	@Override
	public void visit(Problem o) {
		FunctionOptimisationProblem functionOptimisationProblem = (FunctionOptimisationProblem) o;
		Function function = functionOptimisationProblem.getFunction();
		NNFunctionAdapter nnFunctionAdapter = (NNFunctionAdapter) function;
		
		this.mediator = nnFunctionAdapter.getMediator();
	}

	public EvaluationMediator getMediator() {
		return mediator;
	}	
}
