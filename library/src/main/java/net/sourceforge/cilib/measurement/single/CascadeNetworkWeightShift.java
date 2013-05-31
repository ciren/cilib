/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

/**
 * Measures the shift in old weights after last expansion has occurred.
 * Weights produced by the expansion are ignored. Weights in the output
 * layer are also ignored.
 */
public class CascadeNetworkWeightShift implements Measurement {

    private Vector previousExpansion;
    private Vector previousIteration;

    public CascadeNetworkWeightShift() {
        previousExpansion = null;
        previousIteration = null;
    }

    public CascadeNetworkWeightShift(CascadeNetworkWeightShift rhs) {
        previousExpansion = rhs.previousExpansion.getClone();
        previousIteration = rhs.previousIteration.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CascadeNetworkWeightShift getClone() {
        return new CascadeNetworkWeightShift(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        List<Layer> layers = neuralNetwork.getArchitecture().getLayers();

        //calculate layer sizes
        int lastHiddenLayerSize = layers.get(layers.size()-2).size();
        int outputLayerSize = layers.get(layers.size()-1).size();
        int consolidatedLayerSize = 0;
        for (int curLayer = 0; curLayer < layers.size()-2; ++curLayer) {
            consolidatedLayerSize += layers.get(curLayer).size();
        }

        int index = solution.size()-1;

        //remove output weights
        for (int curOutput = 0; curOutput < outputLayerSize*(consolidatedLayerSize+lastHiddenLayerSize); ++curOutput) {
            solution.remove(solution.get(index--));
        }

        //initialise
        if (previousIteration == null) {
            previousIteration = solution.getClone();
            return Real.valueOf(0);
        }

        //expansion occured
        if (solution.size() > previousIteration.size()) {
            previousExpansion = previousIteration;
        }

        previousIteration = solution.getClone();
        
        //remove new hidden layer weights
        for (int curWeight = 0; curWeight < consolidatedLayerSize*lastHiddenLayerSize; ++curWeight) {
            solution.remove(solution.get(index--));
        }

        if (solution.size() == 0) {
            return Real.valueOf(0);
        }

        //calculate difference from remaining weights
        Vector result = solution.subtract(previousExpansion);
        double weightShift = 0.0;
        for (Numeric element : result) {
            weightShift += Math.pow(element.doubleValue(), 2);
        }
        
        return Real.valueOf(weightShift);
    }
}
