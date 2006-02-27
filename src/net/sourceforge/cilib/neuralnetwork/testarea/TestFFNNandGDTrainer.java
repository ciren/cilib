/*
 * TestFFNNandGDTrainer.java
 * 
 * Created on May 23, 2005
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

import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.DefaultData;
import net.sourceforge.cilib.neuralnetwork.MSEErrorFunction;
import net.sourceforge.cilib.neuralnetwork.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.evaluationstrategies.FFNNEvaluationStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.RandomWeightInitialiser;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.FFNN_GD_TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.SquaredErrorFunction;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestFFNNandGDTrainer {

	
	
	public static void main(String[] args) {
		
		int[] sizes = new int[3];
		sizes[0] = 3;
		sizes[1] = 6;
		sizes[2] = 1;
		
		Weight<Double> base= new Weight<Double>(new Double(0.5), new Double(0));
		
		
	//	GenericTopology topo = new GenericTopology(new FFNNStaticTopologyBuilder());
		GenericTopology topo = new GenericTopology(new FFNNgenericTopologyBuilder(sizes, base));
				
		topo.acceptVisitor(new RandomWeightInitialiser(2,0));
		
		FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy(topo, new SquaredErrorFunction(), 0.1, 0.9);
		
		NeuralNetworkData data = null;
		
		try {
			//data = new DefaultRealData("c:\\temp\\easyfunc.txt", 2, 0, 60, 20, 20);
			data = new DefaultData("c:\\Stefan University\\masters\\datasets\\F2.txt", 2, 0, 60, 20, 20);
			
			data.populate();
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		
		
		NNError err = new MSEErrorFunction(1,data.getTrainingSetSize());
		
		
		FFNNEvaluationStrategy eval = new FFNNEvaluationStrategy(topo, 
				data, 
				err,
				trainer);
		
//		laaste eie thingie
		
		NeuralNetworkProblem NNprob = new NeuralNetworkProblem(eval);
//		problem (extend van Optimisation problem) met thingies as parameters
		
		NeuralNetworkController NNControl = new NeuralNetworkController(NNprob);
//		Algorithm vir control van problem
		
		NNControl.addStoppingCondition(new MaximumIterations(2000));
//		add stopping kondisie
		
		System.out.println("Configuration completed...");
//		-----------------------------------------------------------------------------------------------------------

NNControl.initialise();
//needed

System.out.println("About to run simulation...");	

NeuralNetworkDataIterator duh = data.getTrainingSetIterator();

NNPattern p2 = duh.value();
System.out.println("TEST ToString : " + p2.toString());

System.out.println("Input = " + p2.getInput().get(0) + " target = " + p2.getTarget().get(0));

NNControl.run();
////run die stuff
		
	

ArrayList<Double> in = new ArrayList<Double>(1);

in.add(new Double(0.5)); 

StandardPattern p = new StandardPattern(in, null);

ArrayList result = topo.evaluate(p);

System.out.println("test result input = 0.5, output should be 0.25  -->  : " + ((Double)result.get(0)).doubleValue());

System.out.println("data stats:\n\n");

System.out.println("candidate set size      : " + data.getCandidateSetSize());
System.out.println("training set size       : " + data.getTrainingSetSize());
System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
System.out.println("validation set size     : " + data.getValidationSetSize());


		
		
		
		
		
		
		
		
		
		
	}
}
