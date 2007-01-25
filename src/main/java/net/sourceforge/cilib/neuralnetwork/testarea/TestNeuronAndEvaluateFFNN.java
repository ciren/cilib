/*
 * TestNeuronAndEvaluateFFNN.java
 * 
 * Created on Mar 23, 2005
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

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.RandomWeightInitialiser;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.ErrorSignal;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.FFNN_GD_TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.SquaredErrorFunction;


/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestNeuronAndEvaluateFFNN {
	
	
	
	public static void main(String[] args) {
		
		int[] sizes = new int[3];
		sizes[0] = 3;
		sizes[1] = 3;
		sizes[2] = 1;
		
		Weight<Double> base= new Weight<Double>(new Double(1), new Double(0));
		
		GenericTopology topo = new GenericTopology(new FFNNgenericTopologyBuilder(sizes, base));
		
		RandomWeightInitialiser rw = new RandomWeightInitialiser(2,0);
		topo.acceptVisitor(rw);
		
		//GenericTopology topo = new GenericTopology(new FFNNStaticTopologyBuilder());
			
		System.out.println("Sigmoid function test ===============================================");
		SigmoidOutputFunction s = new SigmoidOutputFunction();
		System.out.println("raw output for  0    = " + s.computeFunction(new Double(0)));
		System.out.println("raw output for  0.5  = " + s.computeFunction(new Double(0.5)));
		System.out.println("raw output for  1    = " + s.computeFunction(new Double(1)));
		System.out.println("raw output for  -0.5 = " + s.computeFunction(new Double(-0.5)));
		System.out.println("raw output for  -1   = " + s.computeFunction(new Double(-1)));
		System.out.println("raw output for  10   = " + s.computeFunction(new Double(10)));
		System.out.println("raw output for  -10  = " + s.computeFunction(new Double(-10)));
		
		//TestNeuronAndEvaluateFFNN test = new TestNeuronAndEvaluateFFNN();
		
		
		
		
		//NeuronPipeline neuron = new DotProductSigmoidPipeline(new SigmoidOutputFunction(1));
		
		ArrayList<Double> ins = new ArrayList<Double>(2);
		ins.add(new Double(-0.765));
		ins.add(new Double(0.112));
		
		ArrayList<Double> targt = new ArrayList<Double>(1);
		targt.add(new Double(1.9));
		NNPattern p = new StandardPattern(ins,targt);
		
		ArrayList result = new ArrayList();
		
		for (int counter = 0; counter < 1; counter++){
			
			result = topo.evaluate(p);			
						
			Double real = (Double)result.get(0);
			
			System.out.println("result size = " + result.size());			
			System.out.println("-----------------------------------------------------");
			System.out.println("The final output result: " + real.toString());
			System.out.println("-----------------------------------------------------");
			
			ErrorSignal delta = new SquaredErrorFunction();
			
			FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy(topo, delta, 0.5, 0.9);
			trainer.invokeTrainer(p);
			
		}
		
		//after training
			 result = topo.evaluate(p);
		
		
		Double real = (Double)result.get(result.size()-1);
		
		System.out.println("-----------------------------------------------------");
		System.out.println("The final output after training = result: " + real.toString());
		System.out.println("-----------------------------------------------------");
		
		
	}
}
