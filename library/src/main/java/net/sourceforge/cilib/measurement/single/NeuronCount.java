/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Int;

/**
 * Counts the number of neurons in a neural network.
 */
public class NeuronCount implements Measurement {

    private boolean includeBias;

    public NeuronCount() {
        includeBias = true;
    }

    public NeuronCount(NeuronCount rhs) {
        includeBias = rhs.includeBias;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public NeuronCount getClone() {
        return new NeuronCount(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();

        int count = 0;
        for (Layer curLayer : problem.getNeuralNetwork().getArchitecture().getLayers()) {
            count += curLayer.size();
            if (!includeBias && curLayer.isBias()) {
                --count;
            }
        }

        return Int.valueOf(count);
    }

    /**
     * Sets whether bias units should be included in the count.
     * @param include True to include bias units.
     */
    public void setIncludeBias(boolean include) {
        includeBias = include;
    }
}
