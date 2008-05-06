/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.neuralnetwork.generic.evaluationmediators;

import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
public class PSOMediator extends EvaluationMediator {
	
	protected NeuralNetworkDataIterator iteratorDt = null;
	
	public PSOMediator() {
		super();
	}
	
	public void initialize(){
		
		super.initialize();
		
	}
	
	
	public Vector evaluate(NNPattern p) {
		return topology.evaluate(p);	
	}
	
	
	protected void learningEpoch() {
		
		this.resetError(this.errorDt);
		this.setErrorNoPatterns(this.errorDt, this.data.getTrainingSetSize());
		
		iteratorDt = data.getTrainingSetIterator();
		
		//iterate over each applicable pattern in training dataset
		while(iteratorDt.hasMore()){
			
			Vector output = topology.evaluate(iteratorDt.value());
			this.nrEvaluationsPerEpoch++;
			
			//compute the per pattern error.
			this.computeErrorIteration(this.errorDt, output, iteratorDt.value());
			
			iteratorDt.next();
		}
				
		//finalise errors
		this.finaliseErrors(this.errorDt);
		
	}
	
}
