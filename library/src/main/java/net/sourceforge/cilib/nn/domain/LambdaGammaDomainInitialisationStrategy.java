/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.NeuralNetworks;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import org.parboiled.common.Preconditions;

public class LambdaGammaDomainInitialisationStrategy implements DomainInitialisationStrategy {

    private String lambdaDomainString = "R(0.0:1.0)";
    private String gammaDomainString = "R(0.0:1.0)";

    @Override
    public DomainRegistry initialiseDomain(NeuralNetwork neuralNetwork) {
        validateSigmoidActivationFunctions(neuralNetwork);
        final int activationFuncCount = NeuralNetworks.countActivationFunctions(neuralNetwork);
        final int weightCount = NeuralNetworks.countWeights(neuralNetwork);
        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        StringBasedDomainRegistry domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString(domainString + "^" + weightCount + "," +
                lambdaDomainString + "^" + activationFuncCount + "," +
                gammaDomainString + "^" + activationFuncCount);
        return domainRegistry;
    }

    private void validateSigmoidActivationFunctions(NeuralNetwork neuralNetwork) {
        for (Layer activationLayer : neuralNetwork.getArchitecture().getActivationLayers()) {
            for (Neuron neuron : activationLayer) {
                if (!neuron.isBias()) {
                    Preconditions.checkArgument(neuron.getActivationFunction() instanceof Sigmoid, "Lambda Gamma domain initialisation is only applicable to Sigmoid activation functions.");
                }
            }
        }
    }

    public String getLambdaDomainString() {
        return lambdaDomainString;
    }

    public void setLambdaDomainString(String lambdaDomainString) {
        this.lambdaDomainString = lambdaDomainString;
    }

    public String getGammaDomainString() {
        return gammaDomainString;
    }

    public void setGammaDomainString(String gammaDomainString) {
        this.gammaDomainString = gammaDomainString;
    }
}
