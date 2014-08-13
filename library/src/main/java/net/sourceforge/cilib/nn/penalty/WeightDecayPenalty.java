/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * An implementation of the Weight Decay penalty function and its derivative.
 * Weight decay is defined as the sum of squared weights multiplied by parameter
 * lambda. Lambda should be optimised empirically.
 * <p>
 * References:
 * <p><ul><li>
 * Moody, J. E., et al. "A simple weight decay can improve generalization." 
 * Advances in neural information processing systems 4 (1995): 950-957.
 * </li><li>
 * Hinton, Geoffrey E. "Learning translation invariant recognition in a massively parallel networks." 
 * PARLE Parallel Architectures and Languages Europe. Springer Berlin Heidelberg, 1987.
 * </li></ul>
 * 
 */
public class WeightDecayPenalty extends NNPenalty {

    public WeightDecayPenalty() {
        lambda = ConstantControlParameter.of(1e-5);
    }
    
    @Override
    public double calculatePenalty(Type solution) {
        Vector weights = (Vector) solution;
        double weightSum = 0;
        for(Numeric weight : weights) {
            weightSum += Math.pow(weight.doubleValue(),2);
        }
        return lambda.getParameter() * weightSum / 2;
    }

    @Override
    public double calculatePenaltyDerivative(double weight) {
        return weight*lambda.getParameter();
    }
    
}
