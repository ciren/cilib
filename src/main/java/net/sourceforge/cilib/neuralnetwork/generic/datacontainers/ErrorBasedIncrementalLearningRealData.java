/*
 * ErrorBasedIncrementalLearningRealData.java
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

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.DefaultData;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;


/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ErrorBasedIncrementalLearningRealData extends DefaultData {
	
	protected GenericTopology topology;
	protected NNError baseError;
	
	protected NNError highestError = null;

	/**
	 * @param file_
	 * @param noInputs
	 * @param percentTrain
	 * @param percentGen
	 * @param percentVal
	 */
	public ErrorBasedIncrementalLearningRealData(String file_, int noInputs, int percentCan, int percentTrain, 
						 int percentGen, int percentVal, GenericTopology t, NNError base) {
		
		super(file_, noInputs, percentCan, percentTrain, percentGen, percentVal);
		
		topology = t;
		this.baseError = base.clone();
		highestError = baseError.clone();
		highestError.setValue(new Double(-99999999));
		
	}
	
	
	public void distributePatterns(){
		//initially distribute patterns into the Candidate set, Training, validation and Generalisation set.
		
		super.distributePatterns();
		
		this.activeLearningUpdate(null);
		
	}
	
	
	public void activeLearningUpdate(Object input) {
		//At every update interval, determine pattern informativeness of all 
		//patterns in Candidate set, select most informative one 
		//to remove from candidate set and add to training set.
		
		
		if(candidateSet.size() != 0){
			NNPattern mostInformative = null;
			
			//Iterate over each pattern p in the Candidate set Dc
			NeuralNetworkDataIterator DcIter = this.getCandidateSetIterator();
			
			while(DcIter.hasMore()){
				
				NNPattern p = DcIter.value();
				
				//Evaluate p against current NN topology.
				ArrayList output = this.topology.evaluate(p);
				
				//determine informativeness of p
				NNError patternError = baseError.clone();
				patternError.addIteration(output, p);
				
				
				//if p = highest informativeness so far, set it as prospective update pattern.
				if (patternError.compareTo(highestError) > 0){
					mostInformative = p;
					highestError = patternError;
				}
				
				DcIter.next();
			}//end iterate Dc
			
			
			//remove bestInformative pattern from candidate set Dc and add to training set Dt.
			highestError.setValue(new Double(-999999));
			this.candidateSet.remove(mostInformative);
			this.trainingSet.add(mostInformative);
		}
				
	}

	
}
