package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.NeuralNetworks;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

public class WeightBasedDomainInitializationStrategy implements DomainInitializationStrategy {

    @Override
    public DomainRegistry initializeDomain(NeuralNetwork neuralNetwork) {
        int numWeights = NeuralNetworks.countWeights(neuralNetwork);
        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        StringBasedDomainRegistry domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString(domainString + "^" + numWeights);
        return domainRegistry;
    }

}
