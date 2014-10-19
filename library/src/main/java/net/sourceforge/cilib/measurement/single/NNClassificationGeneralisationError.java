/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Calculates the classification error of the best solution of an algorithm on the generalisation set  
 * of a {@link NNTrainingProblem}. Outputs the percentage of incorrectly classified patterns.
 */
public class NNClassificationGeneralisationError implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;

    protected double outputSensitivityThreshold = 0.2;

    /**
     * {@inheritDoc }
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable generalisationSet = problem.getGeneralisationSet();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        int numberPatternsCorrect = 0;
        int numberPatternsIncorrect = 0;
        OutputErrorVisitor visitor = new OutputErrorVisitor();
        Vector error = null;
        for (StandardPattern pattern : generalisationSet) {
            neuralNetwork.evaluatePattern(pattern);
            visitor.setInput(pattern);
            neuralNetwork.getArchitecture().accept(visitor);
            error = visitor.getOutput();
            boolean isCorrect = true;
            for (Numeric real : error) {
                if (Math.abs(real.doubleValue()) > this.outputSensitivityThreshold) {
                    isCorrect = false;
                    break;
                }
            }
            if (isCorrect){
                numberPatternsCorrect++;
            }
            else {
                numberPatternsIncorrect++;
            }
        }
        
        double percentageIncorrect = (double) numberPatternsIncorrect / ((double) numberPatternsIncorrect + (double) numberPatternsCorrect) * 100;
        return Real.valueOf(percentageIncorrect);
    }
}
