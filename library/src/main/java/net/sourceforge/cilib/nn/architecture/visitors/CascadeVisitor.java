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
 * Class implements an {@link ArchitectureOperationVisitor} that performs a
 * cascade pass through a neural network architecture as the visit operation.
 */
public class CascadeVisitor extends ArchitectureOperationVisitor {

    public CascadeVisitor() {}

    public CascadeVisitor(CascadeVisitor rhs) {
        super(rhs);
    }

    @Override
    public CascadeVisitor getClone() {
        return new CascadeVisitor(this);
    }
	
    /**
     * Perform cascade pass using {@link #input} as the input for the pass and
     * storing the output in {@link #output}.
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        int size = layers.size();

        ((ForwardingLayer) layers.get(0)).setSource(new PatternInputSource(input));

        //Consolidate multiple layers into a single input.
        //The receiving Neuron must ensure that it doesn't process more inputs
        //than what it has weights for.
        Layer consolidatedLayer = new Layer();
        for (Layer curLayer : layers) {
            for (int curNeuron = 0; curNeuron < curLayer.size(); curNeuron++) {
                consolidatedLayer.add(curLayer.getNeuron(curNeuron));
            }
        }

        Layer currentLayer = null;
        for (int l = 1; l < size; l++) {
            currentLayer = layers.get(l);
            int layerSize = currentLayer.size();
            for (int n = 0; n < layerSize; n++) {
                currentLayer.getNeuron(n).calculateActivation(consolidatedLayer);
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
