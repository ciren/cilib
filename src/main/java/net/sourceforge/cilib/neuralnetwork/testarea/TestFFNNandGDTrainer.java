/*
 * Created on 2005/04/23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.LayeredGenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.GenericData;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.RandomDistributionStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.errorfunctions.ClassificationErrorReal;
import net.sourceforge.cilib.neuralnetwork.generic.errorfunctions.MSEErrorFunction;
import net.sourceforge.cilib.neuralnetwork.generic.evaluationmediators.FFNNEvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.FFNNgenericTopologyBuilder;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.FFNN_GD_TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.SquaredErrorFunction;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestFFNNandGDTrainer {

	
	
	public static void main(String[] args) {
		
				
		Weight base= new Weight(new Real(0.5));
				
		FFNNgenericTopologyBuilder builder = new FFNNgenericTopologyBuilder();
		builder.setPrototypeWeight(base);
		builder.addLayer(2);
		builder.addLayer(30);
		builder.addLayer(1);
		//builder.initialize();
		
	//	GenericTopology topo = new GenericTopology();
		GenericTopology topo = new LayeredGenericTopology();
		topo.setTopologyBuilder(builder);
		//topo.initialize();
						
		FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy();
		trainer.setDelta(new SquaredErrorFunction());
		trainer.setTopology(topo);
		trainer.setMomentum(0.9);
		trainer.setLearningRate(0.5);
		//trainer.initialize();
		
		GenericData data = null;
		
		
			data = new GenericData();
			RandomDistributionStrategy distributor = new RandomDistributionStrategy();
			//distributor.setFile("d:\\Stefan University\\masters\\datasets\\F2.txt");
			distributor.setFile("c:\\temp\\data\\tester.txt");
			distributor.setNoInputs(1);
			distributor.setPercentTrain(70);
			distributor.setPercentGen(1);
			distributor.setPercentVal(0);
			distributor.setPercentCan(29);
			data.setDistributor(distributor);
			//data.initialize();
			
			//data.populate();
		
		
		
		NNError err = new MSEErrorFunction();
			err.setNoOutputs(1);
		NNError	err1 = new ClassificationErrorReal();
		
		
		FFNNEvaluationMediator eval = new FFNNEvaluationMediator();
		eval.setTopology(topo);
		eval.setData(data);
		eval.addPrototypError(err);
		eval.addPrototypError(err1);
		eval.setTrainer(trainer);
		//eval.initialize();		
		
		
		NeuralNetworkProblem NNprob = new NeuralNetworkProblem();
		NNprob.setEvaluationStrategy(eval);
		//NNprob.initialize();
		
		NeuralNetworkController NNControl = new NeuralNetworkController();
		NNControl.setProblem(NNprob);
		
		
		NNControl.addStoppingCondition(new MaximumIterations(5000));
		
		System.out.println("Configuration completed...");
//		-----------------------------------------------------------------------------------------------------------

NNControl.initialise();
//needed

System.out.println("About to run simulation...");	

NNControl.run();
////run die stuff
		
	

Vector in = new Vector(1);

in.add(new Real(0.5)); 
in.add(new Real(1.234));

StandardPattern p = new StandardPattern(in, null);

Vector result = topo.evaluate(p);

System.out.println("test result input = 0.5, output should be 0.25  -->  : " + ((Real)result.get(0)).getReal());

System.out.println("data stats:\n\n");

System.out.println("candidate set size      : " + data.getCandidateSetSize());
System.out.println("training set size       : " + data.getTrainingSetSize());
System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
System.out.println("validation set size     : " + data.getValidationSetSize());


		
		
		
		
		
		
		
		
		
		
	}
}
