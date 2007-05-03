/*
 * Created on 2004/12/07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;

/**
 * @author stefanv
 *
 */
public class GenericData implements NeuralNetworkData {
	
	
	protected ArrayList<NNPattern> candidateSet = null;
	protected ArrayList<NNPattern> trainingSet = null;
	protected ArrayList<NNPattern> generalisationSet = null;
	protected ArrayList<NNPattern> validationSet = null;
	
	protected DataDistributionStrategy distributor;
	protected NeuralNetworkDataIterator iter = null;
	
	public GenericData() {
							
		this.candidateSet = null;
		this.trainingSet = null;
		this.generalisationSet = null;
		this.validationSet = null;
		
		this.distributor = null;
		this.iter = new LinearDataIterator(this, null);
										
	}
	
	public void initialize(){
					
		candidateSet = new ArrayList<NNPattern>();
		trainingSet = new ArrayList<NNPattern>();
		generalisationSet = new ArrayList<NNPattern>();
		validationSet = new ArrayList<NNPattern>();
		
		if (this.distributor == null){
			throw new IllegalArgumentException("Required data distributor object was null during initialization");
		}
		this.distributor.initialize();
		
		this.distributor.populateData(this.candidateSet,
									  this.trainingSet,
									  this.generalisationSet,
									  this.validationSet);
	}
	
	
		
	public void setDistributor(DataDistributionStrategy distributor) {
		this.distributor = distributor;
	}

	public int getCandidateSetSize() {
		return candidateSet.size();
	}

	
	public int getTrainingSetSize() {
		return trainingSet.size();
	}

	
	public int getGeneralisationSetSize() {
		return generalisationSet.size();
	}

	
	public int getValidationSetSize() {
		return validationSet.size();
	}

		
		
	public NeuralNetworkDataIterator getTrainingSetIterator() {
		return new LinearDataIterator(this, trainingSet);
	}


	
	public NeuralNetworkDataIterator getGeneralisationSetIterator() {
		return new LinearDataIterator(this, generalisationSet);
	}


	
	public NeuralNetworkDataIterator getValidationSetIterator() {
		return new LinearDataIterator(this, validationSet);
	}
	
	public NeuralNetworkDataIterator getCandidateSetIterator() {
		return new LinearDataIterator(this, candidateSet);
	}


	
	public void activeLearningUpdate(Object input) {
		//Do nothing as no active learning defined.
		//refine in subclasses.
	}
	
		
	public void shuffleTrainingSet(){
		
		Collections.shuffle(this.trainingSet, new Random(System.currentTimeMillis()));
							
	}
	
	

	
	public void printStatistics() {
		System.out.println("Data distribution :\n\n");

		System.out.println("Candidate set size      : " + this.getCandidateSetSize());
		System.out.println("Training set size       : " + this.getTrainingSetSize());
		System.out.println("Generalisation set size : " + this.getGeneralisationSetSize());
		System.out.println("Validation set size     : " + this.getValidationSetSize());
	}
	

}
