package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

public class ShiftedRastrigin extends ContinuousFunction{

	double y_shift;
	
	public ShiftedRastrigin() {
		setDomain("R(-5.12, 5.12)^30");
		
		y_shift = -50;
	}
	    
	public Object getMinimum() {
		return new Double(y_shift);
	 }

	public double evaluate(Vector x) {
		double tmp = 0;
		for (int i = 0; i < getDimension(); ++i) {
			tmp += x.getReal(i) * x.getReal(i) - 10.0 * Math.cos(2 * Math.PI * x.getReal(i));
		}
		return 10*getDimension() + tmp + y_shift;
	}	
}
