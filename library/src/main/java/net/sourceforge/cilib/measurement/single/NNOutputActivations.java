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
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Gets the activations produced by die output layer of NN.
 */
public class NNOutputActivations implements Measurement {

    /**
     * {@inheritDoc }
     */
    @Override
    public NNOutputActivations getClone() {
        return new NNOutputActivations();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public StringType getValue(Algorithm algorithm) {
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        
        //Get output layer
        List layers = problem.getNeuralNetwork().getArchitecture().getLayers();
        int size = layers.size();
        Layer output_layer = (Layer)layers.get(size - 1);
        
        //Get activations
        Vector activations = output_layer.getActivations();
        return new StringType(activations.toString());
    }
}
