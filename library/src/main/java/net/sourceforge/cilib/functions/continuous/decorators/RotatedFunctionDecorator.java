/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Matrix;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Matrices;

/**
 * A function decorator that rotates a given function by a random orthonormal
 * matrix or a linear transformation matrix.
 */
public class RotatedFunctionDecorator implements ContinuousFunction {

    private static final long serialVersionUID = 3107473364744861153L;
    private ContinuousFunction function;
    private Matrix rotationMatrix;
    private boolean initialised;
    private MatrixType type;
    private int condition;

    public enum MatrixType {
        IDENTITY,
        ORTHONORMAL,
        LINEAR_TRANSFORMATION
    }

    public RotatedFunctionDecorator() {
        this.rotationMatrix = null;
        this.type = MatrixType.ORTHONORMAL;
        this.initialised = false;
        this.condition = 1;
    }

    /**
     * Multiplies the argument vector, x, by the transpose of the rotation
     * matrix, stores the result in rotatedX and calls the evaluate method of
     * the function being decorated with the rotated vector as the parameter.
     */
    @Override
    public Double apply(final Vector input) {
        if (type == MatrixType.IDENTITY) {
            return function.apply(input);
        }

        if (!initialised || input.size() != rotationMatrix.getRows()) {
            setRotationMatrix(input.size());
            initialised = true;
        }

        Vector rotatedX = Vector.fill(0.0, input.size());

        for (int j = 0; j < input.size(); j++) {
            for (int i = 0; i < input.size(); i++) {
                rotatedX.setReal(j, rotatedX.doubleValueOf(j)
                    + input.doubleValueOf(i) * rotationMatrix.valueAt(i, j));
            }
        }

        return function.apply(rotatedX);
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
     * @return the rotationMatrix
     */
    public Matrix getRotationMatrix() {
        return rotationMatrix;
    }

    /**
     * @param size the size of the rotationMatrix.
     */
    public void setRotationMatrix(int size) {
        switch(type) {
            case IDENTITY:
                rotationMatrix = Matrices.getIdentityMatrix(size);
                break;
            case ORTHONORMAL:
                rotationMatrix = Matrices.getRandomOrthonormalMatrix(size);
                break;
            case LINEAR_TRANSFORMATION:
                rotationMatrix = Matrices.getRandomLinearTransformationMatrix(size, condition);
                break;
        }
    }

    public void setMatrixType(String type) {
        if ("identity".equalsIgnoreCase(type)) {
            this.type = MatrixType.IDENTITY;
        } else if ("orthonormal".equalsIgnoreCase(type)) {
            this.type = MatrixType.ORTHONORMAL;
        } else if ("linear_transformation".equalsIgnoreCase(type)) {
            this.type = MatrixType.LINEAR_TRANSFORMATION;
        }else {
            throw new IllegalArgumentException("Unknown matrix type. Must be 'identity' or 'orthonormal'");
        }
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
