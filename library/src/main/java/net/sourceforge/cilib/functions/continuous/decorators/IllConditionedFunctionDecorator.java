/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation to create an ill-conditioned function.
 * <p>
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 */
public class IllConditionedFunctionDecorator implements ContinuousFunction {

    private ContinuousFunction function;
    private ControlParameter alpha;

    public IllConditionedFunctionDecorator() {
        this.alpha = ConstantControlParameter.of(1.0);
    }

    @Override
    public Double apply(Vector input) {
        Vector.Builder builder = Vector.newBuilder();

        for (int i = 0; i < input.size(); i++) {
            double x = input.doubleValueOf(i);
            builder.add(x * Math.pow(alpha.getParameter(), i * 0.5 / (input.size() - 1)));
        }

        return function.apply(builder.build());
    }

    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    public ContinuousFunction getFunction() {
        return function;
    }

    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }

    public ControlParameter getAlpha() {
        return alpha;
    }

}
