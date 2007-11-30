package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

public class ErrorDt implements Measurement {
	private static final long serialVersionUID = -5027085270461720189L;
	EvaluationMediator eval;

	public ErrorDt() {
		eval = null;
	}

	public ErrorDt(ErrorDt rhs) {
//		super(rhs);
		throw new UnsupportedOperationException("public ErrorDt(ErrorDt rhs)");
	}

	public ErrorDt getClone() {
		return new ErrorDt(this);
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
		Vector err = new Vector();
		
		for (int i = 0; i < errorDt.length; i++){
			err.add(new Real(errorDt[i].getValue().doubleValue()));
		}
		
		return err;
	}

	public void setEval(EvaluationMediator eval) {
		this.eval = eval;
	}
}
