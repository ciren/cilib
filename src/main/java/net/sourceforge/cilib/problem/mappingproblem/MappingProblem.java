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

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MatrixDataSetBuilder;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Matrix;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * Abstract MappingProblem class that allows for implementing methods of
 * mapping high-dimensional data onto lower dimensions.  This class is abstract
 * and merely provides a nice skeleton, similar to the
 * OptimisationProblemAdapter.
 *
 * @author jkroon
 *
 * TODO: change this to use the MatrixDataSetBuilder correctly
 */
public abstract class MappingProblem extends OptimisationProblemAdapter {
    private static final long serialVersionUID = 8988100373800461079L;

    private int outputDimension = -1;
    private int inputDimension = -1;
    private int numvectors = -1;
    private Matrix inputs = null;
    private Matrix inpDistMatrix = null;
    private MappingEvaluator evaluator = null;
    private DistanceMeasure distanceMeasure = null;


    /**
     *
     *
     */
    public MappingProblem() {
        this.evaluator = new CurvilinearCompEvaluator();
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }


    /**
     * Calculates the fitness of the given matrix.  This wraps arounds the
     * {@see evaluateMapping} function.  It may call evaluateMapping multiple
     * times for every call to calculateFitness depending on the dataset.
     *
     * @param solution The solution to evaluate.  This must conform to the
     *        domain.
     *
     * @author jkroon
     */
    protected final Fitness calculateFitness(Type solution) {
        Vector matrix = (Vector) solution;

        Matrix distmatrix = null;
//        new Matrix<Double>(numvectors, numvectors);
        Matrix outputs = null;
//        new Matrix<Double>(numvectors, outputDimension);

        performMapping(inputs, matrix, outputs);

        matrix = null;

        Matrix.Builder builder = Matrix.builder();

        for(int a = 0; a < numvectors; a++) {
//            distmatrix.set(a, a, 0.0);
            for(int b = 0; b < a; b++) {
                double distance = this.distanceMeasure.distance(outputs.getRow(a), outputs.getRow(b));
//                distmatrix.set(a, b, distance);
//                distmatrix.set(b, a, distance);
            }
        }

        outputs = null;

        return evaluator.evaluateMapping(distmatrix);
    }

    /**
     * This function is there to perform the mapping.  It <b>must not</b>
     * alter the values in the inputs or distmatrix array.  It should place
     * the mapped to vectoers in the outputs array.  All arrays are
     * pre-allocated.
     *
     * The distmatrix is a single dimensional array - of the size specified by
     * getMatrixSize().
     *
     * @param inputs The input vectors - do not alter.
     * @param distmatrix The matrix as supplied by the Algorithm - do not alter.
     * @param outputs Place your resulting output vectors in here.
     *
     * @author jkroon
     */
    protected abstract void performMapping(Matrix inputs, Vector distmatrix, Matrix outputs);


    /**
     * This function should return the number of "doubles" required in the
     * matrix in order to perform the mapping.
     *
     * @return The size of the mapping matrix.
     *
     * @author jkroon
     */
    protected abstract int getMatrixSize();


    /**
     * Gets the value of M, the input dimension.
     *
     * @return The current value of M.
     *
     * @author jkroon
     */
    public final int getInputDim() {
        return inputDimension;
    }


    /**
     * Gets the value of D, the output dimension.
     *
     * @return The current value of D.
     *
     * @author jkroon
     */
    public final int getOutputDim() {
        return outputDimension;
    }


    /**
     * This function retrieves the number of input vectors that forms
     * part of the dataset.
     *
     * @author jkroon
     */
    protected final int getNumInputVectors() {
        return numvectors;
    }


    /**
     * This function sets the evaluator to use.
     *
     * @param The evaluator to use.
     *
     * @author jkroon
     */
    public final void setEvaluator(MappingEvaluator evaluator) {
        this.evaluator = evaluator;
        evaluator.setMappingProblem(this);
    }


    /**
     * This method is used during initialisation by the Simulator to provide us
     * with out DataSet.  This method loads the actual data from the DataSet.
     *
     * @param dataset The dataset from which to retrieve the data.
     *
     * @author jkroon
     *
     * TODO: Get this to work!!!
     *
     */
    public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {
        super.setDataSetBuilder(dataSetBuilder);

        MatrixDataSetBuilder matrixDataSetBuilder = (MatrixDataSetBuilder) dataSetBuilder;
        inputs = matrixDataSetBuilder.getMatrix();

        Matrix.Builder matrixBuilder = Matrix.builder().dimensions(numvectors, numvectors);

        for(int i = 0; i < numvectors; i++) {
            for(int j = 0; j < i; j++) {
                double distance = this.distanceMeasure.distance(inputs.getRow(i), inputs.getRow(j));
                matrixBuilder.valueAt(i, j, distance);
                matrixBuilder.valueAt(j, i, distance);
            }
        }

        inpDistMatrix = matrixBuilder.build();
    }

    /**
     * Retrieve the distance between the two given input vectors.
     *
     * @param i1 Index of the first vector
     * @param i2 Index of the second vector
     *
     * @return the distance between the two vectors.
     *
     * @author jkroon
     */
    public final double getDistanceInputVect(int i1, int i2) {
        return inpDistMatrix.valueAt(i2, i1);
    }


}
