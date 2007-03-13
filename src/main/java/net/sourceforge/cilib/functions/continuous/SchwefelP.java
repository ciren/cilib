package net.sourceforge.cilib.functions.continuous;


import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;


/**
 * Characteristics:
 * 
 * <li>Unimodal</li>
 * <li>Non Separable</li>
 * 
 * f(x) = 0; x = (0,0,...,0)
 * 
 * x e [-100,100]
 * 
 * @author Gary Pampara
 */

public class SchwefelP extends ContinuousFunction {
	
	public SchwefelP() {
        setDomain("R(-100, 100)^30");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
    public double evaluate(Vector x) {
    	double sumsq = 0.0;
		double sum = 0.0;
		
		for (int i = 0; i < getDimension(); i++) {
			sum = 0.0;
			
			for (int j = 0; j < i; j++) {
				sum += x.getReal(j);
			}
			
			sumsq = sum * sum; 
		}
		
		return sumsq;
    }
}

