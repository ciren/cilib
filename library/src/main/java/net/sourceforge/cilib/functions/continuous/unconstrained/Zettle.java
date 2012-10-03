/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Diego Siqueira
 */
public class Zettle implements ContinuousFunction {
    
	@Override
	public Double apply(Vector input) {
		double x1 = input.get(0).doubleValue();
        double x2 = input.get(1).doubleValue();
     
        return (Math.pow(((x1 * x1) + (x2 * x2) - 2*x1), 2) + 0.25*x1);   
	}
}
