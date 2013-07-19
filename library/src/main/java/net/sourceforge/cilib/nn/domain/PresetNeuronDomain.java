/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

/**
 * Generates neuron domain from a user-set prototype domain. The prototype
 * reflects the domain for one weight. This prototype is repeated for each
 * weight.
 */
public class PresetNeuronDomain implements NeuronDomainProvider {

    private DomainRegistry weightDomainPrototype;

    public PresetNeuronDomain() {}

    public PresetNeuronDomain(PresetNeuronDomain rhs) {
        weightDomainPrototype = rhs.weightDomainPrototype.getClone();
    }

    /**
     * {@inheritDoc }
     */
    public PresetNeuronDomain getClone() {
        return new PresetNeuronDomain(this);
    }

    /**
     * {@inheritDoc }
     */
    public StringBasedDomainRegistry generateDomain(int fanin) {
        
        StringBasedDomainRegistry domainRegistry = new StringBasedDomainRegistry();
        String domainString = weightDomainPrototype.getDomainString();
        domainRegistry.setDomainString(domainString + "^" + fanin);

        return domainRegistry;
    }

    /**
     * Sets the prototype domain for a single weight.
     * @param weightDomainPrototype The domain prototype.
     */
    public void setWeightDomainPrototype(DomainRegistry weightDomainPrototype) {
        this.weightDomainPrototype = weightDomainPrototype;
    }
}
