/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.domain;

import net.cilib.nn.NeuralNetwork;
import net.cilib.nn.NeuralNetworks;
import net.cilib.nn.architecture.visitors.ArchitectureVisitor;
import net.cilib.nn.architecture.visitors.LambdaGammaVisitor;
import net.cilib.type.types.Type;
import net.cilib.type.types.container.Vector;

public class LambdaGammaSolutionConversionStrategy implements SolutionConversionStrategy {

    private int activationFuncCount = 0;
    private int weightCount = 0;

    @Override
    public SolutionConversionStrategy initialise(NeuralNetwork neuralNetwork) {
        activationFuncCount = NeuralNetworks.countActivationFunctions(neuralNetwork);
        weightCount = NeuralNetworks.countWeights(neuralNetwork);
        return this;
    }

    @Override
    public ArchitectureVisitor interpretSolution(Type solution) {
        return new LambdaGammaVisitor((Vector)solution, weightCount, activationFuncCount);
    }

    protected int getActivationFuncCount() {
        return activationFuncCount;
    }

    protected int getWeightCount() {
        return weightCount;
    }
}
