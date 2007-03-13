package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

public class ShiftedQuadric extends ContinuousFunction{
	
	double y_shift;
	double x_shift;
	
	public ShiftedQuadric() {
        setDomain("R(-100, 100)^30");
        
        y_shift = 50;
        x_shift = 0;
    }
    
    public Object getMinimum() {
        return new Double(y_shift);
    }
    
    public double evaluate(Vector x) {
        double sumsq = 0;
        for (int i = 0; i < getDimension(); ++i) {
            double sum = 0;
            for (int j = 0; j <= i; ++j) {
                sum += x.getReal(j);
            }
            sumsq += (sum+x_shift) * (sum+x_shift);
        }
        return sumsq + y_shift;
    }
}
