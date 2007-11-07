package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

public class ErrorDg implements Measurement {
	private static final long serialVersionUID = -5129864489346375855L;
	EvaluationMediator eval;
	
	public ErrorDg() {
		eval = null;
	}

	public ErrorDg(ErrorDg rhs) {
//		super(rhs);
		throw new UnsupportedOperationException("public ErrorDg(ErrorDg rhs)");
	}

	public ErrorDg clone() {
		return new ErrorDg(this);
	}

	public ErrorDg(EvaluationMediator eval) {
		this.eval = eval;
	}

	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		NNError[] errorDg = eval.getErrorDg();
		Vector err = new Vector();
		
		for (int i = 0; i < errorDg.length; i++){
			err.add(new Real(errorDg[i].getValue().doubleValue()));
		}
		
		return err;
	}

	public void setEval(EvaluationMediator eval) {
		this.eval = eval;
	}
}
