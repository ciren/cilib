/*
 * RandomWeightInitialiser.java
 * 
 * Created on Apr 24, 2005
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
package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;


import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RandomWeightInitialiser implements GenericTopologyVisitor {

	//the range over which values may be initiated. 
	//offset is center from  of the distribution
	//i.e. range = 2, offset 0 means range [-1,1]
	
	double range = 2;
	double offset = 0;
	
	
		
	
	/**
	 * @param range
	 * @param offset
	 */
	public RandomWeightInitialiser(double range, double offset) {
		this.range = range;
		this.offset = offset;
	}
	
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.TopologyVisitors.GenericTopologyVisitor#visitNeuronConfig(net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronConfig)
	 */
	public void visitNeuronConfig(NeuronConfig n) {
		
		//first initialise all neuron-to-neuron weights if they exist
		if (n.getInputWeights() != null){
			for (int i = 0; i < n.getInputWeights().length; i++){
				Weight<Double> w = n.getInputWeights()[i];
				w.setWeightValue( new Double( (Math.random() * range) - range/2 + offset));
			}
		}
		
		//also initialise pattern-to-neuron weight if they exist.
		//default = 1
		if (n.getPatternWeight() != null){
			n.getPatternWeight().setWeightValue(new Double(1.0));
		}

	}

}
