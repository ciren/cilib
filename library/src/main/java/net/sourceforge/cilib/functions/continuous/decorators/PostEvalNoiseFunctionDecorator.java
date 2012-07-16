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
 * Adds noise to result of an evaluated function.
 * The noise is added as follows:
 * functionResult * (offset + scale * |Gaussian(0,1)|)
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and Tiwari, S. (2005).
 * Problem Definitions and Evaluation Criteria for the CEC 2005 Special Session on Real-Parameter Optimization.
 * Natural Computing, 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 */
public class PostEvalNoiseFunctionDecorator implements ContinuousFunction {

    private ContinuousFunction function;
    private ProbabilityDistributionFuction randomNumber;
    private ControlParameter variance;
    private ControlParameter scale;
    private ControlParameter offset;

    /**
     * Default constructor.
     */
    public PostEvalNoiseFunctionDecorator() {
        this.randomNumber = new GaussianDistribution();
        this.variance = ConstantControlParameter.of(1.0);
        this.scale = ConstantControlParameter.of(0.4);
        this.offset = ConstantControlParameter.of(1.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        return function.apply(input) * (offset.getParameter() + scale.getParameter() * Math.abs(randomNumber.getRandomNumber(0.0, this.variance.getParameter())));
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
    }

    /**
     * Gets the variance of the noise.
     * @return 
     */
    public ControlParameter getVariance() {
        return variance;
    }

    /**
     * Sets the variance of the noise.
     * @param variance 
     */
    public void setVariance(ControlParameter variance) {
        this.variance = variance;
    }
    
    /**
     * Gets the scale of the noise.
     * @return 
     */
    public ControlParameter getScale() {
        return scale;
    }

    /**
     * Sets the scale of the noise.
     * @param noiseScale 
     */
    public void setScale(ControlParameter noiseScale) {
        this.scale = noiseScale;
    }

    /**
     * Sets the offset of the noise.
     * @param offset 
     */
    public void setOffset(ControlParameter offset) {
        this.offset = offset;
    }

    /**
     * Gets the offset of the noise.
     * @return 
     */
    public ControlParameter getOffset() {
        return offset;
    }
}
