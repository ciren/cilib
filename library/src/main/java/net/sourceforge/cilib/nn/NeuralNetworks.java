package net.sourceforge.cilib.nn;

import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;

/**
 * Utility class for neural networks.
 */
public final class NeuralNetworks {

    public static int countWeights(NeuralNetwork neuralNetwork) {
        int weightCount = 0;
        for (Layer layer : neuralNetwork.getArchitecture().getActivationLayers()) {
            for (Neuron neuron : layer) {
                weightCount += neuron.getNumWeights();
            }
        }
        return weightCount;
    }

    public static int countActivationFunctions(NeuralNetwork neuralNetwork) {
        int activationFuncCount = 0;
        for (Layer layer : neuralNetwork.getArchitecture().getActivationLayers()) {
            for (Neuron neuron : layer) {
                if (!neuron.isBias()) {
                    activationFuncCount++;
                }
            }
        }
        return activationFuncCount;
    }

    private NeuralNetworks() { }
}
