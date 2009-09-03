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
package net.sourceforge.cilib.problem.mappingproblem;

import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.Matrix;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class provides an adapter for implementation of NonlinearMapping
 * evaluation functions.  The class was written in order to make it
 * easier to write new ways of evaluating how effective a non-linear
 * mapping is, this allows for comparing different methods of evaluation.
 *
 * The reason the mapping is non-linear because it is not reverseable.
 *
 * @author jkroon
 */
public class LinearMappingProblem extends MappingProblem {
    private static final long serialVersionUID = -8250226009646654027L;

    public LinearMappingProblem() {

    }

    public LinearMappingProblem(LinearMappingProblem copy) {

    }

    public LinearMappingProblem getClone() {
        return new LinearMappingProblem(this);
    }

    /**
     * This function performs the actual mapping.  It is only used by this
     * class and should never be required by any of the subclasses, who
     * should only be implementing the {@see evaluateMapping} function.
     *
     * The dimension of the input vector (M) must always be greater than the
     * dimension of the output vector (D).  This is not checked for as the
     * function will not break should this not hold - it just doesn't make
     * sense.
     *
     * The structure of the matrix is a typical "C" convention, how the data
     * is stored depends on whether you look at the input and output vectors
     * as row or column vectors.  In the case of column vectors the indexes
     * into the matrix would go accross with 0 at the top left, (M-1) top-right,
     * M*(D-1) at the bottom left and (M*D-1) at the bottom right.  In the
     * case where you are working with row-vectors simply this whole scheme
     * just transposes.  In the case of row-vectors this is rowvector * matrix,
     * in the case of column vectors this is matrix * columnvector.
     *
     * @param input The input vector.
     * @param matrix The matrix to be used for the mapping.
     * @param output The vector into which the output should be placed.
     *
     * @author jkroon
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
                    double value = matrix.getReal(base+m) * input.valueAt(v, m);
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
     *
     * @author jkroon
     */
    public final int getMatrixSize() {
        return getInputDim() * getOutputDim();
    }

    public DomainRegistry getDomain() {
        // TODO Auto-generated method stub
        return null;
    }

}
