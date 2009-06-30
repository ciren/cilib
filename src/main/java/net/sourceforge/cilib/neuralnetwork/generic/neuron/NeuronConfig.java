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
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author stefanv
 *
 * This class forms the base for any Neuron in a NN topology.  It forms the extrinsic state to a NeuronPipeline
 * object.  It also keeps track of the Neurons current and previous output and so doing supports recurrent
 * architectures.
 *
 * It forms part of the Generic Framework and should mostly be used "as-is".  In specific cases it can be extended, but an extension may not work
 * for all cases...
 *
 */
public abstract class NeuronConfig {

    //Output array: current output
    Type currentOutput = null;

    //Inputs from other neurons in the net
    NeuronConfig[] input = null;

    //the corresponding weights for input
    Weight[] inputWeights = null;

    //indice of input from NNPattern - negative = not used
    int patternInputPos = -999;

    //The weight used by the pattern input if applicable
    Weight patternWeight = null;

    //Indicates whether to use input[i]'s current output (0) or timestep t-1 output (1).
    //This is used mostly in Recurrent architectures with self connections
    boolean[] timeStepMap = null;

    //timestep t-1 output
    Type tMinus1Output = null;

    //true if the neuron is an output of the network
    //used to indicate which neurons in which layers are also output neurons.  This is
    //because in some topologies like Hopfield nets, neurons are input, output and hidden
    //neurons simultaneously
    boolean isOutputNeuron = false;


    public NeuronConfig() {
        super();
        //default values - Topology builder needs to explicitely change this value for each neuron
        this.input = null;
        this.inputWeights = null;
        this.timeStepMap = null;
        this.patternInputPos = -999;
        this.patternWeight = null;


        currentOutput = null;
        tMinus1Output = null;
    }


    public NeuronConfig(int pipeIndex, Type initValC, Type initValT) {
        //used mostly for neuron types with a fixed output, and no fan-in such as bias units, etc.
        this.input = null;
        this.inputWeights = null;
        this.timeStepMap = null;
        this.patternInputPos = -999;
        this.patternWeight = null;

        currentOutput = initValC;
        tMinus1Output = initValT;
    }


    public NeuronConfig(NeuronConfig[] input,
            Weight[] inputWeights,
            boolean[] timeStepMap,
            int patternInput,
            Weight patternWeight,
            Type initialOutput) {

        //Full constructor, used to fully initialise any neuron

        this.input = input;
        this.inputWeights = inputWeights;
        this.timeStepMap = timeStepMap;
        this.patternInputPos = patternInput;
        this.patternWeight = patternWeight;
        currentOutput = initialOutput;
        tMinus1Output = initialOutput;

        System.out.println("\nNeuron Config: -------------------------");
        if (input != null) System.out.println("input size: " + input.length);
        if (inputWeights != null) System.out.println("input weight size: " + inputWeights.length);
        System.out.println("timestepmap size: " + timeStepMap.length);
        System.out.println("Pattern pos: " + patternInputPos);
        if (patternWeight != null)System.out.println("pattern weight: " + patternWeight.toString());
        //System.out.println("layer: " + layer);
        System.out.println("Current output: " + currentOutput);
    }


    public abstract Type computeOutput(NeuronConfig n, NNPattern p);

    public abstract Type computeOutputFunctionDerivativeAtPos(Type pos);

    public abstract Type computeOutputFunctionDerivativeUsingLastOutput(Type lastOutput);

    public abstract Type computeActivationFunctionDerivativeAtPos(Type pos);

    public abstract Type computeActivationFunctionDerivativeUsingLastOutput(Type lastOutput);




    public Type getCurrentOutput() {
        return currentOutput;
    }

    public NeuronConfig[] getInput() {
        return input;
    }

    public Weight[] getInputWeights() {
        return inputWeights;
    }


    public int getPatternInputPos() {
        return patternInputPos;
    }

    public Weight getPatternWeight() {
        return patternWeight;
    }

    public boolean[] getTimeStepMap() {
        return timeStepMap;
    }

    public Type getTminus1Output() {
        return tMinus1Output;
    }

    public void setCurrentOutput(Type currentOutput) {
        this.currentOutput = currentOutput;
    }

    public void setInput(NeuronConfig[] input) {
        this.input = input;
    }

    public void setInputWeights(Weight[] inputWeights) {
        this.inputWeights = inputWeights;
    }



    public void setPatternInputPos(int patternInput) {
        this.patternInputPos = patternInput;
    }

    public void setPatternWeight(Weight patternWeight) {
        this.patternWeight = patternWeight;
    }

    public void setTimeStepMap(boolean[] timeStepMap) {
        this.timeStepMap = timeStepMap;
    }

    public void setTminus1Output(Type tminus1Output) {
        tMinus1Output = tminus1Output;
    }


    public boolean isOutputNeuron() {
        return isOutputNeuron;
    }

    public void setOutputNeuron(boolean isOutputNeuron) {
        this.isOutputNeuron = isOutputNeuron;
    }
}
