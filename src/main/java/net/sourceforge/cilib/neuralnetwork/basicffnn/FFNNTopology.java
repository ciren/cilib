/*
 * FFNNTopology.java
 * 
 * Created on Dec 07, 2004
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
package net.sourceforge.cilib.neuralnetwork.basicffnn;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;


/**
 * @author stefanv
 *
 * This is a basic Feedforward Neural network implementation.  Its sole purpose is to illustrate the working of
 * the CIlib Neural network framework and to show how it can be used to create a custom NN.
 */
public class FFNNTopology implements NeuralNetworkTopology {

	protected double[] weights = null;
	
	int nrInputBiased;  //includes bias unit, thus true input = nrInput - 1
	int nrHiddenBiased; //includes bias unit, thus true hidden = nrHidden - 1
	int nrOutput;
	
	int nrWeights; //nr of weights in the NN
    double []hiddenResult;
	double []outputResult;
	ArrayList<Double> output = null;
	NNPattern lastPattern = null;

	private double learnRateEta;
	private double momentumAlpha;

	private double[] oldWeightChangesHO;

	private double[] oldWeightChangesIH;

	private double newWeightChanges;

	
	
	/**
	 * 
	 */
	public FFNNTopology(int inputNr, 
            			int hiddenNr, 
            			int outputNr,
            			double learn,
            			double moment) {
		
		  learnRateEta = learn;
		  momentumAlpha = moment;
		 
		  nrWeights = ((inputNr + 1) * hiddenNr); //for hidden layer bias unit
		  nrWeights += ((hiddenNr + 1) * outputNr); //for output units connected
		  //thus bias units weights are included too

		  nrInputBiased = inputNr + 1;
		  nrHiddenBiased = hiddenNr + 1;
		  nrOutput = outputNr; //hier moet nog 'n bias kom erens

		  weights = new double[nrWeights];
		  for (int i = 0; i < nrWeights; i++){
		    weights[i] = (Math.random() * 2) - 1;        
		  }

		  hiddenResult = new double[nrHiddenBiased]; //no bias unit input
		  //keep results from dot product of I-H layers
		  for (int i = 0; i < nrHiddenBiased; i++)
		    hiddenResult[i] = 0.0;

		  outputResult = new double[nrOutput]; //no bias unit input
		  //keep results from dot product of H-O layers
		  for (int i = 0; i < nrOutput; i++)
		    outputResult[i] = 0.0;
			
		  

		  oldWeightChangesHO = new double[nrHiddenBiased * nrOutput];
		  for (int i = 0; i < nrHiddenBiased * nrOutput; i++)
		    oldWeightChangesHO[i] = 0.0;

		  oldWeightChangesIH = new double[(nrHiddenBiased-1) * nrInputBiased];
		  for (int i = 0; i < (nrHiddenBiased-1) * nrInputBiased; i++){
		    oldWeightChangesIH[i] = 0.0;
		  }
		  newWeightChanges=0;
		  		  	
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkTopology#evaluate(net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern)
	 */
	public ArrayList evaluate(NNPattern p) {
		
//		still need to check here for right length vector then exception
		  //Does not delete the activation of hidden or output units, used to feed back...
		  //zeroes activation before doinjg a feedforward though...

		  if(p.getInputLength() != this.nrInputBiased - 1)
		  	throw new IllegalArgumentException("Input in Pattern does not match topology input number");
		
		  int Hcounter = 0;

		  for (int i = 0; i < (nrHiddenBiased); i++) {
		    hiddenResult[i] = 0.0;
		  }//end for
		  
		  hiddenResult[nrHiddenBiased - 1] = -1; //Initialize bias unit

		  for (int i = 0; i < (nrOutput); i++) {
		    outputResult[i] = 0.0;
		  }//end for
		  //---------------------------------------------------------------------------------


		  for	(Hcounter = 0; Hcounter < (nrHiddenBiased-1);	Hcounter++) {
		    for (int Icounter = 0; Icounter < nrInputBiased; Icounter++) {
		      if (Icounter == (nrInputBiased - 1)) //Bias	unit
		        hiddenResult[Hcounter]+= -weights[Hcounter*nrInputBiased + Icounter];
		      else {
		      	double in = ((Double)p.getInput().get(Icounter)).doubleValue();
		        hiddenResult[Hcounter]+= weights[Hcounter*nrInputBiased + Icounter]*in;
		      }
		    }//end for
		    hiddenResult[Hcounter] = 1.0 / (1.0 + Math.exp(-hiddenResult[Hcounter]));
		    //sigmoid activation
		  }//end for

		  //========================================================================================
		  //output neurons:
		  int start = nrInputBiased * (nrHiddenBiased - 1);

		  for (int Ocounter = 0; Ocounter < nrOutput; Ocounter++){
		    for (Hcounter = 0; Hcounter < nrHiddenBiased; Hcounter++){
		      if (Hcounter == (nrHiddenBiased - 1)) //Bias	unit
		        outputResult[Ocounter]+= -weights[start + Ocounter * nrHiddenBiased + Hcounter];
		      else
		        outputResult[Ocounter]+= weights[start + Ocounter * nrHiddenBiased + Hcounter]*(hiddenResult[Hcounter]);
		    } //end for Hcounter
		    outputResult[Ocounter] = 1.0 / (1.0 + Math.exp(-outputResult[Ocounter]));
		  } //end for Ocounter

		  
		  //convert to ArrayList...
		  
		  ArrayList<Double> temp = new ArrayList<Double>();
		  output = new ArrayList<Double>();
		  for (int i = 0; i < nrOutput;i++){
		  	temp.add(new Double(outputResult[i]));
		  	output.add(new Double(outputResult[i]));
		  }
		  
		  lastPattern = p;
		  
		  return temp;
	}

	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkTopology#getWeights()
	 */
	public ArrayList<Double> getWeights() {
		ArrayList<Double> temp = new ArrayList<Double>();
		for (int i = 0; i < nrWeights;i++){
		 	temp.add(new Double(weights[i]));
		}
		return temp;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkTopology#setWeights(java.lang.Object)
	 */
	public void setWeights(ArrayList w) {
		

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkTopology#train(net.sourceforge.cilib.NeuralNetwork.Foundation.NNError)
	 */
	public void train() {
		
        //Take one patern and feedforward.  Use result to determine error and
		//adjust weights accordingly...
		
		//double avgOutputError = 0.0;
		double []errorSignalOutput = new double[nrOutput];
		double []errorSignalHidden = new double[nrHiddenBiased];
		
		for (int i = 0; i < nrOutput; i++){
			double temp = ((Double) lastPattern.getTarget().get(i)) - ((Double)output.get(i));
			double temp2 = ((Double)output.get(i));
			//avgOutputError += temp*temp;
			errorSignalOutput[i] =  -temp * (1.0 - temp2) * temp2;
		}
		
		//avgOutputError /= nrOutput; //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		
		//now for hidden error signals...
		int start = nrInputBiased * (nrHiddenBiased - 1);
		
		for (int j = 0; j < nrHiddenBiased; j++){
			errorSignalHidden[j] = 0.0;
			for (int k = 0; k < nrOutput; k++){      
				errorSignalHidden[j] += errorSignalOutput[k] * weights[start + j + nrOutput * k ] * 
										(1.0 - hiddenResult[j]) * (hiddenResult[j]);
			} //end for i
		} //end for j (hidden unit count)
		
		//Weight Changes...
		//========================================================= 
		
		
		for (int Ocounter = 0; Ocounter < nrOutput; Ocounter++){
			double temp = (-1.0 * learnRateEta) *	errorSignalOutput[Ocounter];
			for (int Hcounter = 0; Hcounter < nrHiddenBiased; Hcounter++){
				int temp2 = Ocounter * nrHiddenBiased + Hcounter;
				newWeightChanges =  temp * hiddenResult[Hcounter];
				weights[start + temp2] += newWeightChanges + momentumAlpha * oldWeightChangesHO[temp2];
				oldWeightChangesHO[temp2] = newWeightChanges;
			} //enmd for Hcounter
		} //end for Ocounter                                        
		
		
		for (int Hcounter = 0; Hcounter < (nrHiddenBiased - 1); Hcounter++){
			for (int Icounter = 0; Icounter < nrInputBiased; Icounter++){
				int temp = Hcounter*nrInputBiased + Icounter;
				if (Icounter != (nrInputBiased-1)) {
					newWeightChanges = (-learnRateEta) * errorSignalHidden[Hcounter] * ((Double)lastPattern.getInput().get(Icounter));
				}
				else {
					newWeightChanges = (-learnRateEta) * errorSignalHidden[Hcounter] * -1;
				}
				
				weights[temp] += newWeightChanges + momentumAlpha * oldWeightChangesIH[temp];
				oldWeightChangesIH[temp] = newWeightChanges;
			} //enmd for Ocounter
		} //end for Hcounter
		
				
	}


}
