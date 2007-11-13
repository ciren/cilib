/**
 * 
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Generalized Ackley</b></p>
 * 
 * <p><b>Reference:</b> T.Back, <i>Evolutionary Algorithms in Theory and Practice</i>,
 * Oxford University Press, 1996</p>
 * 
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x_i in [-32.768,32.768]</li>
 * </ul>
 * </p>
 * 
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * 
 * @author Edwin Peer
 * @author Olusegun Olorunda
 *
 */
public class Ackley extends ContinuousFunction {
	private static final long serialVersionUID = -7803711986955989075L;

	public Ackley() {
        setDomain("R(-32.768, 32.768)^30");
    }
	
	public Object getMinimum() {
        return new Double(0.0);
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
	        return -20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / getDimension())) - Math.exp(sumcos / getDimension()) + 20 + Math.E;
	}

}
