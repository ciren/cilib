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

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.BiasNeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.DotProductNeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.LinearOutputFunction;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author leo
 * This class constructs a Neural Network topology that consists of only an Input and an Ouput layer, with weight values between them.
 * This Neural Network does not have a hidden layer.
 */
public class InputOutputGenericTopologyBuilder extends FFNNgenericTopologyBuilder {

	public InputOutputGenericTopologyBuilder() {
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<ArrayList<NeuronConfig>> createLayerList() {
        ArrayList<ArrayList<NeuronConfig>> network = new ArrayList<ArrayList<NeuronConfig>>();

        ArrayList<NeuronConfig> tmp = new ArrayList<NeuronConfig>();

        //construct layer 0 as a base case
        for (int n = 0; n < layerSizes[0] - 1; n++){
            DotProductNeuronConfig neuron = new DotProductNeuronConfig();
            neuron.setOutputFunction(new LinearOutputFunction());
            boolean[] tsA = new boolean[1];
            tsA[0] = false;
            neuron.setTimeStepMap(tsA);
            neuron.setPatternWeight(new Weight(new Real(1)));
            neuron.setPatternInputPos(n);
            neuron.setCurrentOutput(new Real(0));
            neuron.setTminus1Output(new Real(0));
            neuron.setOutputNeuron(false);
            tmp.add(neuron);
        }
        //add bias neuron
        BiasNeuronConfig biasNeuron = new BiasNeuronConfig();
        biasNeuron.setCurrentOutput(new Real(-1));
        biasNeuron.setTminus1Output(new Real(-1));
        biasNeuron.setInputWeights(null);
        tmp.add(biasNeuron);

        network.add(tmp);

        //---------------------------------------------------------------------
        //construct the output layer

        ArrayList<NeuronConfig> tmp2 = new ArrayList<NeuronConfig>();
        for (int n = 0; n < layerSizes[layerSizes.length - 1]; n++){
            DotProductNeuronConfig neuron2 = new DotProductNeuronConfig();
            if(outputActivationFunction != null)
                neuron2.setOutputFunction(outputActivationFunction.getClone()); //output layer gets output activation function
            else
                neuron2.setOutputFunction(activationFunction.getClone()); //hidden layer act function

            //set input neurons
            NeuronConfig[] inputs = new NeuronConfig[network.get(0).size()];
            for (int inp = 0; inp < inputs.length; inp++){
                inputs[inp] = network.get(0).get(inp);
            }
            neuron2.setInput(inputs);

            //set input weights
            Weight[] w = new Weight[network.get(0).size()];
            for (int wi = 0; wi < w.length; wi++){
                w[wi] = prototypeWeight.getClone();
            }
            neuron2.setInputWeights(w);

            //set time step values to false
            boolean[] tsH = new boolean[network.get(0).size()];
            for (int inp = 0; inp < inputs.length; inp++){
                tsH[inp] = false;
            }
            neuron2.setTimeStepMap(tsH);
            neuron2.setCurrentOutput(new Real(0));
            neuron2.setTminus1Output(new Real(0));
            neuron2.setOutputNeuron(true);

            tmp2.add(neuron2);
        }
        network.add(tmp2);

        return network;
	}
    /**
     * {@inheritDoc}
     */
	@Override
	public void initialize() {
        if ((this.layerSizes == null) || (this.prototypeWeight == null)){
            throw new IllegalArgumentException("FFNNGenericTopologyBuilder: Required object was null during initialization");
        }

        if (this.layerSizes[0] <= 1) {
            throw new IllegalArgumentException("FFNNGenericTopologyBuilder: illegal layer size. Layer 0 size = " + this.layerSizes[0] +
                    ".  Total layers = " + this.layerSizes.length + ".  Minimum layer size is 2 as bias units are included.");
        }


	}
    /**
     * {@inheritDoc}
     */
    @Override
    public void addLayer(int nrNeurons){

        if (this.layerSizes == null){
            this.layerSizes = new int[1];
            this.layerSizes[0] = nrNeurons;
        }
        else {
            int[] tmp = new int[this.layerSizes.length + 1];

            for (int i=0; i < layerSizes.length; i++){
                tmp[i] = layerSizes[i];
            }
            tmp[layerSizes.length] = nrNeurons;
            this.layerSizes = tmp;
        }
    }

}
