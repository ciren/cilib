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
package net.sourceforge.cilib.neuralnetwork.testarea;

import net.sourceforge.cilib.measurement.generic.Time;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.foundation.epochstrategy.BatchTrainingSetEpochStrategy;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.AreaUnderROC;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.DcPatternCount;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.DgPatternCount;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.DtPatternCount;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.DvPatternCount;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.ErrorDg;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.ErrorDt;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.ErrorDv;
import net.sourceforge.cilib.neuralnetwork.foundation.measurements.RobelOverfittingRho;
import net.sourceforge.cilib.neuralnetwork.foundation.postSimulation.PostMeasurementSuite;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.LayeredGenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.CrossValidationStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.GenericData;
import net.sourceforge.cilib.neuralnetwork.generic.errorfunctions.ClassificationErrorReal;
import net.sourceforge.cilib.neuralnetwork.generic.errorfunctions.MSEErrorFunction;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.FFNNgenericTopologyBuilder;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.FanInWeightInitialiser;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.FFNN_GD_TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies.SquaredErrorFunction;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.types.Real;

/**
 * TODO: Complete this javadoc.
 */
public final class XMLmimic {

    private XMLmimic() {
    }

    public static void run(){

        NeuralNetworkProblem neuralNetworkProblem = new NeuralNetworkProblem();
        EvaluationMediator eval = new EvaluationMediator();
        eval.setEpochStrategy(new BatchTrainingSetEpochStrategy());
//            FFNNEvaluationMediator eval = new FFNNEvaluationMediator();
            //SAILAEvaluationStrategy eval = new SAILAEvaluationStrategy();

                //GenericTopology topo = new GenericTopology();
                GenericTopology topo = new LayeredGenericTopology();
                    FFNNgenericTopologyBuilder builder = new FFNNgenericTopologyBuilder();
                        Weight base= new Weight();
                            base.setWeightValue(new Real(0.5));
                            base.setPreviousChange(new Real(0));
                    builder.setPrototypeWeight(base);
                    builder.addLayer(5);
                    builder.addLayer(15);
                    builder.addLayer(3);
                topo.setTopologyBuilder(builder);
                topo.setWeightInitialiser(new FanInWeightInitialiser());
            eval.setTopology(topo);

                GenericData data = new GenericData();
                //RandomDistributionStrategy distrib = new RandomDistributionStrategy();
                CrossValidationStrategy distrib = new CrossValidationStrategy();
                distrib.setK(15);
                distrib.setKoffset(1);
                //SAILARealData data = new SAILARealData();
                //data.setTopology(topo);
            //    data.setFile("d:\\Stefan University\\masters\\datasets\\F2.txt");
                distrib.setFile("C:\\temp\\data\\IrisScaled.txt");
                distrib.setNoInputs(4);
                distrib.setPercentTrain(95);
                distrib.setPercentGen(5);
            //    distrib.setPercentVal(15);
                distrib.setPercentCan(0);
            //    distrib.setPatternRandomizerSeed(200);
                data.setDistributor(distrib);
            eval.setData(data);

                NNError err = new MSEErrorFunction();
                err.setNoOutputs(3);
                //err.setNoPatterns(1000);        //direct settings wont work in XML
                NNError err1 = new ClassificationErrorReal();
            eval.addPrototypError(err);
            eval.addPrototypError(err1);

                FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy();
                trainer.setDelta(new SquaredErrorFunction());
                trainer.setMomentum(0.5);
                trainer.setLearningRate(0.9);
            eval.setTrainer(trainer);

        neuralNetworkProblem.setEvaluationStrategy(eval);



        NeuralNetworkController neuralNetworkControl = new NeuralNetworkController();
            neuralNetworkControl.setProblem(neuralNetworkProblem);
            neuralNetworkControl.addStoppingCondition(new MaximumIterations(1000));
            PostMeasurementSuite measures = new PostMeasurementSuite();
            measures.setOutputFile("c:\\temp\\data\\dom.txt");
            AreaUnderROC auc = new AreaUnderROC();
            auc.setData(data);
            auc.setTopology(topo);
            measures.addMeasurement(new ErrorDt(eval));
            measures.addMeasurement(new ErrorDg(eval));
            measures.addMeasurement(new ErrorDv(eval));
            measures.addMeasurement(auc);
            measures.addMeasurement(new DcPatternCount());
            measures.addMeasurement(new DtPatternCount());
            measures.addMeasurement(new DgPatternCount());
            measures.addMeasurement(new DvPatternCount());
            measures.addMeasurement(new RobelOverfittingRho());
            measures.addMeasurement(new Time());
            neuralNetworkControl.setMeasures(measures);

        neuralNetworkControl.initialise();







        System.out.println("Configuration completed...");
//        -----------------------------------------------------------------------------------------------------------


System.out.println("About to run simulation...");

neuralNetworkControl.run();
////run the stuff


/*
MixedVector in = new MixedVector(1);

in.add(new Real(0.5));
in.add(new Real(1.234));

StandardPattern p = new StandardPattern(in, null);

MixedVector result = topo.evaluate(p);

System.out.println("test result input = 0.5, output should be 0.25  -->  : " + ((Real)result.get(0)).getReal());
*/
System.out.println("data stats:\n\n");

System.out.println("candidate set size      : " + data.getCandidateSetSize());
System.out.println("training set size       : " + data.getTrainingSetSize());
System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
System.out.println("validation set size     : " + data.getValidationSetSize());



    }

    public static void main(String [] args){

        XMLmimic.run();
        //XMLmimic.run();
        //XMLmimic.run();
    }

}
