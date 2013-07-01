/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.single;

import net.cilib.algorithm.Algorithm;
import net.cilib.measurement.Measurement;
import net.cilib.nn.architecture.Layer;
import net.cilib.nn.components.Neuron;
import net.cilib.problem.nn.NNTrainingProblem;
import net.cilib.type.types.Int;

/**
 * Counts the number of weights in a neural network.
 */
public class NNWeightCount implements Measurement {

    private int penalty;

    public NNWeightCount() {
        penalty = 0;
    }

    public NNWeightCount(NNWeightCount rhs) {
        penalty = rhs.penalty;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public NNWeightCount getClone() {
        return new NNWeightCount(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();

        int count = 0;
        for (Layer curLayer : problem.getNeuralNetwork().getArchitecture().getLayers()) {
            for (Neuron curNeuron : curLayer) {
                count += curNeuron.getNumWeights();

                if (curNeuron.getActivationFunction() != null)
                    count += penalty;
            }
        }

        return Int.valueOf(count);
    }

    /**
     * Sets a penalty that should be added for each activation function in the NN.
     * @param newPenalty An integer estimate of how many weight evaluations
     *                      a activation function is worth.
     */
    public void setPenalty(int newPenalty) {
        penalty = newPenalty;
    }
}
