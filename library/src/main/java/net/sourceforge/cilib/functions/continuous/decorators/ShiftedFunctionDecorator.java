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
    private Vector shiftVector;
    private boolean randomShift;

    public ShiftedFunctionDecorator() {
        this.verticalShift = ConstantControlParameter.of(0.0);
        this.horizontalShift = ConstantControlParameter.of(0.0);
        this.shiftVector = null;
        this.randomShift = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        if (randomShift) {
            if (shiftVector == null || input.size() != shiftVector.size()) {
                shiftVector = Vector.newBuilder().copyOf(input).buildRandom();
            }
        } else {
            shiftVector = Vector.fill(horizontalShift.getParameter(), input.size());
        }
        
        return function.apply(input.subtract(shiftVector)) + verticalShift.getParameter();
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

    public void setRandomShift(boolean randomShift) {
        this.randomShift = randomShift;
    }

    public boolean getRandomShift() {
        return randomShift;
    }
}
