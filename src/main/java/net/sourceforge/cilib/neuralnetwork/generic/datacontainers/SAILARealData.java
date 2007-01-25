/*
 * SAILARealData.java
 * 
 * Created on Jul 06, 2005
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
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import net.sourceforge.cilib.neuralnetwork.DefaultData;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.StandardLayerIterator;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SAILARealData extends DefaultData {
	
	GenericTopology topology;

	/**
	 * @param file_
	 * @param noInputs
	 * @param percentTrain
	 * @param percentGen
	 * @param percentVal
	 */
	public SAILARealData(String file_, int noInputs, int percentCan, int percentTrain, 
						 int percentGen, int percentVal, GenericTopology t) {
		
		super(file_, noInputs, percentCan, percentTrain, percentGen, percentVal);
		
		topology = t;
		
	}
	
	
	public void distributePatterns(){
		//initially distribute patterns into the Candidate set, Training, validation and Generalisation set.
		
		super.distributePatterns();
		
		//add one pattern with SAILA operator 4.29
		this.activeLearningUpdate(null);
		
	}
	
	
	public void activeLearningUpdate(Object input) {
		//At every update interval, determine pattern informativeness of all 
		//patterns in Candidate set, select most informative one 
		//to remove from candidate set and add to training set.
		
		NNPattern mostInformative = null;
		double bestInformativeness = -999999;
		
		//Iterate over each pattern p in the Candidate set Dc
		NeuralNetworkDataIterator DcIter = this.getCandidateSetIterator();
		
		while(DcIter.hasMore()){
			//System.out.println("Hello...........................................");
			NNPattern p = DcIter.value();
		
			//Evaluate p against current NN topology.
			this.topology.evaluate(p);
		
			//determine informativeness of p
			double pInformativeness = this.calculateInformativeness(p);
		
			//if p = highest informativeness so far, set it as prospective update pattern.
			if (pInformativeness > bestInformativeness){
				mostInformative = p;
				bestInformativeness = pInformativeness;
			}
		
			DcIter.next();
		}//end iterate Dc
		
		//remove bestInformative pattern from candidate set Dc and add to training set Dt.
		if(candidateSet.size() != 0){
			this.candidateSet.remove(mostInformative);
			this.trainingSet.add(mostInformative);
		}
		
	}


	/**
	 * @param p
	 */
	private double calculateInformativeness(NNPattern p) {
		
		double bestOutputSo = -999999.0;
		
		//Iterate over output neurons Ok
		StandardLayerIterator oIter = topology.getLayerIterator(2);
		
		while(oIter.hasMore()){
			
			double normOSVec = 0;
			
			if(oIter.value().getInputWeights() != null){
				//calculate the normalised output sensitivity vector (sum-norm)
				normOSVec = this.normalisedOSVector(oIter.value());
			}
			
			//select the max output unit, equation 4.2
			if (normOSVec > bestOutputSo){
				bestOutputSo = normOSVec;
			}
			
			oIter.nextNeuron();
		}//end oIter
		
		return bestOutputSo;
	}


	/**
	 * @return
	 */
	private double normalisedOSVector(NeuronConfig Ok) {
		
		int nrInputs = Ok.getInput()[0].getInput().length;
		double[] inputVector = new double[nrInputs];
		double outputDeriv = (1.0 - ((Double)Ok.getCurrentOutput()).doubleValue())
							 * ((Double)Ok.getCurrentOutput()).doubleValue();
		
		//For each input unit
		for (int i = 0; i < nrInputs; i++){
			
			//System.out.println("Normaliser: input = " + i);
			
			inputVector[i] = 0.0;
		
			//Iterate over Hidden units
			for (int h = 0; h < Ok.getInput().length; h++){
				//System.out.println("\thidden = " + h);
				
				NeuronConfig hidden = Ok.getInput()[h];
				//check that no bias unit used.
				if (hidden.getInputWeights() != null){
					//Evaluate equation E6 from SAILA paper, adding result to the input unit vector.
					inputVector[i] += (Double) Ok.getInputWeights()[h].getWeightValue() 
						              * ( (1.0 - ((Double)hidden.getCurrentOutput()).doubleValue()) * 
							          ((Double)hidden.getCurrentOutput()).doubleValue() ) *
							          (Double) hidden.getInputWeights()[i].getWeightValue();	
				}
			}
			
			inputVector[i] *= outputDeriv;
					
		} //end for each input
		
		//Normalise the inputVector (sum-norm), equation 4.4
		double sumNorm = 0.0;
		
		for (int i = 0; i < nrInputs; i++){
			sumNorm += Math.abs(inputVector[i]);
		}
		
		return sumNorm;
	}

	
	
	
	
}
