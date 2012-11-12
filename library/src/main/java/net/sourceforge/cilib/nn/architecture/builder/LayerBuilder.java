/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.RandomWeightInitializationStrategy;
import net.sourceforge.cilib.nn.components.WeightInitializationStrategy;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

/**
 * Class represents an object responsible for constructing a layer i.e. construct
 * and correctly set up all the neurons the layer should contain. The concrete
 * extension defines how this is done. Class also stores the {@link WeightInitializationStrategy}
 * and the {@link DomainRegistry} for the weights.
 */
public abstract class LayerBuilder {

    private WeightInitializationStrategy weightInitializationStrategy;
    private DomainRegistry domainRegistry;

    /**
     * Default constructor. The default weight initialization strategy is the
     * {@link RandomWeightInitializationStrategy} and the default domain registry
     * is the {@link StringBasedDomainRegistry}.
     */
    public LayerBuilder() {
        weightInitializationStrategy = new RandomWeightInitializationStrategy();
        domainRegistry = new StringBasedDomainRegistry();
    }

    /**
     * Builds a layer based on the specified {@link LayerConfiguration} and the
     * feeding layer's size.
     * @param layerConfiguration the layer configuration for the layer to be constructed.
     * @param previousLayerAbsoluteSize the absolute size (incl. bias neurons) of the feeding layer.
     * @return the constructed layer.
     */
    public abstract Layer buildLayer(LayerConfiguration layerConfiguration, int previousLayerAbsoluteSize);

    /**
     * Gets the weight initialization stratetegy.
     * @return the weight initialization strategy.
     */
    public WeightInitializationStrategy getWeightInitializationStrategy() {
        return weightInitializationStrategy;
    }

    /**
     * Sets the weight initialization strategy.
     * @param weightInitializationStrategy the weight initialization strategy.
     */
    public void setWeightInitializationStrategy(WeightInitializationStrategy weightInitializationStrategy) {
        this.weightInitializationStrategy = weightInitializationStrategy;
    }

    /**
     * Gets the domain registry.
     * @return the domain registry.
     */
    public DomainRegistry getDomainRegistry() {
        return domainRegistry;
    }

    /**
     * Sets the domain registry.
     * @param domainRegistry the new domain registry.
     */
    public void setDomainRegistry(DomainRegistry domainRegistry) {
        this.domainRegistry = domainRegistry;
    }

    /**
     * Convenience method for getting the domain registry's domain string.
     * @return the domain registry's domain string.
     */
    public String getDomain() {
        return domainRegistry.getDomainString();
    }

    /**
     * Convenience method for setting the domain registry's domain string.
     * @param domain the domain registry's domain string.
     */
    public void setDomain(String domain) {
        this.domainRegistry.setDomainString(domain);
    }
}
