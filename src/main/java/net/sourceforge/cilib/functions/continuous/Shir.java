package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

/**
 * The Damavandi function obtained from O.M. Shir and T. Baeck,
 * "Dynamic Niching in Evolution Strategies with Covariance Matrix Adaptation"
 * 
 * Global Maximin: f(x1,...,xn) = 1
 * 
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * </ul>
 * 
 * @author  Andries Engelbrecht
 */

public class Shir extends ContinuousFunction {
	private static final long serialVersionUID = 8157687561496975789L;
	
	double l1, l2, l3, l4, l5, sharpness;
	
	public Shir() {
        setDomain("R(0, 1)^30");
        
        l1 = 1.0;
        l2 = 1.0;
        l3 = 1.0;
        l4 = 1.0;
        l5 = 1.0;
        sharpness = 2;
    }
    
    public Object getMaximum() {
        return new Double(1);
    }
    
	public double evaluate(Vector x) {
		double sin_term;
		double exp_term;
		double product = 1.0;
		
		for (int i = 0; i < getDimension(); i++) {
			sin_term = 1.0;
			for (int k = 1; k <= sharpness; k++)
				sin_term *= Math.sin(l1*Math.PI*x.getReal(i) + l2);
			exp_term = Math.exp(-l3*((x.getReal(i)-l4)/l5)*((x.getReal(i)-l4)/l5));
			product *= (sin_term * exp_term); 
		}
		
		return product;
	}

}
