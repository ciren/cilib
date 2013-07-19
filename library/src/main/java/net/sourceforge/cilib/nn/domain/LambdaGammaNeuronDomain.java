/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.type.DomainRegistry;

/**
 * Wraps another NeuronDomainProvider to add lambda and gamma domains. It first
 * gets a domain from the wrapped provider. It then appends the lambda domain
 * and the the gamma domain. This implies that only one lambda and one gamma
 * domain is added for each neuron. The lambda and gamma domains are cloned from
 * user-set prototypes.
 */
public class LambdaGammaNeuronDomain implements NeuronDomainProvider {

    private DomainRegistry lambdaDomainPrototype;
    private DomainRegistry gammaDomainPrototype;
    private NeuronDomainProvider provider;

    public LambdaGammaNeuronDomain() {}

    public LambdaGammaNeuronDomain(LambdaGammaNeuronDomain rhs) {
        lambdaDomainPrototype = rhs.lambdaDomainPrototype.getClone();
        gammaDomainPrototype = rhs.gammaDomainPrototype.getClone();
        provider = rhs.provider.getClone();
    }

    /**
     * {@inheritDoc }
     */
    public LambdaGammaNeuronDomain getClone() {
        return new LambdaGammaNeuronDomain(this);
    }

    /**
     * {@inheritDoc }
     */
    public DomainRegistry generateDomain(int fanin) {
        
        DomainRegistry domainRegistry = provider.generateDomain(fanin);
        String domainString = domainRegistry.getDomainString();
        domainRegistry.setDomainString(domainString + "," + lambdaDomainPrototype.getDomainString() + "," + gammaDomainPrototype.getDomainString());

        return domainRegistry;
    }

    /**
     * Sets the prototype domain for the lambda weight.
     * @param domainPrototype The domain prototype to set.
     */
    public void setLambdaDomainPrototype(DomainRegistry domainPrototype) {
        this.lambdaDomainPrototype = domainPrototype;
    }

    /**
     * Sets the prototype domain for the gamma weight.
     * @param domainPrototype The domain prototype to set.
     */
    public void setGammaDomainPrototype(DomainRegistry domainPrototype) {
        this.gammaDomainPrototype = domainPrototype;
    }

    /**
     * Sets the NeuronDomainProvider to wrap.
     * @param provider The provider to wrap.
     */
    public void setProvider(NeuronDomainProvider provider) {
        this.provider = provider;
    }
}
