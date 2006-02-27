/*
 * FFNNStaticTopologyBuilder.java
 * 
 * Created on Mar 23, 2005
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.neuralnetwork.testarea;

import java.lang.reflect.Array;
import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.GenericTopologyBuilder;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.BiasNeuronPipeline;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.DotProductPipeline;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.LinearOutputFunction;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronPipeline;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FFNNStaticTopologyBuilder extends GenericTopologyBuilder{

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.GenericTopologyBuilder#createNeuronList()
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<NeuronConfig>> createLayerList() {
		
		ArrayList<ArrayList<NeuronConfig>> layers = new ArrayList<ArrayList<NeuronConfig>>();
		
		ArrayList<NeuronConfig> layer1 = new ArrayList<NeuronConfig>();
		ArrayList<NeuronConfig> layer2 = new ArrayList<NeuronConfig>();
		ArrayList<NeuronConfig> layer3 = new ArrayList<NeuronConfig>();
		
		NeuronConfig inputA;
		NeuronConfig inputB;
		NeuronConfig inputBias;
		NeuronConfig hiddenC;
		NeuronConfig hiddenD;
		NeuronConfig hiddenBias;
		NeuronConfig outputE;
		
		inputBias = new NeuronConfig(2,new Double(-1), null);
		hiddenBias = new NeuronConfig(2,new Double(-1), null);
		
		
		
		//Neuron config start
		//inputs from other neurons
		NeuronConfig[] inputsA = null;
		
		Weight<Double>[] inputAWeights = null;
				
		boolean[] tsA = new boolean[1];
		tsA[0] = false;
		
		Weight<Double> patternAw = new Weight<Double>(new Double(1), new Double(0));
		
		inputA = new NeuronConfig(inputsA,inputAWeights,tsA,0,patternAw, new Double(0),0);
		
		//Neuron config end.
		
		
//		Neuron config start
		//inputs from other neurons
		NeuronConfig[] inputsB = null;
		
		Weight<Double>[] inputBWeights = null;
				
		boolean[] tsB = new boolean[1];
		tsB[0] = false;
		
		Weight<Double> patternBw = new Weight<Double>(new Double(1),new Double(0));
		
		inputB = new NeuronConfig(inputsB,inputBWeights,tsB,1,patternBw, new Double(0),0);
		
		//Neuron config end.
		
		
		//Neuron config start
		//inputs from other neurons
		NeuronConfig[] inputsC = new NeuronConfig[3];
		inputsC[0] = inputA;
		inputsC[1] = inputB;
		inputsC[2] = inputBias;
		
		Weight<Double>[] inputCWeights = (Weight<Double>[]) Array.newInstance(Weight.class, 3);
		inputCWeights[0] = new Weight<Double>(new Double(0.1), new Double(0));
		inputCWeights[1] = new Weight<Double>(new Double(0.4), new Double(0));
		inputCWeights[2] = new Weight<Double>(new Double(0.7), new Double(0));
				
		boolean[] tsC = new boolean[3];
		tsC[0] = false;
		tsC[1] = false;
		tsC[2] = false;
		
		Weight<Double> patternCw = null;
		
		hiddenC = new NeuronConfig(inputsC,inputCWeights,tsC,-999,patternCw, new Double(0),1);
		
		//Neuron config end.
		
		{
		
//		Neuron config start
		//inputs from other neurons
		NeuronConfig[] inputsD = new NeuronConfig[3];
		inputsD[0] = inputA;
		inputsD[1] = inputB;
		inputsD[2] = inputBias;
		
		Weight<Double>[] inputDWeights = (Weight<Double>[]) Array.newInstance(Weight.class, 3);
		inputDWeights[0] = new Weight<Double>(new Double(-0.8), new Double(0));
		inputDWeights[1] = new Weight<Double>(new Double(-0.1), new Double(0));
		inputDWeights[2] = new Weight<Double>(new Double(0.3), new Double(0));
				
		boolean[] tsD = new boolean[3];
		tsD[0] = false;
		tsD[1] = false;
		tsD[2] = false;
		
		Weight<Double> patternDw = null;
		
		hiddenD = new NeuronConfig(inputsD,inputDWeights,tsD,-999,patternDw, new Double(0),1);
		}
		//Neuron config end.
		
		
//		Neuron config start
		//inputs from other neurons
		NeuronConfig[] inputsE = new NeuronConfig[3];
		inputsE[0] = hiddenC;
		inputsE[1] = hiddenD;
		inputsE[2] = hiddenBias;
		
		Weight<Double>[] inputEWeights = (Weight<Double>[]) Array.newInstance(Weight.class, 3);
		inputEWeights[0] = new Weight<Double>(new Double(0.2), new Double(0));
		inputEWeights[1] = new Weight<Double>(new Double(-0.9), new Double(0));
		inputEWeights[2] = new Weight<Double>(new Double(0.6), new Double(0));
				
		boolean[] tsE = new boolean[3];
		tsE[0] = false;
		tsE[1] = false;
		tsE[2] = false;
		
		Weight<Double> patternEw = null;
		
		outputE = new NeuronConfig(inputsE,inputEWeights,tsE,-999,patternEw, new Double(0),1);
		outputE.setOutputNeuron(true);
		
		//Neuron config end.
		
		
		
		
		layer1.add(inputA);
		layer1.add(inputB);
		layer1.add(inputBias);
		
		layer2.add(hiddenC);
		layer2.add(hiddenD);
		layer2.add(hiddenBias);
		
		layer3.add(outputE);
		
		layers.add(layer1);
		layers.add(layer2);
		layers.add(layer3);
		
		
		return layers;
	
	
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.GenericTopologyBuilder#createNeuronPipePool()
	 */
	public ArrayList<NeuronPipeline> createNeuronPipePool() {
		ArrayList<NeuronPipeline> tmp = new ArrayList<NeuronPipeline>(2);
		tmp.add(new DotProductPipeline(new LinearOutputFunction())); //used by pattern input neuron
		tmp.add(new DotProductPipeline(new SigmoidOutputFunction()));
		tmp.add(new BiasNeuronPipeline());
		
		return tmp;
		
	}

}
