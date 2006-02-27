/*
 * EvaluationStrategy.java
 * 
 * Created on Nov 30, 2004
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
package net.sourceforge.cilib.neuralnetwork.foundation;


import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;



/**
 * @author stefanv
 *
 * The EvaluationStrategy class is used to implement any behavioural charateristics of a NN implementation.
 * It is the Controller part of a MVC (Model-View-Controller) pattern.
 * 
 * EvaluationStrategy has elements of the Mediator, Template Method and Strategy Design Patterns.  The 
 * performLearning() method uses a Template Method pattern to keep track of the total patterns presented.
 * The learningEpoch() and Evaluate() methods are abstract and so doing implements a Strategy pettern.  The 
 * behaviour and flow of the entire NN system is controlled by subclasses of EvaluationStrategy, with 
 * learningEpoch() and Evaluate() as entry points.  From these methods all aspects of the NN are brought 
 * together such as NeuralNetworkTopology, NeuralNetworkData, TrainingStrategy, etc.
 *  
 */
public abstract class EvaluationStrategy {

	protected NeuralNetworkTopology topology = null;
	protected TrainingStrategy trainer = null;	
	protected NeuralNetworkData data = null;
	protected NNError baseError = null;
	protected int nrEvaluationsPerEpoch;
	protected int totalEvaluations;
	
	protected NNError errorDt = null;
	protected NNError errorDg = null;
	protected NNError errorDv = null;
	
	
	/**
	 * @param topology
	 * @param data
	 */
	public EvaluationStrategy(NeuralNetworkTopology topology_,
							  NeuralNetworkData data_,
							  NNError base,
							  TrainingStrategy trainer_) {
		
		if ((data_ == null) || (topology_ == null) || (base == null) || (trainer_ == null)) {			
			throw new IllegalArgumentException("Evaluation Strategy error: a required object was null");
		}
				
		this.topology = topology_;
		this.data = data_;
		
		this.baseError = base;
		this.errorDg = base.clone();
		this.errorDt = base.clone();
		this.errorDv = base.clone();
		
		
		nrEvaluationsPerEpoch = 0;
		totalEvaluations = 0;
		trainer = trainer_;
		
		
			
	}
	
	/**
	 * 
	 * @return
	 */
	public void performLearning(){
		
		learningEpoch();
		totalEvaluations += nrEvaluationsPerEpoch;
		nrEvaluationsPerEpoch = 0;
		
	}
	
	protected abstract void learningEpoch();

	public abstract Object evaluate(NNPattern p);
	

	/**
	 * @return
	 */
	public NeuralNetworkTopology getTopology() {
		return topology;
	}


	/**
	 * @param topology2
	 */
	public void setTopology(NeuralNetworkTopology topology2) {
		this.topology = topology2;
	}
	
	
	/**
	 * @return Returns the errorDg.
	 */
	public NNError getErrorDg() {
		return errorDg;
	}
	/**
	 * @param errorDg The errorDg to set.
	 */
	public void setErrorDg(NNError errorDg) {
		this.errorDg = errorDg;
	}
	/**
	 * @return Returns the errorDt.
	 */
	public NNError getErrorDt() {
		return errorDt;
	}
	/**
	 * @param errorDt The errorDt to set.
	 */
	public void setErrorDt(NNError errorDt) {
		this.errorDt = errorDt;
	}
	/**
	 * @return Returns the errorDv.
	 */
	public NNError getErrorDv() {
		return errorDv;
	}
	/**
	 * @param errorDv The errorDv to set.
	 */
	public void setErrorDv(NNError errorDv) {
		this.errorDv = errorDv;
	}
	/**
	 * @return Returns the nrEvaluationsPerEpoch.
	 */
	public int getNrEvaluationsPerEpoch() {
		return nrEvaluationsPerEpoch;
	}
	/**
	 * @param nrEvaluationsPerEpoch The nrEvaluationsPerEpoch to set.
	 */
	public void setNrEvaluationsPerEpoch(int nrEvaluationsPerEpoch) {
		this.nrEvaluationsPerEpoch = nrEvaluationsPerEpoch;
	}
	/**
	 * @return Returns the totalEvaluations.
	 */
	public int getTotalEvaluations() {
		return totalEvaluations;
	}
	/**
	 * @param totalEvaluations The totalEvaluations to set.
	 */
	public void setTotalEvaluations(int totalEvaluations) {
		this.totalEvaluations = totalEvaluations;
	}
}
