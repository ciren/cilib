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
package net.sourceforge.cilib.neuralnetwork.foundation;


import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.neuralnetwork.foundation.epochstrategy.EmptyEpochStrategy;
import net.sourceforge.cilib.neuralnetwork.foundation.epochstrategy.EpochStrategy;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.TypeList;



/**
 * @author stefanv
 *
 */
public class EvaluationMediator extends AbstractAlgorithm implements SingularAlgorithm {
    private static final long serialVersionUID = 1291326641087190123L;
    protected NNError[] prototypeError = null;
    protected NNError[] errorDg = null;
    protected NNError[] errorDt = null;
    protected NNError[] errorDv = null;
    protected int nrEvaluationsPerEpoch;

    protected NeuralNetworkTopology topology = null;
    protected NeuralNetworkData data = null;
    protected TrainingStrategy trainer = null;

    protected int totalEvaluations;

    private EpochStrategy epochStrategy;

    public EvaluationMediator() {
        nrEvaluationsPerEpoch = 0;
        totalEvaluations = 0;

        this.epochStrategy = new EmptyEpochStrategy();
    }

    public void performInitialisation(){

        if (this.data == null)  {
            throw new IllegalArgumentException("Evaluation Strategy error: required data object was null");
        }
        if (this.topology == null)  {
            throw new IllegalArgumentException("Evaluation Strategy error: required topology object was null");
        }
        if (this.prototypeError == null)  {
            throw new IllegalArgumentException("Evaluation Strategy error: required prototypeError object was null");
        }
//        if (this.trainer == null)  {
//            throw new IllegalArgumentException("Evaluation Strategy error: required trainer object was null");
//        }

        //Initialize objects, depth first via Chain of Responsibility pattern.
        data.initialize();
        topology.initialize();
        if (trainer != null) {
            trainer.setTopology(this.topology);
            trainer.initialize();
        }


        this.errorDg = new NNError[prototypeError.length];
        this.errorDt = new NNError[prototypeError.length];
        this.errorDv = new NNError[prototypeError.length];

        for (int i=0; i < prototypeError.length; i++){

            this.errorDg[i] = prototypeError[i].getClone();
            this.errorDg[i].setNoPatterns(this.data.getGeneralisationSetSize());
            this.errorDt[i] = prototypeError[i].getClone();
            this.errorDt[i].setNoPatterns(this.data.getTrainingSetSize());
            this.errorDv[i] = prototypeError[i].getClone();
            this.errorDv[i].setNoPatterns(this.data.getValidationSetSize());
        }
    }

    @Override
    public void algorithmIteration() {
        this.epochStrategy.performIteration(this);
    }

    public void computeErrorIteration(NNError[] err, TypeList output, NNPattern input){

        for (int e = 0; e < err.length; e++){
            err[e].computeIteration(output, input);
        }

    }

    // This might not be needed in this class.
    public TypeList evaluate(NNPattern p) {
        return topology.evaluate(p);
    }
//    public abstract Vector evaluate(NNPattern p);


    public NNError[] getErrorDg() {
        return errorDg;
    }


    public NNError[] getErrorDt() {
        return errorDt;
    }



    public NNError[] getErrorDv() {
        return errorDv;
    }



    public int getNrEvaluationsPerEpoch() {
        return nrEvaluationsPerEpoch;
    }



    public NeuralNetworkTopology getTopology() {
        return topology;
    }


    public int getTotalEvaluations() {
        return totalEvaluations;
    }


//    protected abstract void learningEpoch();


    public void performLearning(){
//        learningEpoch();
        algorithmIteration();
        totalEvaluations += nrEvaluationsPerEpoch;
        nrEvaluationsPerEpoch = 0;

    }
    public void finaliseErrors(NNError[] err){
        for (int e = 0; e < err.length; e++){
            err[e].finaliseError();
        }
    }
    public void resetError(NNError[] err){
        for (int e = 0; e < err.length; e++){
            err[e] = prototypeError[e].getClone();
        }
    }

    public void setErrorDg(NNError[] errorDg) {
        throw new UnsupportedOperationException("Class member cannot be set directly - please use reset() method");
    }


    public void setErrorDt(NNError[] errorDt) {
        throw new UnsupportedOperationException("Class member cannot be set directly - please use reset() method");
    }


    public void setErrorDv(NNError[] errorDv) {
        throw new UnsupportedOperationException("Class member cannot be set directly - please use reset() method");
    }


    public void setErrorNoOutputs(NNError[] err, int nr){
        for (int e = 0; e < err.length; e++){
            err[e].setNoOutputs(nr);
        }
    }

    public void setErrorNoPatterns(NNError[] err, int nr){
        for (int e = 0; e < err.length; e++){
            err[e].setNoPatterns(nr);
        }
    }


    public void setTopology(NeuralNetworkTopology topology) {
        this.topology = topology;
    }

    public NNError[] getPrototypeError() {
        return prototypeError;
    }

    public void addPrototypError(NNError proto){

        if (this.prototypeError == null){
            this.prototypeError = new NNError[1];
            this.prototypeError[0] = proto.getClone();
        }
        else {
            NNError[] tmp = new NNError[this.prototypeError.length + 1];
            for (int i=0; i < this.prototypeError.length; i++){
                tmp[i] = this.prototypeError[i].getClone();
            }
            tmp[this.prototypeError.length] = proto.getClone();
            this.prototypeError = tmp;
        }
    }

    public NeuralNetworkData getData() {
        return data;
    }

    public void setData(NeuralNetworkData data) {
        this.data = data;
    }

    public TrainingStrategy getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainingStrategy trainer) {
        this.trainer = trainer;
    }

    public EpochStrategy getEpochStrategy() {
        return epochStrategy;
    }

    public void setEpochStrategy(EpochStrategy epochStrategy) {
        this.epochStrategy = epochStrategy;
    }


    @Override
    public OptimisationSolution getBestSolution() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EvaluationMediator getClone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OptimisationSolution> getSolutions() {
        // TODO Auto-generated method stub
        return null;
    }

    public void incrementEvaluationsPerEpoch() {
        this.nrEvaluationsPerEpoch++;
    }

}
