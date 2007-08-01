package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Theuns Cloete
 */
public class GenericFunctionMeasurement implements Measurement {
	private static final long serialVersionUID = 3301062975775598397L;
	private Function function = null;

	public GenericFunctionMeasurement() {
		function = null;
	}

	public GenericFunctionMeasurement(GenericFunctionMeasurement rhs) {
		function = rhs.function;
	}

	public GenericFunctionMeasurement clone() {
		return new GenericFunctionMeasurement(this);
	}

	public String getDomain() {
		return "R";
	}

	public Type getValue() {
		if (function == null)
			throw new InitialisationException("The function that should be evaluated has not been set");

		Vector vector = (Vector)Algorithm.get().getBestSolution().getPosition();
		return new Real(function.evaluate(vector));
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function f) {
		function = f;
	}
}
