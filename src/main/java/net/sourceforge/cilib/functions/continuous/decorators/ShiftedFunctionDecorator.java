/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Function;

/**
 * ShiftedFunctionDecorator.
 *
 * Characteristics:
 *
 * Let c be a fixed positive number, then:
 *
 * Horizontal Shift:
 * If g(x) = f(x-c), then
 *             (c > 0) means that g(x) is f(x) shifted c units to the right
 *             (c < 0) means that g(x) is f(x) shifted c units to the left
 *
 * Vertical Shift:
 * If g(x) = f(x) + c, then
 *             (c > 0) means that g(x) is f(x) shifted c units upwards
 *             (c < 0) means that g(x) is f(x) shifted c units downwards
 *
 */
public class ShiftedFunctionDecorator implements ContinuousFunction {

    private static final long serialVersionUID = 8687711759870298103L;
    private ContinuousFunction function;
    private ControlParameter verticalShift;
    private ControlParameter horizontalShift;

    public ShiftedFunctionDecorator() {
        this.verticalShift = ConstantControlParameter.of(0.0);
        this.horizontalShift = ConstantControlParameter.of(0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Vector tmp = horizontalShift.getParameter() == 0.0 ? input : input.map(new Function<Numeric, Numeric>() {
            @Override
            public Numeric apply(Numeric x) {
                return Real.valueOf(x.doubleValue() - horizontalShift.getParameter());
            }
        });

        return function.apply(tmp) + verticalShift.getParameter();
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
    }

    /**
     * Get the horizontal shift (X-axis) associated with this function.
     * @return The horizontal shift in the X-axis
     */
    public ControlParameter getHorizontalShift() {
        return horizontalShift;
    }

    /**
     * Set the amount of horizontal shift to be applied to the function during evaluation.
     * @param horizontalShift The amount of horizontal shift.
     */
    public void setHorizontalShift(ControlParameter horizontalShift) {
        this.horizontalShift = horizontalShift;
    }

    /**
     * Get the vertical shift (Y-axis) associated with this function.
     * @return The vertical shift in the Y-axis
     */
    public ControlParameter getVerticalShift() {
        return verticalShift;
    }

    /**
     * Set the amount of vertical shift to be applied to the function during evaluation.
     * @param verticalShift the amount of vertical shift.
     */
    public void setVerticalShift(ControlParameter verticalShift) {
        this.verticalShift = verticalShift;
    }
}
