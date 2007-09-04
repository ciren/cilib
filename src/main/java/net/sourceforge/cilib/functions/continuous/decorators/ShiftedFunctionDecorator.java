/**
 * 
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Olusegun Olorunda
 * 
 * Characteristics:
 * 
 * f(x) = verticalShift
 * x = (x_i - horizontalShift | i e [1,n])
 *
 */
public class ShiftedFunctionDecorator extends ContinuousFunction {
	private static final long serialVersionUID = 8687711759870298103L;
	private ContinuousFunction function;
	private double verticalShift;
	private double horizontalShift;
	
	public ShiftedFunctionDecorator() {
		setDomain("R");
		verticalShift = 0.0;
		horizontalShift = 0.0;
	}
	
	public Object getMinimum() {
        return new Double(verticalShift);
    }

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.functions.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
	 */
	@Override
	public double evaluate(Vector x) {
		for (int i = 0; i < x.getDimension(); i++) {
			x.setReal(i, x.getReal(i) + horizontalShift);
		}
		
		return function.evaluate(x) + verticalShift;
	}

	/**
	 * @return the function
	 */
	public ContinuousFunction getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(ContinuousFunction function) {
		this.function = function;
		this.setDomain(function.getDomainRegistry().getDomainString());
	}

	/**
	 * Get the horizontal shift (X-axis) associated with this function
	 * @return The horizontal shift in the X-axis
	 */
	public double getHorizontalShift() {
		return horizontalShift;
	}

	/**
	 * Set the amount of horizontal shift to be applied to the function during evaluation.
	 * @param horizontalShift The amount of horizontal shift.
	 */
	public void setHorizontalShift(double horizontalShift) {
		this.horizontalShift = horizontalShift;
	}

	/**
	 * Get the vertical shift (Y-axis) associated with this function
	 * @return The vertical shift in the Y-axis
	 */
	public double getVerticalShift() {
		return verticalShift;
	}

	/**
	 * Set the amount of vertical shift to be applied to the function during evaluation.
	 * @param verticalShift the amount of vertical shift.
	 */
	public void setVerticalShift(double verticalShift) {
		this.verticalShift = verticalShift;
	}

}
