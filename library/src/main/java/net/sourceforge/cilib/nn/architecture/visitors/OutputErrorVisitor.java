/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The visitor calculates the output error as the difference between the target
 * output and the output layer output. The differences is not squared or summed.
 */
public class OutputErrorVisitor extends ArchitectureOperationVisitor {

    public OutputErrorVisitor() {}

    public OutputErrorVisitor(OutputErrorVisitor rhs) {
        super(rhs);
    }

    @Override
    public OutputErrorVisitor getClone() {
        return new OutputErrorVisitor(this);
    }
	
    /**
     * Calculate the output error given the StandardPattern that contains the target
     * as {@link #input} .
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        Layer outputLayer = architecture.getLayers().get(architecture.getNumLayers() - 1);
        int layerSize = outputLayer.size();
        Vector.Builder outputBuilder = Vector.newBuilder();
        for (int k = 0; k < layerSize; k++) {
            Neuron currentNeuron = outputLayer.get(k);
            double t_k = layerSize > 1 ? ((Vector) input.getTarget()).doubleValueOf(k) : ((Real)input.getTarget()).doubleValue();
            double o_k = currentNeuron.getActivation();
            double tmp = (t_k - o_k);
            outputBuilder.add(tmp);
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
