/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.hybrid;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is a container class to store information about individual functions used
 * in hybrid composite functions of the CEC2005 benchmark functions. Rotation and 
 * shifting is done through here rather than using separate decorator classes.
 * <p>
 * Parameters that must be set:
 * </p>
 * <ul>
 * <li>sigma</li>
 * <li>lambda: scaling factor</li>
 * <li>horizontalShift: shifting of the optimum</li>
 * <li>bias: vertical shifting</li>
 * <li>function: the optimization function that makes up the overall function</li>
 * </ul>
 * <p>
 * When adding functions to the HybridCompositionFunction make sure the horizontalShift
 * </p>
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and Tiwari, S. (2005).
 * Problem Definitions and Evaluation Criteria for the CEC 2005 Special Session on Real-Parameter Optimization.
 * Natural Computing, 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 */
public class SingleFunction implements ContinuousFunction {
    private ContinuousFunction function;
    private RotatedFunctionDecorator rotationFunction;
    private double sigma;
    private double weight;
    private double lambda;
    private double horizontalShift;
    private double fmax;
    private double bias;
    private Vector shifted; //A temporary vector to hold the shifted input
    private Vector shiftVector;
    private boolean initialised;
    private boolean randomShift;
    
    /**
     * Default constructor.
     */
    public SingleFunction() {
        this.initialised = false;
        this.rotationFunction = new RotatedFunctionDecorator();
        this.sigma = 1.0;
        this.lambda = 1.0;
        this.horizontalShift = 0.0;
        this.bias = 0.0;
        this.randomShift = false;
        this.shiftVector = null;
    }

    /*
     * Getters and setters for the parameters
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getSigma() {
        return sigma;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getLambda() {
        return lambda;
    }

    public void setFunction(ContinuousFunction function) {
        this.function = function;
        this.rotationFunction.setFunction(function);
    }

    public ContinuousFunction getFunction() {
        return function;
    }

    public void setHorizontalShift(double horizontalShift) {
        this.horizontalShift = horizontalShift;
    }

    public double getHorizontalShift() {
        return horizontalShift;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void setfMax(double fmax) {
        this.fmax = fmax;
    }

    public double getfMax() {
        return fmax;
    }    

    public void setShifted(Vector shifted) {
        this.shifted = shifted;
    }

    public Vector getShifted() {
        return shifted;
    }

    public void setRandomShift(boolean randomShift) {
        this.randomShift = randomShift;
    }

    public boolean getRandomShift() {
        return randomShift;
    }
    
    /**
     * Sets the rotation matrix type.
     * @param type Either "identity" or "orthonormal"
     */
    public void setMatrixType(String type) {
        rotationFunction.setMatrixType(type);
    }
    
    /**
     * Sets the condition for the linear transformation matrix if it's used.
     * @param condition The condition of the matrix.
     */
    public void setCondition(int condition) {
        rotationFunction.setCondition(condition);
    }
    
    /**
     * Shifts the input vector.
     * @param input 
     */
    public void shift(Vector input) {
        if (shiftVector == null) {
            if (randomShift) {
                shiftVector = Vector.newBuilder().copyOf(input).buildRandom();
            } else {
                shiftVector = Vector.fill(horizontalShift, input.size());
            }
        }
        setShifted(input.subtract(shiftVector));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        //need to get input's size to set fMax
        if (!initialised) {
            setfMax(Math.abs(rotationFunction.apply(Vector.fill(5.0, input.size()).divide(lambda))));
            initialised = true;
        }

        return rotationFunction.apply(shifted.divide(lambda)) / getfMax();
    }
}
