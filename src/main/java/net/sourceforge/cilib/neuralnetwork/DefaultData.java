/*
 * DefaultData.java
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;

/**
 * @author stefanv
 *
 * This class is a "standard" implementation of Fixed Set Learning (FSL).  The source of data is a local text file.
 * The class reads the text file which can have any amount of patterns (one line), separated by a newline.  Each 
 * item should be separated by a space. Each pattern has a variable length input section, followed by a 
 * target section (rest of line).
 * 
 * The populate() method reads all data into the candidate set, after which distributePatterns() is called.  This
 * method then randomly distributes the candidate set patterns into the training, validation and generalisation set.
 */
public class DefaultData implements NeuralNetworkData {

	
	
	protected ArrayList<NNPattern> candidateSet = null;
	protected ArrayList<NNPattern> trainingSet = null;
	protected ArrayList<NNPattern> generalisationSet = null;
	protected ArrayList<NNPattern> validationSet = null;
	protected BufferedReader inputReader = null;
	protected String file;
	
	protected int noInputs;
	
	protected int percentTrain;
	protected int percentGen;
	protected int percentVal;
	protected int percentCan;
	
	/**
	 * @throws IOException 
	 * 
	 */
	public DefaultData(String file_, 
						   int noInputs,
						   int percentCandidate,
						   int percentTrain,
						   int percentGen,
						   int percentVal) {
		
				
		if (percentCandidate + percentTrain + percentGen + percentVal != 100){
			throw new IllegalArgumentException("Percentages for data sets do not add up to 100");
		}
		
		this.noInputs = noInputs;
		
		this.percentGen = percentGen;
		this.percentTrain = percentTrain;
		this.percentVal = percentVal;
		this.percentCan = percentCandidate;
		
		candidateSet = new ArrayList<NNPattern>();
		trainingSet = new ArrayList<NNPattern>();
		generalisationSet = new ArrayList<NNPattern>();
		validationSet = new ArrayList<NNPattern>();
		
		file= file_;
								
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#getNrPatternsCandidateSet()
	 */
	public int getCandidateSetSize() {
		return candidateSet.size();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#getNrPatternsTrainingSet()
	 */
	public int getTrainingSetSize() {
		return trainingSet.size();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#getNrPatternsGeneralisationSet()
	 */
	public int getGeneralisationSetSize() {
		return generalisationSet.size();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#getNrPatternsValidationSet()
	 */
	public int getValidationSetSize() {
		return validationSet.size();
	}

	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#addPatternsFromInput()
	 * 
	 * Adds all patterns from the file into the candidate set.  Distribution into the proper sets happens 
	 * in the method distributePatterns()
	 */
	public void populate() throws IOException {

		try {
			inputReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("File not found...");
			e.printStackTrace();
			System.exit(1);
		}		
		
		while(inputReader.ready()) {
			String line = inputReader.readLine();
			
			StringTokenizer token = new StringTokenizer(line, " ");
			
			if(token.countTokens() <= this.noInputs)
				throw new IOException("Record lengths dont match!");
			
			ArrayList<Double> input = new ArrayList<Double>(this.noInputs);		
			ArrayList<Double> target = new ArrayList<Double>();
			
			for (int i = 0; i < noInputs; i++){
				input.add(new Double(token.nextToken()));
			}
			
			while(token.hasMoreTokens()){
				target.add(new Double(token.nextToken()));
			}
			
			candidateSet.add(new StandardPattern(input,target));
									
		}//end while
		
		distributePatterns();
	}
	
	
	
	
	//Removes patterns from the candidate set and inserts them into the relevant sets as 
	//specified by the respective percentages.
	public void distributePatterns(){
		
		int totalPatterns = this.candidateSet.size();
		
		int trainsetSize = (int)Math.floor(totalPatterns * this.percentTrain / 100);
		int gensetSize = (int)Math.floor(totalPatterns * this.percentGen / 100);
		int valsetSize = (int)Math.floor(totalPatterns * this.percentVal / 100);
		
		System.out.println("DataSource: planned distribution of patterns:");
		System.out.println("=====================================");
		System.out.println("training set      :" + trainsetSize);
		System.out.println("genralisation set :" + gensetSize);
		System.out.println("validation set    :" + valsetSize);
		
		//initiate training set randomly from Candidate set for trainsetSize patterns
		for (int t = 0; t < trainsetSize; t++){
			
			int target = (int)Math.random() * candidateSet.size();
			trainingSet.add(candidateSet.remove(target));
		}
		
		
		//initiate gen set randomly from Candidate set for trainsetSize patterns
		for (int t = 0; t < gensetSize; t++){
			
			int target = (int)Math.random() * candidateSet.size();
			generalisationSet.add(candidateSet.remove(target));
		}
		
		//initiate val set randomly from Candidate set for trainsetSize patterns
		for (int t = 0; t < valsetSize; t++){
			
			int target = (int)Math.random() * candidateSet.size();
			validationSet.add(candidateSet.remove(target));
		}
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#getTrainingSetIterator()
	 */
	public NeuralNetworkDataIterator getTrainingSetIterator() {
		return new LinearDataIterator(this, trainingSet);
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#getGeneralisationSetIterator()
	 */
	public NeuralNetworkDataIterator getGeneralisationSetIterator() {
		return new LinearDataIterator(this, generalisationSet);
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#getValidationSetIterator()
	 */
	public NeuralNetworkDataIterator getValidationSetIterator() {
		return new LinearDataIterator(this, validationSet);
	}
	
	public NeuralNetworkDataIterator getCandidateSetIterator() {
		return new LinearDataIterator(this, candidateSet);
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkData#activeLearningUpdate()
	 */
	public void activeLearningUpdate(Object input) {
		//Do nothing as no active learning defined.
		//refine in subclasses.
	}


	/* (non-Javadoc)
	 * Shuffles the training set to avoid "memorisation" based on training order.
	 */
	public void shuffleTrainingSet() {
		
		ArrayList<NNPattern> tmp = new ArrayList<NNPattern>();
		int size = trainingSet.size();
		
		for (int i = 0; i < size; i++){
			int target = (int)(Math.random() * trainingSet.size());
			//System.out.println("trainsize = " + trainingSet.size() + " target pattern = " + target +
			//		           " value = " + trainingSet.get(target));
			tmp.add(trainingSet.remove(target));	
			
		}//end for
				
		trainingSet = null;
		trainingSet = tmp;
				
		
	}

}
