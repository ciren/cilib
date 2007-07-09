/*
 * Created on 2005/03/23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.neuralnetwork.basicFFNN.FFNNTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.LayeredGenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.FFNNgenericTopologyBuilder;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.ErrorSignal;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.FFNN_GD_TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.SquaredErrorFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;


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
		
		Weight base= new Weight(new Real(1));
		
		//GenericTopology topo = new GenericTopology();
		GenericTopology topo = new LayeredGenericTopology();
		FFNNgenericTopologyBuilder builder = new FFNNgenericTopologyBuilder();
		builder.addLayer(3);
		builder.addLayer(3);
		builder.addLayer(1);
		builder.setPrototypeWeight(base);
		builder.initialize();
		topo.setTopologyBuilder(builder);
		topo.initialize();
		
		
		//alternative weights setter------------------------------------
		MixedVector weights = new MixedVector();
		
		weights.add(new Real(0.1));
		weights.add(new Real(0.4));
		weights.add(new Real(0.7));
		
		weights.add(new Real(-0.8));
		weights.add(new Real(-0.1));
		weights.add(new Real(0.30));
		
		weights.add(new Real(0.2));
		weights.add(new Real(-0.9));
		weights.add(new Real(0.6));
		
		topo.setWeights(weights);
				
		//--------------------------------------------------------------
		
		//GenericTopology topo = new GenericTopology(new FFNNStaticTopologyBuilder());
			
		System.out.println("Sigmoid function test ===============================================");
		SigmoidOutputFunction s = new SigmoidOutputFunction();
		System.out.println("raw output for  0    = " + s.computeFunction(new Real(0)));
		System.out.println("raw output for  0.5  = " + s.computeFunction(new Real(0.5)));
		System.out.println("raw output for  1    = " + s.computeFunction(new Real(1)));
		System.out.println("raw output for  -0.5 = " + s.computeFunction(new Real(-0.5)));
		System.out.println("raw output for  -1   = " + s.computeFunction(new Real(-1)));
		System.out.println("raw output for  10   = " + s.computeFunction(new Real(10)));
		System.out.println("raw output for  -10  = " + s.computeFunction(new Real(-10)));
		
		//TestNeuronAndEvaluateFFNN test = new TestNeuronAndEvaluateFFNN();
		
		
		
		
		//NeuronPipeline neuron = new DotProductSigmoidPipeline(new SigmoidOutputFunction(1));
		
		MixedVector ins = new MixedVector();
		ins.add(new Real(-0.765));
		ins.add(new Real(0.112));
		
		MixedVector targt = new MixedVector();
		targt.add(new Real(0.7666));
		NNPattern p = new StandardPattern(ins,targt);
		
		MixedVector result = new MixedVector();
		
		ErrorSignal delta = new SquaredErrorFunction();
		
		FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy();
		trainer.setTopology(topo);
		trainer.setDelta(delta);
		trainer.setMomentum(0.9);
		trainer.setLearningRate(0.5);
		trainer.initialize();
		
		for (int counter = 0; counter < 100; counter++){
			
			result = topo.evaluate(p);			
						
			Double real = ((Real)result.get(0)).getReal();
			
			System.out.println("-----------------------------------------------------");
			System.out.println("The final output result: " + real.toString());
			
			
			
			trainer.invokeTrainer(p);
			
		}
		
		//after training
			 result = topo.evaluate(p);
		
		
		Double real = ((Real)result.get(result.size()-1)).getReal();
		
		
		System.out.println("The final output after training = result: " + real.toString());
		System.out.println("-----------------------------------------------------\n");
		
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		
		FFNNTopology ffnn = new FFNNTopology(2,2,1, 0.5, 0.9);
		
		MixedVector weightsB = new MixedVector();
		weightsB.add(new Real(0.1));
		weightsB.add(new Real(0.4));
		weightsB.add(new Real(0.7));
		
		weightsB.add(new Real(-0.8));
		weightsB.add(new Real(-0.1));
		weightsB.add(new Real(0.3));
		
		weightsB.add(new Real(0.2));
		weightsB.add(new Real(-0.9));
		weightsB.add(new Real(0.6));
		
			ffnn.setWeights(weightsB);
		
		for (int counter = 0; counter < 100; counter++){
			
			result = new MixedVector();
			result = ffnn.evaluate(p);
			real = ((Real)result.get(0)).getReal();
			
			System.out.println("__________________________________________________________");
			System.out.println("FFNNTopology---- output result: " + real.toString());
			
			
			ffnn.train();
			result = ffnn.evaluate(p);
			real = ((Real)result.get(0)).getReal();
						
		}
		
		System.out.println("FFNNTopology final output after training = result: " + real.toString());
		System.out.println("__________________________________________________________\n");
		
		
	}
}
