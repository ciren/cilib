package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.type.types.container.MixedVector;

public class NNFunctionAdapter extends Function {
	private static final long serialVersionUID = -8189968864920232174L;
	
	EvaluationMediator mediator;

	@Override
	public Double evaluate(Object in) {
		mediator.getTopology().setWeights((MixedVector)in);
		mediator.performLearning();
		return mediator.getErrorDt()[0].getValue();
	}

	@Override
	public Object getMaximum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMinimum() {
		// TODO Auto-generated method stub
		return null;
	}

}
