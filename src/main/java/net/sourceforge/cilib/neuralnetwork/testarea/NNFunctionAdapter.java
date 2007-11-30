package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.type.types.container.Vector;

public class NNFunctionAdapter extends Function {
	private static final long serialVersionUID = -8189968864920232174L;
	
	EvaluationMediator mediator;

	@Override
	public Double evaluate(Object in) {
		mediator.getTopology().setWeights((Vector)in);
		mediator.performLearning();
		return mediator.getErrorDt()[0].getValue();
	}

	public Object getMaximum() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getMinimum() {
		// TODO Auto-generated method stub
		return null;
	}

	public NNFunctionAdapter getClone() {
		return new NNFunctionAdapter();
	}

}
