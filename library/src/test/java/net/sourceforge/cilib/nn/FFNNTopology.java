/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn;

import java.util.ArrayList;

import net.sourceforge.cilib.nn.architecture.NeuralInputSource;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is a basic Feedforward Neural network implementation. Its sole purpose is to illustrate the
 * working of the CIlib Neural network framework and to show how it can be used to create a custom
 * NN.
 */
public class FFNNTopology {

    protected double[] weights = null;
    int nrInputBiased; // includes bias unit, thus true input = nrInput - 1
    int nrHiddenBiased; // includes bias unit, thus true hidden = nrHidden - 1
    int nrOutput;
    int nrWeights; // nr of weights in the NN
    double[] hiddenResult;
    double[] outputResult;
    ArrayList<Double> output = null;
    NeuralInputSource lastPattern = null;
    private double learnRateEta;
    private double momentumAlpha;
    private double[] oldWeightChangesHO;
    private double[] oldWeightChangesIH;
    private double newWeightChanges;

    public FFNNTopology(int inputNr, int hiddenNr, int outputNr, double learn, double moment) {
        learnRateEta = learn;
        momentumAlpha = moment;

        nrWeights = ((inputNr + 1) * hiddenNr); // for hidden layer bias unit
        nrWeights += ((hiddenNr + 1) * outputNr); // for output units connected
        // thus bias units weights are included too

        nrInputBiased = inputNr + 1;
        nrHiddenBiased = hiddenNr + 1;
        nrOutput = outputNr;

        weights = new double[nrWeights];

        for (int i = 0; i < nrInputBiased * hiddenNr; i++) {
            weights[i] = 2.0 * Math.random() * (1.0 / Math.sqrt(nrInputBiased)) - (1.0 / Math.sqrt(nrInputBiased));
        }

        for (int i = nrInputBiased * hiddenNr; i < nrWeights; i++) {
            weights[i] = 2.0 * Math.random() * (1.0 / Math.sqrt(nrHiddenBiased)) - (1.0 / Math.sqrt(nrHiddenBiased));
        }

        hiddenResult = new double[nrHiddenBiased]; // no bias unit input
        // keep results from dot product of I-H layers
        for (int i = 0; i < nrHiddenBiased; i++) {
            hiddenResult[i] = 0.0;
        }

        outputResult = new double[nrOutput]; // no bias unit input
        // keep results from dot product of H-O layers
        for (int i = 0; i < nrOutput; i++) {
            outputResult[i] = 0.0;
        }

        oldWeightChangesHO = new double[nrHiddenBiased * nrOutput];
        for (int i = 0; i < nrHiddenBiased * nrOutput; i++) {
            oldWeightChangesHO[i] = 0.0;
        }

        oldWeightChangesIH = new double[hiddenNr * nrInputBiased];
        for (int i = 0; i < hiddenNr * nrInputBiased; i++) {
            oldWeightChangesIH[i] = 0.0;
        }

        newWeightChanges = 0;
    }

    /*
     * (non-Javadoc)
     * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkTopology#evaluate(net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern)
     */
    public TypeList evaluate(NeuralInputSource p) {
        // Still need to check here for right length vector then exception
        // Does not delete the activation of hidden or output units, used to feed back...
        // zeroes activation before doing a feedforward though...

        if (p.size() != this.nrInputBiased - 1) {
            throw new IllegalArgumentException("Input in Pattern does not match topology input number: pattern = " + p.size() + ", topology = " + (this.nrInputBiased - 1));
        }

        for (int i = 0; i < (nrHiddenBiased); i++) {
            hiddenResult[i] = 0.0;
        }
        hiddenResult[nrHiddenBiased - 1] = -1.0; // Initialise bias unit

        for (int i = 0; i < (nrOutput); i++) {
            outputResult[i] = 0.0;
        }
        // ---------------------------------------------------------------------------------

        for (int j = 0; j < (nrHiddenBiased - 1); j++) {
            for (int i = 0; i < nrInputBiased; i++) {
                if (i == (nrInputBiased - 1)) {// Bias unit
                    hiddenResult[j] += -1.0 * weights[j * nrInputBiased + i];
                } else {
                    double in = p.getNeuralInput(i);
                    hiddenResult[j] += weights[j * nrInputBiased + i] * in;
                }
            }
            // Sigmoid activation
            hiddenResult[j] = 1.0 / (1.0 + Math.exp(-1.0 * hiddenResult[j]));
        }

        // ========================================================================================
        // output neurons:
        int start = nrInputBiased * (nrHiddenBiased - 1);
        for (int k = 0; k < nrOutput; k++) {
            for (int j = 0; j < nrHiddenBiased; j++) {
                outputResult[k] += weights[start + k * nrHiddenBiased + j] * (hiddenResult[j]);
            }
            // Sigmoid activation
            outputResult[k] = 1.0 / (1.0 + Math.exp(-1.0 * outputResult[k]));
        }

        // convert to ArrayList...
        TypeList temp = new TypeList();
        output = new ArrayList<Double>();
        for (int i = 0; i < nrOutput; i++) {
            temp.add(Real.valueOf(outputResult[i]));
            output.add(Double.valueOf(outputResult[i]));
        }

        lastPattern = p;
        return temp;
    }

