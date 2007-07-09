package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The Damavandi function obtained from N. Damavandi, S. Safavi-Naeini,
 * "A hybrid evolutionary programming method for circuit optimization"
 * 
 * Global Minimum: f(x,y) = 0;  (x,y) = (2, 2)
 * Local Minimum: f(x,y) = 2; (x,y) = (7, 7)
 * 
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Deceptive</li>
 * </ul>
 * 
 * @author  Andries Engelbrecht
 */

public class Damavandi extends ContinuousFunction {
	private static final long serialVersionUID = 2857754134712271398L;

	public Damavandi() {
        setDomain("R(0, 12)^2");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
	public double evaluate(Vector x) {
		double x1 = x.getReal(0);
		double x2 = x.getReal(1);
		
		double numerator = Math.sin(Math.PI*(x1-2))*Math.sin(Math.PI*(x2-2));
		double denumerator = Math.PI*Math.PI*(x1-2)*(x2-2);
		double factor1 = 1 - Math.pow(Math.abs(numerator/denumerator),5);
		double factor2 = 2 + (x1-7)*(x1-7) + 2*(x2-7)*(x2-7);
		
		return factor1*factor2;
	}

}
