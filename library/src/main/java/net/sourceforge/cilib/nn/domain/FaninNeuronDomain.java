/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.type.StringBasedDomainRegistry;

/**
 * Generates neuron domain from the neuron's fanin. Particularly, the domain is
 * R(-1/sqrt(fanin):1/sqrt(fanin))^fanin
 */
public class FaninNeuronDomain implements NeuronDomainProvider {

    /**
     * {@inheritDoc }
     */
    public FaninNeuronDomain getClone() {
        return new FaninNeuronDomain();
    }

    /**
     * {@inheritDoc }
     */
    public StringBasedDomainRegistry generateDomain(int fanin) {

        double bound = 1/Math.sqrt(fanin);
        
        StringBasedDomainRegistry domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString("R(" + (-bound) + ":" + bound + ")^" + fanin);

        return domainRegistry;
    }
}
