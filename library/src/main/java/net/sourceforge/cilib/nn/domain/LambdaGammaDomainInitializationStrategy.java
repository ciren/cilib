package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.NeuralNetworks;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

public class LambdaGammaDomainInitializationStrategy implements DomainInitializationStrategy {

    private String lambdaDomainString = "R(0.0:1.0)";
    private String gammaDomainString = "R(0.0:1.0)";

    @Override
    public DomainRegistry initializeDomain(NeuralNetwork neuralNetwork) {
        final int activationFuncCount = NeuralNetworks.countActivationFunctions(neuralNetwork);
        final int weightCount = NeuralNetworks.countWeights(neuralNetwork);
        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        StringBasedDomainRegistry domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString(domainString + "^" + weightCount + "," +
                lambdaDomainString + "^" + activationFuncCount + "," +
                gammaDomainString + "^" + activationFuncCount);
        return domainRegistry;
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
