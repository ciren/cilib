package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;


/**
 * Characteristics:
 * 
 * f(x) = -12569.5, x = (420.9687,...,420.9687);
 * 
 * x e [-500,500]
 * 
 * @author  Andries Engelbrecht
 */
// TODO: Check discontinuous / continuous

public class SchwefelProblem2_26 extends ContinuousFunction {
	
	public SchwefelProblem2_26() {
        setDomain("R(-500, 500)^30");
    }
    
    public Object getMinimum() {
        return new Double(-12569.5);
    }
    
    public double evaluate(Vector x) {
        double sum = 0.0;
        
        for (int i = 0; i < getDimension(); i++) {
        	sum += x.getReal(i)*Math.sin(Math.sqrt(Math.abs(x.getReal(i))));
        }
        return -sum;
    }
}

