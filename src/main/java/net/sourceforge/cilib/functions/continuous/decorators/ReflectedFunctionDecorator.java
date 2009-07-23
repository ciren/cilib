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
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Class to reflect the wrapped {@linkplain Function} in a horizontal or
 * vertical fashion.
 *
 * Characteristics:
 *
 * Sets f(x) to f(-x) or -f(x) or -f(-x) based on what is required, by
 * reflecting on a specific axis.
 *
 * Setting values in xml works the same as setting string values
 *
 * @author Olusegun Olorunda
 *
 */
public class ReflectedFunctionDecorator extends ContinuousFunction {
    private static final long serialVersionUID = -5042848697343918398L;
    private ContinuousFunction function;
    private boolean horizontalReflection;
    private boolean verticalReflection;

    public ReflectedFunctionDecorator() {
        setDomain("R");
        horizontalReflection = false;
        verticalReflection = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReflectedFunctionDecorator getClone() {
        return new ReflectedFunctionDecorator();
    }

    public Double getMinimum() {
        // adds the value of the verticalShift to the original function minimum
        return function.getMinimum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        Vector tmp = input.getClone();

        if (horizontalReflection) {
            for (int i = 0; i < input.getDimension(); i++) {
                tmp.setReal(i, -input.getReal(i));
            }
        }

        if(verticalReflection)
            return -function.evaluate(tmp);

        return function.evaluate(tmp);
    }

    /**
     * Get the decorated function contained by this instance.
     * @return the function
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Set the wrapped function.
     * @param function the function to set.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
        this.setDomain(function.getDomainRegistry().getDomainString());
    }

    /**
     * Get the horizonal reflection.
     * @return the horizontalReflection.
     */
    public boolean getHorizontalReflection() {
        return horizontalReflection;
    }

    /**
     * Invoking this method sets horizontalReflection to true.
     */
    public void setHorizontalReflection(boolean horizontalReflection) {
        this.horizontalReflection = horizontalReflection;
    }

    /**
     * Invoking this method sets horizontalReflection to true.
     */
    public void setHorizontalReflection(String horizontalReflection) {
        this.horizontalReflection = Boolean.parseBoolean(horizontalReflection);
    }

    /**
     * Get the vertical reflection.
     * @return the verticalReflection.
     */
    public boolean getVerticalReflection() {
        return verticalReflection;
    }

    /**
     * Invoking this method sets verticalReflection to true.
     */
    public void setVerticalReflection(boolean verticalReflection) {
        this.verticalReflection = verticalReflection;
    }

    /**
     * Invoking this method sets verticalReflection to true.
     */
    public void setVerticalReflection(String verticalReflection) {
        this.verticalReflection = Boolean.parseBoolean(verticalReflection);
    }

}
