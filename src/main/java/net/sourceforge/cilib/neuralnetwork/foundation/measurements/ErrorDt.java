package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

public class ErrorDt implements Measurement {
	
	EvaluationMediator eval;
	
	public ErrorDt() {
		eval = null;
	}
	
	

	public ErrorDt(EvaluationMediator eval) {
		super();
		// TODO Auto-generated constructor stub
		this.eval = eval;
	}



	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		NNError[] errorDt = eval.getErrorDt();
		MixedVector err = new MixedVector();
		
		for (int i = 0; i < errorDt.length; i++){
			err.add(new Real(errorDt[i].getValue().doubleValue()));
		}
		
		return err;
	}

	public void setEval(EvaluationMediator eval) {
		this.eval = eval;
	}
	
	

}
