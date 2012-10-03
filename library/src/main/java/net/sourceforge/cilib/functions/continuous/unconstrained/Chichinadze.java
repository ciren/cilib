package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

public class Chichinadze implements ContinuousFunction {

	@Override
	public Double apply(Vector input) {
		double x1 = input.get(0).doubleValue();
		double x2 = input.get(1).doubleValue();
		
		double cos = Math.cos(Math.PI * x1 / 2);
		double sin = Math.sin(5*Math.PI*x1);
		double ePow = Math.exp(-0.5 * ((x2 - 0.5) * (x2 - 0.5))); 
		
		return (x1*x1) - 12 * (x1) + 11 + 10*cos + 8 * sin - Math.pow((1/5.0), 0.5)* ePow;
	}

}
