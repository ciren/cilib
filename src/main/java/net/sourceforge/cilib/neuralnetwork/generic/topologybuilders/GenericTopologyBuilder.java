/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.neuralnetwork.generic.topologybuilders;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronFunction;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;


/**
 * @author stefanv
 *
 * This class froms the base for building any NN topology.  It provides a few basic tools such as contructing a
 * layered topology, either connected or not connected.  This provides a base for createLayerList(). createLayerList()
 * and createNeuronPipePool() are implemented in sub classes and together are responsible for building a valid
 * topology (Builder Design Pattern).
 *
 *
 */
public abstract class GenericTopologyBuilder implements Initializable {
    protected NeuronFunction activationFunction;
    protected NeuronFunction outputActivationFunction;
    public GenericTopologyBuilder(){
        activationFunction = new SigmoidOutputFunction();
        outputActivationFunction = null;
    }

    /**
     * Assemble the Neural Network
     * @return the Layer List of {@linkplain NeuronConfig} that represents the Neural Network
     */
    public abstract ArrayList<ArrayList<NeuronConfig>> createLayerList();

    /**
     * Get the type of {@linkplain NeuronFunction} that is used to construct the Neural Network
     * @return The {@linkplain NeuronFunction}
     */
    public NeuronFunction getActivationFunction() {
        return activationFunction;
    }

    /**
     * Set the type of {@linkplain NeuronFunction} to use for the hidden layer(s) of the Neural Network
     * @param activationFunction The activation function to use when constructing the Neural Network
     */
    public void setActivationFunction(NeuronFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    /**
     * Get the type of {@linkplain NeuronFunction} that is used to construct the output layer Neural Network
     * @return The {@linkplain NeuronFunction}
     */
    public NeuronFunction getOutputActivationFunction() {
        return outputActivationFunction;
    }

    /**
     * The type of {@linkplain NeuronFunction} to use for output neurons, this may differ from the type used in the hidden layer(s)
     * @param outputActivationFunction The activation funciton to use for output Neurons when construction the Neural Network
     */
    public void setOutputActivationFunction(NeuronFunction outputActivationFunction) {
        this.outputActivationFunction = outputActivationFunction;
    }

    /*
    protected ArrayList<ArrayList<NeuronConfig>> constructConnectedLayeredTopology(int[] layerSizes, Weight baseW){

        ArrayList<ArrayList<NeuronConfig>> network = new ArrayList<ArrayList<NeuronConfig>>();

        ArrayList<NeuronConfig> tmp = new ArrayList<NeuronConfig>();

        //construct layer 0 as a base case
        for (int n = 0; n < layerSizes[0]; n++){
            NeuronConfig neuron = new NeuronConfig();
            tmp.add(neuron);
        }
        network.add(tmp);

        //construct more layers iteratively, and connect them to previous layer
        for (int layer = 1; layer < layerSizes.length; layer++){

            ArrayList<NeuronConfig> tmp2 = new ArrayList<NeuronConfig>();
            for (int n = 0; n < layerSizes[layer]; n++){
                NeuronConfig neuron = new NeuronConfig();

                Weight[] w = new Weight[network.get(layer-1).size()];
                for (int wi = 0; wi < w.length; wi++){
                    w[wi] = baseW.clone();
                }
                neuron.setInputWeights(w);

                tmp2.add(neuron);
            }
            network.add(tmp2);

        }

        return network;

    }

    protected ArrayList<ArrayList<NeuronConfig>> constructLayeredTopology(int[] layerSizes){

        ArrayList<ArrayList<NeuronConfig>> network = new ArrayList<ArrayList<NeuronConfig>>();

        ArrayList<NeuronConfig> tmp = new ArrayList<NeuronConfig>();

        //construct layer 0 as a base case
        for (int n = 0; n < layerSizes[0]; n++){
            NeuronConfig neuron = new NeuronConfig();
            tmp.add(neuron);
        }
        network.add(tmp);

        //construct more layers recursively, no weights though...
        for (int layer = 1; layer < layerSizes.length; layer++){

            ArrayList<NeuronConfig> tmp2 = new ArrayList<NeuronConfig>();
            for (int n = 0; n < layerSizes[layer]; n++){
                NeuronConfig neuron = new NeuronConfig();
                tmp2.add(neuron);
            }
            network.add(tmp2);
        }
        return network;
    }

    */
}
