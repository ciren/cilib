package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Vector;

public class ShiftedSphere extends ContinuousFunction{

	double y_shift;
	double x_shift;
	
    public ShiftedSphere() {
        setDomain("R(-15, 15)^30");
        
        y_shift = 20;
        x_shift = 0;
    }
    
    public Object getMinimum() {
        return new Double(20);
    }
   
    public double evaluate(Vector x) {
        double tmp = 0;
        for (int i = 0; i < x.getDimension(); i++) {
            tmp += (x.getReal(i)+x_shift) * (x.getReal(i)+x_shift);
        }
        return tmp + y_shift;
    }
}
