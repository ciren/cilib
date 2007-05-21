package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

public class AbsoluteValue extends ContinuousFunction {
	private static final long serialVersionUID = 2425057152860379846L;
	
	public AbsoluteValue() {
		setDomain("R(-100, 100)^30");
		
		verticalShift = -80;
		horizontalShift = 5;
	}
	    
	public Object getMinimum() {
		return new Double(verticalShift);
	 }

	public double evaluate(Vector x) {
		double tmp = 0;
		for (int i = 0; i < getDimension(); ++i) {
			tmp += Math.abs(x.getReal(i)+horizontalShift);
		}
		return tmp + verticalShift;
	}	
}
