/*
 * Created on 2004/12/29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.basicFFNN;

import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.GenericData;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.RandomDistributionStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.errorfunctions.MSEErrorFunction;
import net.sourceforge.cilib.neuralnetwork.generic.evaluationmediators.FFNNEvaluationMediator;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author stefanv
 * 
 * 
 */
public class TrainAndTestNN {
	
	public static void main(String[] args) {
		
		NeuralNetworkTopology NNtopology = new FFNNTopology(1, 30, 1, 0.5, 1.0);
		TrainingStrategy train = new FFNNTrainingStrategy((FFNNTopology)NNtopology);
		
		GenericData data = null;
		
		
		data = new GenericData();
		RandomDistributionStrategy distributor = new RandomDistributionStrategy();
		distributor.setFile("c:\\temp\\data\\tester.txt");
		distributor.setNoInputs(1);
		distributor.setPercentTrain(70);
		distributor.setPercentGen(1);
		distributor.setPercentVal(0);
		distributor.setPercentCan(29);
		data.setDistributor(distributor);
		
		data.initialize();
		
		
		
		
		NNError err = new MSEErrorFunction();
		err.setNoOutputs(1);
		err.setNoPatterns(data.getTrainingSetSize());
		//NNError err1 = new ClassificationErrorReal();
		
		//use the Generic Package's FFNNEvaluationMediator.
		FFNNEvaluationMediator eval = new FFNNEvaluationMediator();
		eval.setTopology(NNtopology); 
		eval.setData(data);
		eval.addPrototypError(err);
		//eval.addPrototypError(err1);
		eval.setTrainer(train);
		//	eval.initialize();
		
		
		NeuralNetworkProblem NNprob = new NeuralNetworkProblem();
		NNprob.setEvaluationStrategy(eval);
		//	NNprob.initialize();
		
		NeuralNetworkController NNControl = new NeuralNetworkController();
		NNControl.setProblem(NNprob);
		
		NNControl.addStoppingCondition(new MaximumIterations(5000));
		
		System.out.println("Configuration completed...");
		//-----------------------------------------------------------------------------------------------------------
		
		NNControl.initialise();
		//needed by Algorithm...
		
		NNControl.run();
		//run Algorithm
		
		
		//create a pattern manually and test output
		MixedVector in = new MixedVector();
		
		in.add(new Real(1)); in.add(new Real(1));
		
		StandardPattern p = new StandardPattern(in, null);
		
		MixedVector result = NNtopology.evaluate(p);
		
		//output line - add any status here that reports on the manual pattern entered.
		System.out.println("test result: 1 and 1 = 0.6    -->  " + ((Real)result.get(0)).getReal());
		
		System.out.println("data stats:\n\n");
		
		System.out.println("candidate set size      : " + data.getCandidateSetSize());
		System.out.println("training set size       : " + data.getTrainingSetSize());
		System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
		System.out.println("validation set size     : " + data.getValidationSetSize());
		
		
		
	}
}
