/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import java.util.List;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Visitor implements a general gradient decent backpropagation algorithm
 * for an N layer feed forward network with momentum and learning rate terms.
 */
public class BackPropagationVisitor implements ArchitectureVisitor {

    private StandardPattern previousPattern;
    private double[][] layerWeightsDelta;
    private double momentum;
    private double learningRate;
    private double[][] previousWeightUpdates;

    /**
     * Default constructor.
     */
    public BackPropagationVisitor() {
        learningRate = 0.1;
        momentum = 0.9;
    }

    public BackPropagationVisitor(BackPropagationVisitor rhs) {
        learningRate = rhs.learningRate;
        momentum = rhs.learningRate;

        previousPattern = new StandardPattern(rhs.previousPattern);

        layerWeightsDelta = new double[rhs.layerWeightsDelta.length][];
        for (int i = 0; i < rhs.layerWeightsDelta.length; ++i) {
            layerWeightsDelta[i] = new double[rhs.layerWeightsDelta[i].length];
            for (int j = 0; j < rhs.layerWeightsDelta[i].length; ++j) {
                layerWeightsDelta[i][j] = rhs.layerWeightsDelta[i][j];
            }
        }

        previousWeightUpdates = new double[rhs.previousWeightUpdates.length][];
        for (int i = 0; i < rhs.previousWeightUpdates.length; ++i) {
            previousWeightUpdates[i] = new double[rhs.previousWeightUpdates[i].length];
            for (int j = 0; j < rhs.previousWeightUpdates[i].length; ++j) {
                previousWeightUpdates[i][j] = rhs.previousWeightUpdates[i][j];
            }
        }
    }

    public BackPropagationVisitor getClone() {
        return new BackPropagationVisitor(this);
    }

    /**
     * Performs a gradient decent backpropagation given the previous {@link StandardPattern}
     * as input as well as the weight updates after the previous execution of a
     * backpropagation. If the previous weight updates do not exist, the visitor
     * will create them and initialise them to zero.
     * @param architecture
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        int numLayers = layers.size();
        int currentLayerIdx = numLayers - 1; // skip input layer
        Layer currentLayer = layers.get(currentLayerIdx);
        int layerSize = currentLayer.size();
        Layer nextLayer = null;
        int nextLayerSize = -1;
        Neuron currentNeuron;

        // setup delta storage
        layerWeightsDelta = new double[numLayers - 1][]; // not necessary for input layer

        //calculate output layer deltas
        layerWeightsDelta[currentLayerIdx - 1] = new double[layerSize];
        for (int k = 0; k < layerSize; k++) {
            currentNeuron = currentLayer.get(k);
            double t_k = layerSize > 1 ? ((Vector) previousPattern.getTarget()).doubleValueOf(k) : ((Real) previousPattern.getTarget()).doubleValue();
            double o_k = currentNeuron.getActivation();
            layerWeightsDelta[currentLayerIdx - 1][k] = -1.0 * (t_k - o_k) * currentNeuron.getActivationFunction().getGradient(o_k);
        }

        //calculate deltas for all hidden layers
        for (currentLayerIdx = numLayers - 2; currentLayerIdx > 0; currentLayerIdx--) {
            currentLayer = layers.get(currentLayerIdx);
            layerSize = currentLayer.size();
            layerSize = currentLayer.isBias() ? layerSize - 1 : layerSize;
            layerWeightsDelta[currentLayerIdx - 1] = new double[layerSize];
            for (int j = 0; j < layerSize; j++) {
                layerWeightsDelta[currentLayerIdx - 1][j] = 0.0;
                nextLayer = layers.get(currentLayerIdx + 1);
                nextLayerSize = nextLayer.size();
                nextLayerSize = nextLayer.isBias() ? nextLayerSize - 1 : nextLayerSize;
                for (int k = 0; k < nextLayerSize; k++) {
                    double w_kj = nextLayer.get(k).getWeights().doubleValueOf(j);
                    layerWeightsDelta[currentLayerIdx - 1][j] += w_kj * layerWeightsDelta[currentLayerIdx][k];
                }
                currentNeuron = currentLayer.get(j);
                layerWeightsDelta[currentLayerIdx - 1][j] *= currentNeuron.getActivationFunction().getGradient(currentNeuron.getActivation());
            }
        }

        //storage for the weight updates
        if (previousWeightUpdates == null) {
            previousWeightUpdates = new double[numLayers - 1][];
            for (currentLayerIdx = numLayers - 1; currentLayerIdx > 0; currentLayerIdx--) {
                for (int k = 0; k < layerSize; k++) {
                    currentLayer = layers.get(currentLayerIdx);
                    layerSize = currentLayer.isBias() ? currentLayer.size() - 1 : currentLayer.size();
                    int previousLayerSize = layers.get(currentLayerIdx - 1).size();
                    previousWeightUpdates[currentLayerIdx - 1] = new double[layerSize * previousLayerSize + previousLayerSize + 1];
                }
            }
        }

        ((ForwardingLayer) layers.get(0)).setSource(new PatternInputSource(previousPattern));
        //updates output and all hidden layer weights
        for (currentLayerIdx = numLayers - 1; currentLayerIdx > 0; currentLayerIdx--) { // loop excludes input layer
            currentLayer = layers.get(currentLayerIdx);
            layerSize = currentLayer.isBias() ? currentLayer.size() - 1 : currentLayer.size();
            int previousLayerSize = -1;
            Layer previousLayer = null;

            for (int k = 0; k < layerSize; k++) {
                currentNeuron = currentLayer.get(k);
                previousLayer = layers.get(currentLayerIdx - 1);
                previousLayerSize = previousLayer.size();

                double tmp = (-1.0 * learningRate) * layerWeightsDelta[currentLayerIdx - 1][k];
                for (int j = 0; j < previousLayerSize; j++) {
                    double weight = currentNeuron.getWeights().doubleValueOf(j);
                    double newWeightUpdate = tmp * previousLayer.getNeuralInput(j);
                    double update = newWeightUpdate + momentum * previousWeightUpdates[currentLayerIdx - 1][k * previousLayerSize + j];
                    currentNeuron.getWeights().setReal(j, weight + update);
                    previousWeightUpdates[currentLayerIdx - 1][k * previousLayerSize + j] = newWeightUpdate;
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * Gets the learning rate.
     * @return the learning rate.
     */
    public double getLearningRate() {
        return learningRate;
    }

    /**
     * Sets the learning rate.
     * @param learningRate the new learning rate.
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Gets the momentum.
     * @return the momentum.
     */
    public double getMomentum() {
        return momentum;
    }

    /**
     * Sets the momentum.
     * @param momentum the new momentum.
     */
    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    /**
     * Gets the previous weight updates.
     * @return the previous weight updates.
     */
    public double[][] getPreviousWeightUpdates() {
        return previousWeightUpdates;
    }

    /**
     * Sets the previous weight updates.
     * @param previousWeightUpdates the new previous weight updates.
     */
    public void setPreviousWeightUpdates(double[][] previousWeightUpdates) {
        this.previousWeightUpdates = previousWeightUpdates;
    }

    /**
     * Gets the previous pattern.
     * @return the previous pattern.
     */
    public StandardPattern getPreviousPattern() {
        return previousPattern;
    }

    /**
     * Sets the previous pattern.
     * @param previousPattern the new previous pattern.
     */
    public void setPreviousPattern(StandardPattern previousPattern) {
        this.previousPattern = previousPattern;
    }
}
