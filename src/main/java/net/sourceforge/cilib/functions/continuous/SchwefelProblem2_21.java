package net.sourceforge.cilib.functions.continuous;


import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Characteristics:
 * 
 * f(x) = 0;
 * 
 * x e [-100,100]
 * 
 * @author  Andries Engelbrecht
 */
// TODO: Check discontinuous / continuous

public class SchwefelProblem2_21 extends ContinuousFunction {
	private static final long serialVersionUID = 8583159190281586599L;

	public SchwefelProblem2_21() {
        setDomain("R(-100, 100)^30");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
    public double evaluate(Vector x) {
        double max = Math.abs(x.getReal(0));
        double value;
        
        for (int i = 1; i < x.getDimension(); ++i) {
        	value = Math.abs(x.getReal(i));
            if (value > max)
            	max = value;
        }
        return max;
    }
}