    /*
     * (non-Javadoc)
     * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkTopology#getWeights()
     */
    public Vector getWeights() {
        Vector.Builder temp = Vector.newBuilder();
        for (int i = 0; i < nrWeights; i++) {
            temp.add(Real.valueOf(weights[i]));
        }
        return temp.build();
    }

    /*
     * (non-Javadoc)
     * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkTopology#setWeights(java.lang.Object)
     */
    public void setWeights(Vector w) {
        if (w.size() != this.weights.length) {
            throw new IllegalArgumentException("FFNNTopology: Weight Vector lenghts do not match - " + "FFNN vector size = " + weights.length + ", input = " + w.size());
        }
        for (int i = 0; i < nrWeights; i++) {
            weights[i] = w.doubleValueOf(i);
        }
    }

    public void train() {
        // Take one patern and feedforward. Use result to determine error and
        // adjust weights accordingly...

        double[] errorSignalOutput = new double[nrOutput];
        double[] errorSignalHidden = new double[nrHiddenBiased - 1];

        // Error signal for output units.
        for (int k = 0; k < nrOutput; k++) {
            double t_k = nrOutput == 1 ? ((Real) ((PatternInputSource) lastPattern).getTarget()).doubleValue() : ((Vector) ((PatternInputSource) lastPattern).getTarget()).doubleValueOf(k);
            double o_k = output.get(k);
            errorSignalOutput[k] = -1.0 * (t_k - o_k) * (1.0 - o_k) * o_k;
        }

        // Error signal for hidden units.
        int start = nrInputBiased * (nrHiddenBiased - 1);
        for (int j = 0; j < nrHiddenBiased - 1; j++) {
            errorSignalHidden[j] = 0.0;
            for (int k = 0; k < nrOutput; k++) {
                double w_kj = weights[start + j + k * nrHiddenBiased];
                errorSignalHidden[j] += errorSignalOutput[k] * w_kj;
            }
            double y_j = hiddenResult[j];
            errorSignalHidden[j] *= (1.0 - y_j) * y_j;
        }

        // Weight Changes...
        // =========================================================
        for (int k = 0; k < nrOutput; k++) {
            double temp = (-1.0 * learnRateEta) * errorSignalOutput[k];
            for (int j = 0; j < nrHiddenBiased; j++) {
                int kj = k * nrHiddenBiased + j;
                newWeightChanges = temp * hiddenResult[j];
                weights[start + kj] += newWeightChanges + momentumAlpha * oldWeightChangesHO[kj];
                oldWeightChangesHO[kj] = newWeightChanges;
            }
        }

        for (int j = 0; j < (nrHiddenBiased - 1); j++) {
            double temp = (-1.0 * learnRateEta) * errorSignalHidden[j];
            for (int i = 0; i < nrInputBiased; i++) {
                int ji = j * nrInputBiased + i;
                if (i != (nrInputBiased - 1)) {
                    newWeightChanges = temp * lastPattern.getNeuralInput(i);
                } else {
                    newWeightChanges = temp * -1.0;
                }
                weights[ji] += newWeightChanges + momentumAlpha * oldWeightChangesIH[ji];
                oldWeightChangesIH[ji] = newWeightChanges;
            }
        }
    }

    public void initialise() {
    }
}
