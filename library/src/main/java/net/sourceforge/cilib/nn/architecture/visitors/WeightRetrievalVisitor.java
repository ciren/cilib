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
 * Visitor retrieves all the weights in the architecture, starting with the far
 * left hidden layer and top neuron, moving down and then to the layer to the right.
 */
public class WeightRetrievalVisitor extends ArchitectureOperationVisitor {

    public WeightRetrievalVisitor() {}

    public WeightRetrievalVisitor(WeightRetrievalVisitor rhs) {
        super(rhs);
    }

    @Override
    public WeightRetrievalVisitor getClone() {
        return new WeightRetrievalVisitor(this);
    }

    /**
     * Retrieves the weights.
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();

        Vector.Builder outputBuilder = Vector.newBuilder();
        int numLayers = layers.size();
        for (int i = 1; i < numLayers; i++) {
            Layer layer = layers.get(i);
            for (Neuron neuron : layer) {
                outputBuilder.copyOf(neuron.getWeights());
            }
        }

        output = outputBuilder.build();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }
}
