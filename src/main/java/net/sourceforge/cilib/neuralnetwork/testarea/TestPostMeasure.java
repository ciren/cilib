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
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.GenericData;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.RandomDistributionStrategy;
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
public class TestPostMeasure {

    public TestPostMeasure() {
        this.problemDataFile = null;
        this.postMeasuresFile = null;
        this.layer1size = 0;
        this.layer2size = 0;
        this.layer3size = 0;
        this.perTrain = 0;
        this.perGen = 0;
        this.perVal = 0;
        this.perCan = 0;
        this.learningRate = 0;
        this.momentum = 0;
        this.maxIterations = 0;
        this.dataRandomSeed = 100;
    }

    String problemDataFile, postMeasuresFile;
    int layer1size, layer2size, layer3size;
    int perTrain, perGen, perVal, perCan;
    double learningRate, momentum;
    int maxIterations;
    long dataRandomSeed;

    public void runSimulation(){

        NeuralNetworkProblem neuralNetworkProblem = new NeuralNetworkProblem();
            EvaluationMediator eval = new EvaluationMediator();
            eval.setEpochStrategy(new BatchTrainingSetEpochStrategy());
//            FFNNEvaluationMediator eval = new FFNNEvaluationMediator();
                GenericTopology topo = new LayeredGenericTopology();
                    FFNNgenericTopologyBuilder builder = new FFNNgenericTopologyBuilder();
                        Weight base= new Weight();
                            base.setWeightValue(new Real(0.5));
                            base.setPreviousChange(new Real(0));
                    builder.setPrototypeWeight(base);
                    builder.addLayer(this.layer1size);
                    builder.addLayer(this.layer2size);
                    builder.addLayer(this.layer3size);
                topo.setTopologyBuilder(builder);
                topo.setWeightInitialiser(new FanInWeightInitialiser());
            eval.setTopology(topo);

                GenericData data = new GenericData();
                RandomDistributionStrategy distributor = new RandomDistributionStrategy();
                distributor.setFile(this.problemDataFile);
                distributor.setNoInputs(this.layer1size - 1);
                distributor.setPercentTrain(this.perTrain);
                distributor.setPercentGen(this.perGen);
                distributor.setPercentVal(this.perVal);
                distributor.setPercentCan(this.perCan);
                distributor.setPatternRandomizerSeed(this.dataRandomSeed);
                data.setDistributor(distributor);
            eval.setData(data);

                NNError err = new MSEErrorFunction();
                err.setNoOutputs(this.layer3size);
            eval.addPrototypError(err);

                FFNN_GD_TrainingStrategy trainer = new FFNN_GD_TrainingStrategy();
                trainer.setDelta(new SquaredErrorFunction());
                trainer.setMomentum(this.momentum);
                trainer.setLearningRate(this.learningRate);
            eval.setTrainer(trainer);

        neuralNetworkProblem.setEvaluationStrategy(eval);



        NeuralNetworkController neuralNetworkControl = new NeuralNetworkController();
            neuralNetworkControl.setProblem(neuralNetworkProblem);
            neuralNetworkControl.addStoppingCondition(new MaximumIterations(this.maxIterations));
            PostMeasurementSuite measures = new PostMeasurementSuite();
            measures.setOutputFile(this.postMeasuresFile);

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


        neuralNetworkControl.run();

        data.printStatistics();

    }

    public void setDataRandomSeed(long dataRandomSeed) {
        this.dataRandomSeed = dataRandomSeed;
    }

    public void setLayer1size(int layer1size) {
        this.layer1size = layer1size;
    }

    public void setLayer2size(int layer2size) {
        this.layer2size = layer2size;
    }

    public void setLayer3size(int layer3size) {
        this.layer3size = layer3size;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public void setPerCan(int perCan) {
        this.perCan = perCan;
    }

    public void setPerGen(int perGen) {
        this.perGen = perGen;
    }

    public void setPerTrain(int perTrain) {
        this.perTrain = perTrain;
    }

    public void setPerVal(int perVal) {
        this.perVal = perVal;
    }

    public void setPostMeasuresFile(String postMeasuresFile) {
        this.postMeasuresFile = postMeasuresFile;
    }

    public void setProblemDataFile(String problemDataFile) {
        this.problemDataFile = problemDataFile;
    }







    public static void main(String[] args) {

        TestPostMeasure test = new TestPostMeasure();
        test.setLayer1size(5);
        test.setLayer2size(15);
        test.setLayer3size(3);

        test.setPerCan(0);
        test.setPerTrain(80);
        test.setPerGen(10);
        test.setPerVal(10);

        test.setLearningRate(0.5);
        test.setMomentum(0.9);
        test.setMaxIterations(1000);
        test.setDataRandomSeed(((int) Math.random()*1000));
        test.setProblemDataFile("c:\\masters\\classification\\source\\IrisScaled.txt");
        test.setPostMeasuresFile("c:\\masters\\classification\\irisFSL.txt");
        test.runSimulation();

    }

}
