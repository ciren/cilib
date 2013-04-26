/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.PatternInputSource;

/**
 * Class implements an {@link ArchitectureOperationVisitor} that performs a feed-
 * forward through a neural network architecture as the visit operation.
 */
public class FeedForwardVisitor extends ArchitectureOperationVisitor {

    public FeedForwardVisitor() {}

    public FeedForwardVisitor(FeedForwardVisitor rhs) {
        super(rhs);
    }

    @Override
    public FeedForwardVisitor getClone() {
        return new FeedForwardVisitor(this);
    }

    /**
     * Perform a feed-forward using {@link #input} as the input for the FF and
     * storing the output in {@link #output}.
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        int size = layers.size();

        ((ForwardingLayer) layers.get(0)).setSource(new PatternInputSource(input));
        Layer currentLayer = null;
        for (int l = 1; l < size; l++) {
            currentLayer = layers.get(l);
            int layerSize = currentLayer.size();
            for (int n = 0; n < layerSize; n++) {
                currentLayer.get(n).calculateActivation(layers.get(l - 1));
            }
        }

        this.output = currentLayer.getActivations();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }
}
