package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

public class ErrorDg implements Measurement {
	
	EvaluationMediator eval;
	
	public ErrorDg() {
		eval = null;
	}
	
	

	public ErrorDg(EvaluationMediator eval) {
		this.eval = eval;
	}



	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		NNError[] errorDg = eval.getErrorDg();
		MixedVector err = new MixedVector();
		
		for (int i = 0; i < errorDg.length; i++){
			err.add(new Real(errorDg[i].getValue().doubleValue()));
		}
		
		return err;
	}



	public void setEval(EvaluationMediator eval) {
		this.eval = eval;
	}
	
	

}
