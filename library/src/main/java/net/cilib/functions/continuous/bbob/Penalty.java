/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.bbob;

import net.cilib.functions.ContinuousFunction;
import net.cilib.type.types.container.Vector;
import net.cilib.controlparameter.ControlParameter;
import net.cilib.controlparameter.ConstantControlParameter;

/**
 * Implementation to penalise a candidate position.
 */
public class Penalty extends ContinuousFunction {
	private ControlParameter boundary;

    public Penalty() {
        this.boundary = ConstantControlParameter.of(0.0);
    }

    @Override
    public Double f(Vector input) {
        double sum = 0;

        for (int i = 0; i < input.size(); i++) {
            sum += Math.pow(Math.max(0, Math.abs(input.doubleValueOf(i)) - boundary.getParameter()), 2);
        }

        return sum;
    }

    /**
     * Sets the boundary value for the penalty function.
     * @param boundary
     */
    public void setBoundary(ControlParameter boundary) {
        this.boundary = boundary;
    }
}
