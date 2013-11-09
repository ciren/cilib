/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Counts the number of non-zero weights (weights close to zero determined
 * by the epsilon value), and measures as a percentage in terms of the 
 * total number of NN weights.
 */
public class NNNumNonZeroWeightsPercentage implements Measurement {

    private ControlParameter epsilon;

    public NNNumNonZeroWeightsPercentage() {
        epsilon = ConstantControlParameter.of(0.05);
    }

    public NNNumNonZeroWeightsPercentage(NNNumNonZeroWeightsPercentage rhs) {
        epsilon = rhs.epsilon.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public NNNumNonZeroWeightsPercentage getClone() {
        return new NNNumNonZeroWeightsPercentage(this);
    }

    /**
     * Returns the percentage of non-zero weights to the total number of weights
     * as a number in R(0:1)
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        int count = 0;

        for (Numeric n : solution){
            if (Math.abs(n.doubleValue()) > epsilon.getParameter()){
                ++count;
            }
        }

        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();

        int total = 0;
        for (Layer curLayer : problem.getNeuralNetwork().getArchitecture().getLayers()) {
            for (Neuron curNeuron : curLayer) {
                total += curNeuron.getNumWeights();
            }
        }

        return Real.valueOf((double) count / (double) total);
    }

    public void setEpsilon(Double epsilon){
        ((ConstantControlParameter) this.epsilon).setParameter(epsilon);
    }

    public ControlParameter getEpsilon(){
        return this.epsilon;
    }
}
