/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Characteristics:
 *
 * Let c be a fixed positive number.
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

    /**
     * Create an instance of the decorator. Domain is set to "R" by default.
     */
    public ScaledFunctionDecorator() {
        setDomain("R");
        verticalScale = 1.0;
        horizontalScale = 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScaledFunctionDecorator getClone() {
        return new ScaledFunctionDecorator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMinimum() {
        // adds the value of the verticalShift to the original function minimum
        return Double.valueOf(function.getMinimum().doubleValue() * verticalScale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        Vector tmp = input.getClone();

        for (int i = 0; i < input.getDimension(); i++) {
            tmp.setReal(i, (horizontalScale * input.getReal(i)));
        }

        return (verticalScale * function.evaluate(tmp));
    }

    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
        this.setDomain(function.getDomainRegistry().getDomainString());
    }

    /**
     * Get the horizontal scale value.
     * @return The horizontal scale value.
     */
    public double getHorizontalScale() {
        return horizontalScale;
    }

    /**
     * Set the value of the horizontal scale.
     * @param horizontalScale The value of the horizontal scale.
     */
    public void setHorizontalScale(double horizontalScale) {
        if (horizontalScale <= 0)
            throw new InitialisationException("Horizontal scale factor must be greater than zero!");

        this.horizontalScale = horizontalScale;
    }

    /**
     * Get the value of the vertical scale.
     * @return The vertical scale value.
     */
    public double getVerticalScale() {
        return verticalScale;
    }

    /**
     * Set the value of the vertical scale.
     * @param verticalScale The vertical scale to use.
     */
    public void setVerticalScale(double verticalScale) {
        if (verticalScale <= 0)
            throw new InitialisationException("Vertical scale factor must be greater than zero!");

        this.verticalScale = verticalScale;
    }

}
