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

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * ShiftedFunctionDecorator.
 *
 * Characteristics:
 *
 * Let c be a fixed positive number, then:
 *
 * Horizontal Shift:
 * If g(x) = f(x+c), then
 *             (c > 0) means that g(x) is f(x) shifted c units to the left
 *             (c < 0) means that g(x) is f(x) shifted c units to the right
 *
 * Vertical Shift:
 * If g(x) = f(x) + c, then
 *             (c > 0) means that g(x) is f(x) shifted c units upwards
 *             (c < 0) means that g(x) is f(x) shifted c units downwards
 *
 * @author Olusegun Olorunda
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ShiftedFunctionDecorator getClone() {
        return new ShiftedFunctionDecorator();
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        Double functionMinimum = function.getMinimum();
        return new Double(functionMinimum + verticalShift);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        Vector tmp = new Vector();

        for (int i = 0; i < input.getDimension(); i++) {
            tmp.add(new Real(input.getReal(i) + horizontalShift));
        }

        return function.evaluate(tmp) + verticalShift;
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
     * Get the horizontal shift (X-axis) associated with this function.
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
     * Get the vertical shift (Y-axis) associated with this function.
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
