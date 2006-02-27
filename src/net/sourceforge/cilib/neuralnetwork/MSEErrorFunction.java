/*
 * MSEErrorFunction.java
 * 
 * Created on Jul 24, 2004
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
package net.sourceforge.cilib.neuralnetwork;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.problem.Fitness;

/**
 * @author stefanv
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MSEErrorFunction implements NNError {

	protected double value = 0;
	protected int noOutputs = 0;
	protected int noPatterns = 0;
	

	/**
	 * @param value
	 */
	public MSEErrorFunction(int noOutputs_, int noPatterns_) {
		
		this.value = 0;
		noOutputs = noOutputs_;
		noPatterns = noPatterns_;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.cilib.Problem.Fitness#getValue()
	 */
	public Double getValue() {
		return new Double(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Fitness arg0) {

		if (!(arg0 instanceof MSEErrorFunction)) {
			throw new IllegalArgumentException("Incorrect class instance passed");
		}

		return (Double.valueOf(value)).compareTo((Double) ((MSEErrorFunction) arg0).getValue());
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNError#add(net.sourceforge.cilib.NeuralNetwork.Foundation.NNError)
	 */
	public void add(NNError e) {

		if (!(e instanceof MSEErrorFunction)) {
			throw new IllegalArgumentException("Incorrect class instance passed");
		} else {
			this.value += ((Double) ((MSEErrorFunction) e).getValue()).doubleValue();

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNError#compute(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void addIteration(ArrayList output, NNPattern input) {

				
		if (input.getTargetLength() != output.size()){
			throw new IllegalArgumentException("Output and target lenghts don't match");
		} else {
			
			for (int i = 0; i < output.size(); i++){
				this.value += Math.pow( ((Double)input.getTarget().get(i)).doubleValue()
						              - ((Double)output.get(i)).doubleValue() , 2);
												
			}
			
		}
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNError#postEpochActions()
	 */
	public void postEpochActions() {
		
		this.value /= noOutputs * noPatterns;
	}
	
	public NNError clone(){
		return new MSEErrorFunction(this.noOutputs, this.noPatterns);
	}
	
	/**
	 * @return Returns the noOutputs.
	 */
	public int getNoOutputs() {
		return noOutputs;
	}
	/**
	 * @param noOutputs The noOutputs to set.
	 */
	public void setNoOutputs(int noOutputs) {
		this.noOutputs = noOutputs;
	}
	/**
	 * @return Returns the noPatterns.
	 */
	public int getNoPatterns() {
		return noPatterns;
	}
	/**
	 * @param noPatterns The noPatterns to set.
	 */
	public void setNoPatterns(int noPatterns) {
		this.noPatterns = noPatterns;
	}
		
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNError#setValue(java.lang.Object)
	 */
	public void setValue(Object val) {
		value = ((Double)val).doubleValue();		
	}
	
}