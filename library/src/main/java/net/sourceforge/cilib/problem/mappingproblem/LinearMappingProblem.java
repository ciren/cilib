/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.mappingproblem;

import net.sourceforge.cilib.type.types.container.Matrix;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class provides an adapter for implementation of NonlinearMapping
 * evaluation functions.  The class was written in order to make it
 * easier to write new ways of evaluating how effective a non-linear
 * mapping is, this allows for comparing different methods of evaluation.
 * <p>
 * The reason the mapping is non-linear because it is not reversible.
 */
public class LinearMappingProblem extends MappingProblem {
    private static final long serialVersionUID = -8250226009646654027L;

    public LinearMappingProblem() {

    }

    public LinearMappingProblem(LinearMappingProblem copy) {
        super(copy);
    }

    public LinearMappingProblem getClone() {
        return new LinearMappingProblem(this);
    }

    /**
     * This function performs the actual mapping.  It is only used by this
     * class and should never be required by any of the subclasses.
     * <p>
     * The dimension of the input vector (M) must always be greater than the
     * dimension of the output vector (D).  This is not checked for as the
     * function will not break should this not hold - it just doesn't make
     * sense.
     * <p>
     * The structure of the matrix is a typical "C" convention, how the data
     * is stored depends on whether you look at the input and output vectors
     * as row or column vectors.  In the case of column vectors the indexes
     * into the matrix would go across with 0 at the top left, (M-1) top-right,
     * M*(D-1) at the bottom left and (M*D-1) at the bottom right.  In the
     * case where you are working with row-vectors simply this whole scheme
     * just transposes.  In the case of row-vectors this is rowvector * matrix,
     * in the case of column vectors this is matrix * columnvector.
     *
     * @param input     the input vector.
     * @param matrix    the matrix to be used for the mapping.
     * @param output    the vector into which the output should be placed.
     */
    protected final void performMapping(Matrix input, Vector matrix, Matrix output) {
        int outputDimension = getOutputDim(); // D
        int inputDimension = getInputDim(); // M
        int numberOfVectors = getNumInputVectors(); // N

        for(int v = 0; v < numberOfVectors; v++) {
            int base = 0;
            for(int d = 0; d < outputDimension; d++) {
//                output.set(v, d, 0.0);
                for(int m = 0; m < inputDimension; m++) {
                    double value = matrix.doubleValueOf(base+m) * input.valueAt(v, m);
                    //output[v][d] += matrix[base + m] * input[v][m];
//                    output.set(v, d, value);
                }
                base += inputDimension;
            }
        }
    }

    /**
     * Returns the dimension as required above.
     *
     * @return The dimension required for the formulae outputs = inputs * matrix.
     */
    public final int getMatrixSize() {
        return getInputDim() * getOutputDim();
    }
}
