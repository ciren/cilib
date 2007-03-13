package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

public class ShiftedAckley extends ContinuousFunction{
	
	double y_shift;
	double x_shift;
	
	public ShiftedAckley() {
        setDomain("R(-30, 30)^30");
    
        y_shift = -10;
        x_shift = 0;
	}
    
    public Object getMinimum() {
        return new Double(y_shift);
    }
    
    public double evaluate(Vector x) {
        double sumsq = 0.0;
        double sumcos = 0.0;
        for (int i = 0; i < getDimension(); ++i) {
            sumsq += (x.getReal(i)+x_shift) * (x.getReal(i)+x_shift);
            sumcos += Math.cos(2 * Math.PI * x.getReal(i));
        }
        return -20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / getDimension())) - Math.exp(sumcos / getDimension()) + 20 + Math.E + y_shift;
    }
}
