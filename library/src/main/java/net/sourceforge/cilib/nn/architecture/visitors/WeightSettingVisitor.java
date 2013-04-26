/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Visitor that takes a {@link Vector} of weights as input to set the weights of
 * the neurons in an architecture. The weight values are not cloned.
 */
public final class WeightSettingVisitor implements ArchitectureVisitor {

    private final Vector weights;

    public WeightSettingVisitor(Vector weights) {
        this.weights = weights;
    }

    @Override
    public WeightSettingVisitor getClone() {
        return new WeightSettingVisitor(weights.getClone());
    }

    /**
     * Sets the weights of the architecture.
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();

        int currentIndex = 0;
        int numLayers = layers.size();
        for (int i = 1; i < numLayers; i++) {
            Layer layer = layers.get(i);
            for (Neuron neuron : layer) {
                Vector neuronWeights = neuron.getWeights();
                int size = neuronWeights.size();
                for (int j = 0; j < size; j++) {
                    neuronWeights.set(j, weights.get(currentIndex++));
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * Gets the weights to use.
     * @return the weights to use for setting.
     */
    public Vector getWeights() {
        return weights;
    }

}
