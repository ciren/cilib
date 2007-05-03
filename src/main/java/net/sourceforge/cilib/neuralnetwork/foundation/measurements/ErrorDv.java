package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.UnimplementedMethodException;

public class ErrorDv implements Measurement {
	private static final long serialVersionUID = 3315947215103835233L;
	EvaluationMediator eval;

	public ErrorDv() {
		eval = null;
	}

	public ErrorDv(ErrorDv rhs) {
//		super(rhs);
		throw new UnimplementedMethodException("public ErrorDv(ErrorDv rhs)");
	}

	public ErrorDv clone() {
		return new ErrorDv(this);
	}

	public ErrorDv(EvaluationMediator eval) {
		this.eval = eval;
	}

	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		NNError[] errorDv = eval.getErrorDv();
		MixedVector err = new MixedVector();
		
		for (int i = 0; i < errorDv.length; i++){
			err.add(new Real(errorDv[i].getValue().doubleValue()));
		}
		
		return err;
	}

	public void setEval(EvaluationMediator eval) {
		this.eval = eval;
	}
}
