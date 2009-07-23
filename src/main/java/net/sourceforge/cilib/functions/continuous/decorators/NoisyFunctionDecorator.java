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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 *
 * @author Gary Pampara
 *
 */
public class NoisyFunctionDecorator extends ContinuousFunction {
    private static final long serialVersionUID = -3918271655104447420L;

    private ContinuousFunction function;
    private RandomNumber randomNumber;
    private ControlParameter variance;

    /**
     * Create an instance of the decorator and set the domain to "R" by default.
     */
    public NoisyFunctionDecorator() {
        setDomain("R");
        randomNumber = new RandomNumber();
        this.variance = new ConstantControlParameter(1.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NoisyFunctionDecorator getClone() {
        return new NoisyFunctionDecorator();
    }

    /**
     * {@inheritDoc}
     */
    public Double evaluate(Vector input) {
        return function.evaluate(input) + randomNumber.getGaussian(0.0, this.variance.getParameter());
    }


    /**
     * Get the function that is decorated.
     * @return Returns the noisyFunction.
     */
    public ContinuousFunction getFunction() {
        return function;
    }


    /**
     * Set the decorated function.
     * @param function The function to decorate.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
        this.setDomain(function.getDomainRegistry().getDomainString());
    }

    public ControlParameter getVariance() {
        return variance;
    }

    public void setVariance(ControlParameter variance) {
        this.variance = variance;
    }



}
