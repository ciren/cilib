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
package net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.LayerIterator;
import net.sourceforge.cilib.neuralnetwork.generic.Observer;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * @author stefanv
 *
 * This is an Application in the Generic Framework - it does not form part of the framework.
 * It implements multi layer Gradient Decent (GD) training for Feedforward NN (FFNN).
 *
 * To use the class, call the invokeTrainer() method with the last presented
 * pattern as an argument.
 */
public class FFNN_GD_TrainingStrategy implements TrainingStrategy, Observer {

    GenericTopology topology = null;
    ErrorSignal delta = null;
    Double learningRate = null;
    Double momentum = null;


    public FFNN_GD_TrainingStrategy() {
        this.topology = null;
        this.delta = null;
        this.learningRate = null;
        this.momentum = null;
    }


    public void initialize(){
        if (this.topology == null){
            throw new IllegalArgumentException("FFNN_GD_TrainingStrategy: Required topology object was null during initialization");
        }
        if (this.delta == null){
            throw new IllegalArgumentException("FFNN_GD_TrainingStrategy: Required delta object was null during initialization");
        }

        if (this.learningRate == null){
            throw new IllegalArgumentException("FFNN_GD_TrainingStrategy: Required learningRate object was null during initialization");
        }

        if (this.momentum == null){
            throw new IllegalArgumentException("FFNN_GD_TrainingStrategy: Required momentum object was null during initialization");
        }

    }



    public void updateFanInWeights(NeuronConfig n, Type delta){
        //Wi(t) = Wi(t) + learnRate * delta * input_i    +    momentum * change_Wi(t-1)

        //for the case where neuron has neuron-to-neuron inputs
        Weight[] w = n.getInputWeights();
        Double temp = -1 * learningRate * ((Real) delta).getReal();

        for (int i = 0; i < w.length; i++){

            Double change = temp * ((Real) n.getInput()[i].getCurrentOutput()).getReal();

            w[i].setWeightValue(new Real(((Real) w[i].getWeightValue()).getReal() +
                                 change +
                                 (this.momentum * ((Real) w[i].getPreviousChange()).getReal())));

            w[i].setPreviousChange(new Real(change));
        }

        //for the case where the neuron has pattern-to-neuron inputs
        //should never happen as these lie in layer 0, which is never evaluated
        if (n.getPatternWeight() != null){
            throw new IllegalStateException("Topology is not compatible with Gradient Decent - " +
                                        "pattern input neuron has other inputs - thus recurrent");
        }
    }



    public void invokeTrainer(Object args) {

        NNPattern p = (NNPattern) args;
        ArrayList<NeuronConfig> updateNeuronList = new ArrayList<NeuronConfig>();
        TypeList deltaUpdateList = new TypeList();

        //iterate over output layer
        LayerIterator outputIter = topology.getLayerIterator(topology.getNrLayers() - 1);
        TypeList prevDeltaList = new TypeList(outputIter.getNrNeurons());
        LayerIterator prevIter = null;

        for (NeuronConfig oi = outputIter.value(); outputIter.hasMore(); outputIter.nextNeuron()){
            oi = outputIter.value();

            //for each output, find deltaOi definition using
            //error, output and activation derivative
            Type tmpO = delta.computeBaseDelta(p.getTarget().get(outputIter.currentPosition()),
                                                   oi.getCurrentOutput(),
                                                   oi.computeOutputFunctionDerivativeUsingLastOutput(oi.getCurrentOutput()));

            prevDeltaList.add(tmpO);
            updateNeuronList.add(oi);
            deltaUpdateList.add(tmpO);
        } //end for Oi

        prevIter = outputIter;
        //-----------------------------------------------------------------------------

        for (int layer = topology.getNrLayers() - 2; layer > 0; layer--){
        //Iterate backwards over all remaining layers L = #nrlayers-1 ... 1.  layer 0 not done as input layer

            LayerIterator layerIter = topology.getLayerIterator(layer);
            TypeList deltaList = new TypeList(layerIter.getNrNeurons());

            //iterate over all neurons in layer L
            for (NeuronConfig li = layerIter.value(); layerIter.hasMore(); layerIter.nextNeuron()){

                li = layerIter.value();

                //Compute deltaLi for neuron i in layer L, taking recursive
                //definition into account.
                //Only applicable to neurons that backprop (i.e. not Bias unit).
                //Thus if Li has fanin weights array with length > 1 then perform backprop

                if (li.getInputWeights() != null){

                    //Weights out of current layer neuron to layer + 1 neurons
                    ArrayList<Weight> w = new ArrayList<Weight>();
                    prevIter.reset();

                    for (NeuronConfig wi = prevIter.value(); prevIter.hasMore(); prevIter.nextNeuron()){
                        w.add(wi.getInputWeights()[layerIter.currentPosition()]);
                        //TODO: Fix this up - using static indexing, make dynamic looking for Li in Wi inputlist
                        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5
                    }

                    Type tmpL = delta.computeRecursiveDelta(
                            li.computeOutputFunctionDerivativeUsingLastOutput(li.getCurrentOutput()),
                            prevDeltaList,
                            w,
                            li.getCurrentOutput());

                    deltaList.add(tmpL);


                    updateNeuronList.add(li);
                    deltaUpdateList.add(tmpL);

                } //ebd if


            } //end for Li

            prevIter = layerIter;

        } //end for layer

        //update weights for each output neuron using deltaOi
        for (int count = 0; count < updateNeuronList.size(); count++){

                this.updateFanInWeights(updateNeuronList.get(count), deltaUpdateList.get(count));

        }

    }


    public void validate(){
        //Follows the Observer design pattern.  Any change in GenericTopology should call notify, which
        //calls all Observers' validate() method, including this class's.
        //This method should perform a full check on the topology to see if it is still compatible with
        //this trainer.  If not an exception should be thrown.



        //check layering right

        //check no pattern inputs anywere else than in layer 0
    }


    public void preEpochActions(Object args) {


    }

    public void postEpochActions(Object args) {


    }

    public void setDelta(ErrorSignal delta) {
        this.delta = delta;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }

    public void setMomentum(Double momentum) {
        this.momentum = momentum;
    }


    public void setTopology(NeuralNetworkTopology topo) {

        if (!(topo instanceof GenericTopology)){
            throw new IllegalArgumentException("Topology Argument not of type GenericTopology");
        }
        this.topology = (GenericTopology) topo;
    }



}
