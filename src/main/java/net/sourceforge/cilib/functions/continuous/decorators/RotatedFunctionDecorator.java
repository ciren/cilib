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
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Sequence;

/**
 *
 * Rotates a n-dimensional continuous function by multiplying it with
 * an orthonormal basis for R^n.
 */
public class RotatedFunctionDecorator implements ContinuousFunction {

    private static final long serialVersionUID = 3107473364744861153L;
    private ContinuousFunction function;
    private double[][] rotationMatrix;
    /**
     * Specifies a probability that determines whether the rotationMatrix should be
     * re-created for a particular function evaluation.
     *
     * Default value is 0.5.
     */
    private ControlParameter rotationProbability;

    public RotatedFunctionDecorator() {
        rotationMatrix = null;
        rotationProbability = ConstantControlParameter.of(0.5);
    }

    /**
     * Multiplies the argument vector, x, by the transpose of the rotation matrix
     * stores the result in rotatedX and calls the evaluate method of the function
     * being decorated with the rotated vector as the parameter.
     */
    @Override
    public Double apply(final Vector input) {
        ProbabilityDistributionFuction rotateOrNot = new UniformDistribution();

        if (rotationMatrix == null || rotateOrNot.getRandomNumber() < rotationProbability.getParameter()) {
            setRotationMatrix(input.size());
        }

        Vector rotatedX = Vector.copyOf(Sequence.repeat(0.0, input.size()));

        for (int j = 0; j < input.size(); j++) {
            for (int i = 0; i < input.size(); i++) {
                rotatedX.setReal(j, rotatedX.doubleValueOf(j) + input.doubleValueOf(i) * rotationMatrix[i][j]);
            }
        }

        return function.apply(rotatedX);
    }

    /**
     * Initializes the rotation matrix with random values normally distributed
     * with a mean of 0.0 and a standard deviation of 1.0 and then applies the
     * numerically stable version of the Gram-Schmidt Process to it's columns
     * to generate an orthonormal spaning set for R.
     *
     * The process is outlined as follows for each n-dimensional column vector of the
     * rotation matrix (represented here by column_i where i e [0, n]):
     *
     * 1) draw components of column_i from a gaussian distribution with mean 0.0 and
     *    sigma 1.0
     * 2) column_i = column_i - projection, where:
     *    projection = SUM( INNER_PRODUCT(column_i, column_j) * column_j )
     *    with j e [0, i-1]
     * 3) column_i = column_i/|column_i|
     *
     */
    private void initializeMatrix(int dimension) {
        /*
         * set up a spanning set of gaussian random numbers
         */
        rotationMatrix = new double[dimension][dimension];
        ProbabilityDistributionFuction initializer = new GaussianDistribution();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                rotationMatrix[i][j] = initializer.getRandomNumber();
            }
        }

        /*
         * generate an orthonormal spanning set from the above spanning set by
         * replacing each column vector with it's orthonormal equivalent
         *
         * NOTE: matrix representation is actually the 'transpose' of a 2-dimensional
         *       java array. Hence, rotationMatrix[i] is used as a column vector,
         *       instead of a row vector. This means that:
         *
         *       e_13 (math) = rotationMatrix[3][1] (java)
         *
         *       This makes for somewhat easier array representation and operations.
         */
        for (int i = 0; i < dimension; i++) {

            /*
             * create a vector to store the result of projecting the current column on
             * column_j, where j e [0, i-1]
             */
            double[] projection = new double[dimension];
            for (int contents = 0; contents < dimension; contents++) {
                projection[contents] = 0;
            }

            /*
             * set up the projection vector
             */
            for (int j = 0; j < i; j++) {
                double[] column_j = rotationMatrix[j];
                double innerProduct = 0.0;

                /*
                 * calculate inner product for the current vector and column_j
                 */
                for (int contents = 0; contents < dimension; contents++) {
                    innerProduct += (rotationMatrix[i][contents] * column_j[contents]);
                }

                /*
                 * multiply column_j by above inner product and add to the projection
                 */
                for (int contents = 0; contents < dimension; contents++) {
                    projection[contents] += (column_j[contents] * innerProduct);
                }
            }

            /*
             * subtract the projection from the column to be replaced and normalize
             */
            for (int contents = 0; contents < dimension; contents++) {
                rotationMatrix[i][contents] -= projection[contents];
            }

            /*
             * evaluate the norm of the current rotation matrix column to be replaced
             */
            double columnNorm = 0.0;

            for (int contents = 0; contents < dimension; contents++) {
                columnNorm += rotationMatrix[i][contents] * rotationMatrix[i][contents];
            }

            columnNorm = Math.sqrt(columnNorm);

            /*
             * normalize the vector
             */
            for (int contents = 0; contents < dimension; contents++) {
                rotationMatrix[i][contents] /= columnNorm;
            }
        }

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
    public double[][] getRotationMatrix() {
        return rotationMatrix;
    }

    /**
     * @param rotationMatrix the rotationMatrix to set
     */
    public void setRotationMatrix(int dimension) {
        initializeMatrix(dimension);
    }

    /**
     * @return the rotationProbability
     */
    public ControlParameter getRotationProbability() {
        return rotationProbability;
    }

    /**
     * @param rotationProbability the rotationProbability to set
     */
    public void setRotationProbability(ControlParameter rotationProbability) {
        this.rotationProbability = rotationProbability;
    }
}
