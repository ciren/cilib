/*
 * NN.java
 *
 * Created on June 24, 2003, 21:00 PM
 *
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *
 */

package net.sourceforge.cilib.NeuralNetwork;

public class NN {
    /**
     * Size of input layer. Must contain the bias unit as well.
     */
    private int sizeIL;
    /**
     * Size of hidden layer. Must contain the bias unit as well.
     */
    private int sizeHL;
    /**
     * Size of output layer. The output layer does not contain a bias.
     */
    private int sizeOL;
    /**
     * The output from the hidden layer.
     */
    private double hiddenOutput[];

    public NN() {
    }

    public NN(int sizeIL, int sizeHL, int sizeOL) {
        if (sizeIL < 2) {
            throw new RuntimeException("size of the input layer must be greater than 1");
        }
        if (sizeHL < 2) {
            throw new RuntimeException("size of the hidden layer must be greater than 1");
        }
        if (sizeOL < 1) {
            throw new RuntimeException("size of the output layer must be greater than 0");
        }

        this.sizeIL = sizeIL;
        this.sizeHL = sizeHL;
        this.sizeOL = sizeOL;
        hiddenOutput = new double[sizeHL];
    }

    /**
     * Calculate the output of the NN.
     * @param input_value The input values to the NN.  The input of the NN will
     * be normalised in a range of [-Math.sqrt(3), Math.sqrt(3)].
     * @param wieght The weights of the NN.  To ensure that this is possible the
     * input values must be in the range [0, 1].
     * @param output The output of the NN will be stored in this array.
     */
    public void getOutput(
        double input_value[],
        double wieght[],
        double output[]) {
        double[] input = new double[input_value.length];
        System.arraycopy(input_value, 0, input, 0, input_value.length);

        // normalise the input.
        for (int i = 0; i < input.length; i++) {
            input[i] = input[i] * Math.sqrt(3) - 2.0 * Math.sqrt(3);
        }

        //The number of inputs to the hidden neuron is SizeIL.
        Neuron hidden_neuron = new Neuron(sizeIL);

        //calculate the output for each hidden neuron.
        for (int i = 0; i < sizeHL; i++) {
            hiddenOutput[i] =
                hidden_neuron.getOutput(
                    input,
                    wieght,
                    i * sizeIL,
                    (i + 1) * sizeIL);
        }
        hiddenOutput[sizeHL - 1] = -1;

        Neuron output_neuron = new Neuron(sizeHL);

        //calculate the output for each output neuron.
        for (int i = 0; i < sizeOL; i++) {
            output[i] =
                output_neuron.getOutput(
                    hiddenOutput,
                    wieght,
                    sizeIL * (sizeHL - 1) + i * sizeHL,
                    sizeIL * (sizeHL - 1) + (i + 1) * sizeHL);
        }
    }

    /**
     * Get the output from the Hidden Layer.  This should only be called once the
     * output to the NN has been calculated.
     * @param hiddenOutput The output of the NN's HL is stored in this array.
     */
    public void getHLOutput(double hiddenOutput[]) {
        for (int i = 0; i < sizeHL; i++) {
            hiddenOutput[i] = this.hiddenOutput[i];
        }
    }

    public void setSizeHL(int sizeHL) {
        hiddenOutput = new double[sizeHL];
        this.sizeHL = sizeHL;
    }

    public int getSizeHL() {
        return sizeHL;
    }

    public void setSizeIL(int sizeIL) {
        this.sizeIL = sizeIL;
    }

    public int getSizeIL() {
        return sizeIL;
    }

    public void setSizeOL(int sizeOL) {
        this.sizeOL = sizeOL;
    }

    public int getSizeOL() {
        return sizeOL;
    }

    public int getNumberOfWeights() {
        return getSizeIL() * getSizeHL() + getSizeHL() * getSizeOL();
    }
}