/*
 * TestErrorINCLearnWithTrainer.java
 * 
 * Created on Apr 23, 2005
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
/*
 * Created on 2005/04/23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.testarea;

import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.MSEErrorFunction;
import net.sourceforge.cilib.neuralnetwork.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.ErrorBasedIncrementalLearningRealData;
import net.sourceforge.cilib.neuralnetwork.generic.evaluationstrategies.ErrorBasedIncrementalLearningEvaluationStrategy;
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
public class TestErrorINCLearnWithTrainer {

	
	
	public static void main(String[] args) {
		
		int[] sizes = new int[3];
		sizes[0] = 3;
		sizes[1] = 6;
		sizes[2] = 1;
		
		Weight<Double> base= new Weight<Double>(new Double(0.5), new Double(0));
		
		
	//	GenericTopology topo = new GenericTopology(new FFNNStaticTopologyBuilder());
		GenericTopology topo = new GenericTopology(new FFNNgenericTopologyBuilder(sizes, base));
				
		topo.acceptVisitor(new RandomWeightInitialiser(2,0));
		
		FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy(topo, new SquaredErrorFunction(), 0.5, 0.2);
		
		NeuralNetworkData data = null;
		
		try {
			//data = new DefaultRealData("c:\\temp\\easyfunc.txt", 2, 80, 10, 10);
			data = new ErrorBasedIncrementalLearningRealData("c:\\temp\\easyfunc.txt", 2, 90, 0, 0, 10,topo
															, new MSEErrorFunction(topo.getLayer(2).size(),1));
			data.populate();
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		
		System.out.println("data stats before training:\n\n");

		System.out.println("candidate set size      : " + data.getCandidateSetSize());
		System.out.println("training set size       : " + data.getTrainingSetSize());
		System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
		System.out.println("validation set size     : " + data.getValidationSetSize());
		
		
		NNError err = new MSEErrorFunction(1,data.getTrainingSetSize());
		
		
		ErrorBasedIncrementalLearningEvaluationStrategy eval = new ErrorBasedIncrementalLearningEvaluationStrategy(topo, 
				data, 
				err,
				trainer);
		
//		laaste eie thingie
		
		NeuralNetworkProblem NNprob = new NeuralNetworkProblem(eval);
//		problem (extend van Optimisation problem) met thingies as parameters
		
		NeuralNetworkController NNControl = new NeuralNetworkController(NNprob);
//		Algorithm vir control van problem
		
		NNControl.addStoppingCondition(new MaximumIterations(1000));
//		add stopping kondisie
		
		System.out.println("Configuration completed...");
//		-----------------------------------------------------------------------------------------------------------

NNControl.initialise();
//needed

NNControl.run();
////run die stuff
		
		

ArrayList<Double> in = new ArrayList<Double>(2);

in.add(new Double(1)); in.add(new Double(1));

StandardPattern p = new StandardPattern(in, null);

ArrayList result = topo.evaluate(p);

System.out.println("test result - 1 and 1 = 0.6  -->  : " + ((Double)result.get(0)).doubleValue());

System.out.println("data stats:\n\n");

System.out.println("candidate set size      : " + data.getCandidateSetSize());
System.out.println("training set size       : " + data.getTrainingSetSize());
System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
System.out.println("validation set size     : " + data.getValidationSetSize());


		
		
		
		
		
		
		
		
		
		
	}
}
