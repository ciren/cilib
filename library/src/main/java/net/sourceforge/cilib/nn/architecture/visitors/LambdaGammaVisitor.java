/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import com.google.common.annotations.VisibleForTesting;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.container.Vector;

public final class LambdaGammaVisitor implements ArchitectureVisitor {

    private final Vector solution;

    public LambdaGammaVisitor(Vector solution) {
        this.solution = solution;
    }

    @Override
    public LambdaGammaVisitor getClone() {
        return new LambdaGammaVisitor(solution.getClone());
    }

    @Override
    public void visit(Architecture architecture) {

        int weightIdx = 0;
        
        for (Layer neurons : architecture.getActivationLayers()) {
            for (Neuron neuron : neurons) {
                int weightsSize = neuron.getWeights().size();
                neuron.setWeights(solution.copyOfRange(weightIdx, weightIdx + weightsSize));
                weightIdx += weightsSize;
                
                if (!neuron.isBias()) {
                    neuron.setActivationFunction(new Sigmoid(solution.get(weightIdx).doubleValue(), solution.get(weightIdx+1).doubleValue()));
                    weightIdx += 2;
                }
            }
        }
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
