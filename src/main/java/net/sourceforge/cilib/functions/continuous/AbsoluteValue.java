package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

public class AbsoluteValue extends ContinuousFunction {
	double y_shift;
	double x_shift;
	
	public AbsoluteValue() {
		setDomain("R(-100, 100)^30");
		
		y_shift = -80;
		x_shift = 5;
	}
	    
	public Object getMinimum() {
		return new Double(y_shift);
	 }

	public double evaluate(Vector x) {
		double tmp = 0;
		for (int i = 0; i < getDimension(); ++i) {
			tmp += Math.abs(x.getReal(i)+x_shift);
		}
		return tmp + y_shift;
	}	
}
