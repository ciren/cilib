/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.RandomWeightInitialisationStrategy;
import net.sourceforge.cilib.nn.components.WeightInitialisationStrategy;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Class represents an object responsible for constructing a layer i.e. construct
 * and correctly set up all the neurons the layer should contain. The concrete
 * extension defines how this is done. Class also stores the {@link WeightInitialisationStrategy}
 * and the {@link DomainRegistry} for the weights.
 */
public abstract class LayerBuilder implements Cloneable {

    private WeightInitialisationStrategy weightInitialisationStrategy;
    private DomainRegistry domainRegistry;

    /**
     * Default constructor. The default weight initialisation strategy is the
     * {@link RandomWeightInitialisationStrategy} and the default domain registry
     * is the {@link StringBasedDomainRegistry}.
     */
    public LayerBuilder() {
        weightInitialisationStrategy = new RandomWeightInitialisationStrategy();
        domainRegistry = new StringBasedDomainRegistry();
    }

    public LayerBuilder(LayerBuilder rhs) {
        weightInitialisationStrategy = new RandomWeightInitialisationStrategy();
        domainRegistry = new StringBasedDomainRegistry();
    }

    public abstract LayerBuilder getClone();

    /**
     * Builds a layer based on the specified {@link LayerConfiguration} and the
     * feeding layer's size.
     * @param layerConfiguration the layer configuration for the layer to be constructed.
     * @param previousLayerAbsoluteSize the absolute size (incl. bias neurons) of the feeding layer.
     * @return the constructed layer.
     */
    public abstract Layer buildLayer(LayerConfiguration layerConfiguration, int previousLayerAbsoluteSize);

    /**
     * Gets the weight initialisation strategy.
     * @return the weight initialisation strategy.
     */
    public WeightInitialisationStrategy getWeightInitialisationStrategy() {
        return weightInitialisationStrategy;
    }

    /**
     * Sets the weight initialisation strategy.
     * @param weightInitialisationStrategy the weight initialisation strategy.
     */
    public void setWeightInitialisationStrategy(WeightInitialisationStrategy weightInitialisationStrategy) {
        this.weightInitialisationStrategy = weightInitialisationStrategy;
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
