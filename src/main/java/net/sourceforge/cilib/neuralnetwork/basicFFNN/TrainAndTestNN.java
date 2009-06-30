/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.neuralnetwork.basicFFNN;

import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.foundation.epochstrategy.BatchTrainingSetEpochStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.GenericData;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.RandomDistributionStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.errorfunctions.MSEErrorFunction;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author stefanv
 *
 *
 */
public final class TrainAndTestNN {

    private TrainAndTestNN(){
    }

    public static void main(String[] args) {

        NeuralNetworkTopology neuralNetworkTopology = new FFNNTopology(1, 30, 1, 0.5, 1.0);
        TrainingStrategy train = new FFNNTrainingStrategy((FFNNTopology) neuralNetworkTopology);

        // TODO: This entire data related buildng can be done in a DataBasedProblem class!
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
        EvaluationMediator mediator = new EvaluationMediator();
        mediator.setEpochStrategy(new BatchTrainingSetEpochStrategy());
        mediator.setTopology(neuralNetworkTopology);
        mediator.setData(data);
        mediator.addPrototypError(err);
        //eval.addPrototypError(err1);
        mediator.setTrainer(train);
        //    eval.initialize();


        NeuralNetworkProblem neuralNetworkProblem = new NeuralNetworkProblem();
        neuralNetworkProblem.setEvaluationStrategy(mediator);
        //    NNprob.initialize();

        NeuralNetworkController neuralNetworkController = new NeuralNetworkController();
        neuralNetworkController.setProblem(neuralNetworkProblem);

        neuralNetworkController.addStoppingCondition(new MaximumIterations(5000));

        System.out.println("Configuration completed...");
        //-----------------------------------------------------------------------------------------------------------

        neuralNetworkController.initialise();
        //needed by Algorithm...

        neuralNetworkController.run();
        //run Algorithm


        //create a pattern manually and test output
        Vector in = new Vector();

        in.add(new Real(1)); in.add(new Real(1));

        StandardPattern p = new StandardPattern(in, null);

        TypeList result = neuralNetworkTopology.evaluate(p);

        //output line - add any status here that reports on the manual pattern entered.
        System.out.println("test result: 1 and 1 = 0.6    -->  " + ((Real) result.get(0)).getReal());

        System.out.println("data stats:\n\n");

        System.out.println("candidate set size      : " + data.getCandidateSetSize());
        System.out.println("training set size       : " + data.getTrainingSetSize());
        System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
        System.out.println("validation set size     : " + data.getValidationSetSize());



    }
}
