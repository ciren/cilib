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
    private int weightCount;
    private final int activationFuncCount;

    public LambdaGammaVisitor(Vector solution, int weightCount, int activationFuncCount) {
        this.solution = solution;
        this.weightCount = weightCount;
        this.activationFuncCount = activationFuncCount;
    }

    @Override
    public LambdaGammaVisitor getClone() {
        return new LambdaGammaVisitor(solution.getClone(), weightCount, activationFuncCount);
    }

    @Override
    public void visit(Architecture architecture) {
        final Vector weights = extractWeights(solution);
        final Vector lambdas = extractLambdas(solution);
        final Vector gammas = extractGammas(solution);

        int weightIdx = 0;
        int lambdaIdx = 0;
        int gammaIdx = 0;

        for (Layer neurons : architecture.getActivationLayers()) {
            for (Neuron neuron : neurons) {
                if (!neuron.isBias()) {
                    neuron.setActivationFunction(new Sigmoid(lambdas.get(lambdaIdx++).doubleValue(), gammas.get(gammaIdx++).doubleValue()));
                }
                int weightsSize = neuron.getWeights().size();
                neuron.setWeights(weights.copyOfRange(weightIdx, weightIdx + weightsSize));
                weightIdx += weightsSize;
            }
        }
    }

    @VisibleForTesting
    protected Vector extractWeights(Vector solution) {
        return solution.copyOfRange(0, weightCount);
    }

    @VisibleForTesting
    protected Vector extractLambdas(Vector solution) {
        return solution.copyOfRange(weightCount, weightCount + activationFuncCount);
    }

    @VisibleForTesting
    protected Vector extractGammas(Vector solution) {
        return solution.copyOfRange(weightCount + activationFuncCount, weightCount + activationFuncCount * 2);
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
