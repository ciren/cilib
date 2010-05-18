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
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Gary Pampara
 *
 */
public class NoisyFunctionDecorator extends ContinuousFunction {

    private static final long serialVersionUID = -3918271655104447420L;
    private ContinuousFunction function;
    private ProbabilityDistributionFuction randomNumber;
    private ControlParameter variance;

    /**
     * Create an instance of the decorator and set the domain to "R" by default.
     */
    public NoisyFunctionDecorator() {
        setDomain("R");
        randomNumber = new GaussianDistribution();
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
    @Override
    public Double apply(Vector input) {
        return function.apply(input) + randomNumber.getRandomNumber(0.0, this.variance.getParameter());
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
