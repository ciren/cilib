/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.cascadecorrelationalgorithm.CascadeCorrelationAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Real;

/**
 * This measurement is used to estimate the amount of computation performed
 * to evaluate the NN in the cascade correlation algorithm.
 * This implementation assumes that caching is correctly implemented in the
 * cascade correlation algorithm. I.e. once neurons have been selected from
 * the candidates, their behaviour is immediately added to the cache. 
 */
public class CascadeNetworkWeightEvaluationCount implements Measurement {

    private double correlationWeight;
    private int correlationPenalty;
    private double outputWeight;
    private int outputPenalty;

    public CascadeNetworkWeightEvaluationCount() {
        correlationWeight = 1.0;
        correlationPenalty = 0;
        outputWeight = 1.0;
        outputPenalty = 0;
    }

    public CascadeNetworkWeightEvaluationCount(CascadeNetworkWeightEvaluationCount rhs) {
        correlationWeight = rhs.correlationWeight;
        correlationPenalty = rhs.correlationPenalty;
        outputWeight = rhs.outputWeight;
        outputPenalty = rhs.outputPenalty;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CascadeNetworkWeightEvaluationCount getClone() {
        return new CascadeNetworkWeightEvaluationCount(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CascadeCorrelationAlgorithm  ccAlgorithm = (CascadeCorrelationAlgorithm) algorithm;
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        int corEvalCount = ccAlgorithm.getPhase1EvaluationCount();
        int corWeightCount = ccAlgorithm.getPhase1WeightEvaluationCount();
        int outEvalCount = ccAlgorithm.getPhase2EvaluationCount();
        int outWeightCount = ccAlgorithm.getPhase2WeightEvaluationCount();

        List<Layer> layers = problem.getNeuralNetwork().getArchitecture().getLayers();
        int outputSize = layers.get(layers.size()-1).size();

        return Real.valueOf(correlationWeight*(corWeightCount + correlationPenalty*corEvalCount)
                            + outputWeight*(outWeightCount + outputPenalty*outEvalCount*outputSize));
    }

    /**
     * Sets the weight that correlation-phase evaluations counts torwards the
     * final measurement. This is useful in the case where there is a significant
     * difference in the cost of evaluating candidate neurons and output neurons.
     * This weight applies to both the weight evaluations and the activation
     * function evaluations.
     * @param newWeight The weight to be used.
     */
    public void setCorrelationWeight(double newWeight) {
        correlationWeight = newWeight;
    }

    /**
     * Sets a penalty that should be added for each activation function evaluated
     * in the candidate neurons.
     * @param newPenalty An integer estimate of how many weight evaluations
     *                      an activation function is worth.
     */
    public void setCorrelationPenalty(int newPenalty) {
        correlationPenalty = newPenalty;
    }

    /**
     * Sets the weight that output-training phase evaluations counts torwards the
     * final measurement. This is useful in the case where there is a significant
     * difference in the cost of evaluating candidate neurons and output neurons.
     * This weight applies to both the weight evaluations and the activation
     * function evaluations.
     * @param newWeight The weight to be used.
     */
    public void setOutputWeight(double newWeight) {
        outputWeight = newWeight;
    }

    /**
     * Sets a penalty that should be added for each activation function evaluated
     * in the output layer.
     * @param newPenalty An integer estimate of how many weight evaluations
     *                      an activation function is worth.
     */
    public void setOutputPenalty(int newPenalty) {
        outputPenalty = newPenalty;
    }
}
