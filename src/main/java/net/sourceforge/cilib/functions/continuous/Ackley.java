/**
 * 
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The Ackley function
 * 
 * Minimum: f(x) = 0;  x = (0, 0, ...., 0)
 * 
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Not seperable</li>
 * <li>Regular</li>
 * </ul>
 * 
 * @author  Edwin Peer
 * @author Olusegun Olorunda
 *
 */
public class Ackley extends ContinuousFunction {

	public Ackley() {
        setDomain("R(-30, 30)^30");
    }
	
	public Object getMinimum() {
        return new Double(0);
    }
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.functions.redux.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
	 */
	@Override
	public double evaluate(Vector x) {
		 double sumsq = 0.0;
	        double sumcos = 0.0;
	        for (int i = 0; i < getDimension(); ++i) {
	        	sumsq += x.getReal(i) * x.getReal(i);
	            sumcos += Math.cos(2 * Math.PI * x.getReal(i));
	        }
	        return - 20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / getDimension())) - Math.exp(sumcos / getDimension()) + 20 + Math.E;
	}

}
