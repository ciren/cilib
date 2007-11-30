/**
 * 
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Olusegun Olorunda
 *
 */
public class AbsoluteValue extends ContinuousFunction {
	private static final long serialVersionUID = 1662988096338786773L;

	public AbsoluteValue() {
		setDomain("R(-100, 100)^30");
	}
	
	@Override
	public AbsoluteValue getClone() {
		return new AbsoluteValue();
	}

	public Object getMinimum() {
		return new Double(0);
	 }
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.functions.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
	 */
	@Override
	public double evaluate(Vector x) {
		double tmp = 0;
		for (int i = 0; i < getDimension(); ++i) {
			tmp += Math.abs(x.getReal(i));
		}
		return tmp;
	}

}
