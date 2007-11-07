package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/** 
 * Characteristics:
 * 
 * Let c be a fixed positive number
 * 
 * Horizontal Scaling: If g(x) = f(cx), then (c > 1) means that g(x) is f(x)
 * compressed in the horizontal direction by a factor of c (0 < c < 1) means
 * that g(x) is f(x) stretched in the horizontal direction by a factor of 1/c
 * 
 * Vertical Scaling: If g(x) = cf(x), then (c > 1) means that g(x) is f(x)
 * stretched in the vertical direction by a factor of c (0 < c < 1) means that
 * g(x) is f(x) compressed in the vertical direction by a factor of 1/c
 * 
 * @author Olusegun Olorunda
 */
public class ScaledFunctionDecorator extends ContinuousFunction {
	private static final long serialVersionUID = -5316734133098401441L;
	private ContinuousFunction function;
	private double verticalScale;
	private double horizontalScale;

	public ScaledFunctionDecorator() {
		setDomain("R");
		verticalScale = 1.0;
		horizontalScale = 1.0;
	}

	public Object getMinimum() {
		// adds the value of the verticalShift to the original function minimum
		return new Double(((Double) function.getMinimum()).doubleValue() * verticalScale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.cilib.functions.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
	 */
	@Override
	public double evaluate(Vector x) {
		for (int i = 0; i < x.getDimension(); i++) {
			x.setReal(i, (horizontalScale * x.getReal(i)));
		}

		return (verticalScale * function.evaluate(x));
	}

	/**
	 * @return the function
	 */
	public ContinuousFunction getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(ContinuousFunction function) {
		this.function = function;
		this.setDomain(function.getDomainRegistry().getDomainString());
	}

	/**
	 * @return the horizontalScale
	 */
	public double getHorizontalScale() {
		return horizontalScale;
	}

	/**
	 * @param horizontalScale
	 *            the horizontalScale to set
	 */
	public void setHorizontalScale(double horizontalScale) {
		if (horizontalScale <= 0)
			throw new InitialisationException("Horizontal scale factor must be greater than zero!");

		this.horizontalScale = horizontalScale;
	}

	/**
	 * @return the verticalScale
	 */
	public double getVerticalScale() {
		return verticalScale;
	}

	/**
	 * @param verticalScale
	 *            the verticalScale to set
	 */
	public void setVerticalScale(double verticalScale) {
		if (verticalScale <= 0)
			throw new InitialisationException("Vertical scale factor must be greater than zero!");

		this.verticalScale = verticalScale;
	}

}