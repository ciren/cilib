/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

/**
 * Class is a concrete extension of the abstract {@link LayerBuilder}. It constructs
 * a layer by cloning a prototype neuron and adding to it weights such that it is
 * fully connected to the feeding layer.
 */
public class PrototypeFullyConnectedLayerBuilder extends LayerBuilder {

    private Neuron prototypeNeuron;

    /**
     * Default constructor. Default neuron is a {@link Neuron}
     */
    public PrototypeFullyConnectedLayerBuilder() {
        prototypeNeuron = new Neuron();
    }

    public PrototypeFullyConnectedLayerBuilder(PrototypeFullyConnectedLayerBuilder rhs) {
        super(rhs);
        prototypeNeuron = rhs.prototypeNeuron.getClone();
    }

    @Override
    public PrototypeFullyConnectedLayerBuilder getClone() {
        return new PrototypeFullyConnectedLayerBuilder(this);
    }

    /**
     * Builds a layer by cloning a prototype neuron and adding to it weights such that it is
     * fully connected to the feeding layer.
     * @param layerConfiguration
     * @param previousLayerAbsoluteSize
     * @return the built layer.
     */
    @Override
    public Layer buildLayer(LayerConfiguration layerConfiguration, int previousLayerAbsoluteSize) {
        prototypeNeuron.setActivationFunction(layerConfiguration.getActivationFunction());
        int layerSize = layerConfiguration.getSize();
        boolean bias = layerConfiguration.isBias();

        //determine correct domain registry
        DomainRegistry domainRegistry = domainProvider.generateDomain(previousLayerAbsoluteSize);

        //set domain for prototype neuron
        prototypeNeuron.setDomain(domainRegistry.getDomainString());

        //get prototype weight vector
        Vector prototypeWeightVector = null;
        try {
            prototypeWeightVector = (Vector) domainRegistry.getBuiltRepresentation();
        }
        catch(ClassCastException exception) {
            throw new UnsupportedOperationException("The domain string of the neural network weights has to be real valued");
        }

        //add neurons to layer
        Layer layer = new Layer();
        for (int i = 0; i < layerSize; i++) {
            Neuron newNeuron = prototypeNeuron.getClone();

            Vector weights = prototypeWeightVector.getClone();
            //TODO: initialisation should be done by training algorithm
            this.getWeightInitialisationStrategy().initialise(weights);
            newNeuron.setWeights(weights);
            layer.add(newNeuron);
        }
        if (bias) {
            layer.add(new BiasNeuron());
            layer.setBias(true);
        }
        return layer;
    }

    /**
     * Get the prototype neuron.
     * @return the prototype neuron.
     */
    public Neuron getPrototypeNeuron() {
        return prototypeNeuron;
    }

    /**
     * Set the prototype neuron.
     * @param prototypeNeuron the prototype neuron.
     */
    public void setPrototypeNeuron(Neuron prototypeNeuron) {
        this.prototypeNeuron = prototypeNeuron;
    }
}
