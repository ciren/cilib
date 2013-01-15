/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.NeuralNetworks;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

public class WeightBasedDomainInitialisationStrategy implements DomainInitialisationStrategy {

    @Override
    public DomainRegistry initialiseDomain(NeuralNetwork neuralNetwork) {
        int numWeights = NeuralNetworks.countWeights(neuralNetwork);
        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        StringBasedDomainRegistry domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString(domainString + "^" + numWeights);
        return domainRegistry;
    }

}
