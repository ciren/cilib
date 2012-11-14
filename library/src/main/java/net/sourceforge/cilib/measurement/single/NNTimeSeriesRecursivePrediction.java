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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Calculates the MSE generalization error of the best solution of an algorithm
 * optimizing a {@link NNDataTrainingProblem}.
 */
public class NNTimeSeriesRecursivePrediction implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
    private int n = 1; // specifies the extent of recursiveness. Can't be bigger than embedding.
    /**
     * {@inheritDoc }
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * 
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable dataSet = new StandardPatternDataTable();
        StandardPatternDataTable trainingSet = problem.getTrainingSet();
        StandardPatternDataTable generalizationSet = problem.getGeneralizationSet();
        StandardPatternDataTable validationSet = problem.getValidationSet();
        for (StandardPattern pattern : trainingSet) {
            dataSet.addRow(pattern);
        }
        for (StandardPattern pattern : generalizationSet) {
            dataSet.addRow(pattern);
        }
        for (StandardPattern pattern : validationSet) {
            dataSet.addRow(pattern);
        }

        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        Builder builder = Vector.newBuilder(); // final solution vector
        Vector predicted = Vector.of(); // predicted stuff!
        // Now that n predictions are gathered, do the cycle
        for (StandardPattern pattern : dataSet) {
            // set the input vector
            Vector inputs = pattern.getVector();
            if(!predicted.isEmpty()) {
                for(int j = 0; j < n; j++ ) { // substitute predicted values into the pattern
                    inputs.setReal(j, predicted.get(j).doubleValue());
                }
            }
            // get the prediction
            //System.out.println("Inputs: "+inputs.toString());
            pattern.setVector(inputs);
            predicted = neuralNetwork.evaluatePattern(pattern);
            // add to the resulting vector
            for(Numeric element : predicted)
                builder.add(element);
        }
        return builder.build();
    }


    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
