/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.nn.architecture.visitors.CascadeVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;

/**
 * Class represents a {@link NNTrainingProblem} where the goal is to optimize
 * the set of weights of the output layer in a cascade network. It treats the
 * means squared error of all the outputs as the fitness to be minimised.
 */
public class CascadeOutputLayerTrainingProblem extends NNTrainingProblem {

    private ArrayList<Layer> activationCache;
    private int weightEvaluationCount;

    public CascadeOutputLayerTrainingProblem() {
        super();
        activationCache = new ArrayList<Layer>();
        weightEvaluationCount = 0;
    }

    public CascadeOutputLayerTrainingProblem(CascadeOutputLayerTrainingProblem rhs) {
        super(rhs);
        weightEvaluationCount = rhs.weightEvaluationCount;

        activationCache = new ArrayList<Layer>();
        for (Layer curLayer : rhs.activationCache) {
            activationCache.add(curLayer.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CascadeOutputLayerTrainingProblem getClone() {
        return new CascadeOutputLayerTrainingProblem(this);
    }

    /**
     * Initializes the problem by generating a cache of NN activations.
     * It does not generate the data sets; it is assumed that these
     * data sets will be provided by another problem.
     */
    @Override
    public void initialise() {
        generateCache();

        List<Layer> layers = neuralNetwork.getArchitecture().getLayers();
        int numWeights = 0;
        for (int curLayer = 0; curLayer < layers.size()-1; ++curLayer) {
            numWeights += layers.get(curLayer).size();
        }

        numWeights *= layers.get(layers.size()-1).size();

        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString(domainString + "^" + numWeights);
    }

    /**
     * Calculates the fitness of the given solution by setting the output layer
     * weights to the solution and evaluating the training set in order to calculate
     * the means squared error of the errors produced by the candidate output layer.
     *
     * @param solution the weights representing a solution.
     * @return a new MinimizationFitness wrapping the MSE.
     */
    @Override
    protected MinimisationFitness calculateFitness(Type solution) {

        weightEvaluationCount += ((Vector) solution).size();

        Layer candidateLayer = neuralNetwork.getArchitecture().getLayers().get(neuralNetwork.getArchitecture().getNumLayers()-1);
        int currentIndex = 0;
        for (Neuron neuron : candidateLayer) {
            Vector neuronWeights = neuron.getWeights();
            int size = neuronWeights.size();
            for (int j = 0; j < size; j++) {
                neuronWeights.set(j, ((Vector) solution).get(currentIndex++));
            }
        }

        //calculate MSE
        double mse = 0.0;
        OutputErrorVisitor errorVisitor = new OutputErrorVisitor();
        for (int curPattern = 0; curPattern < activationCache.size(); ++curPattern) {
            //Feed consolidated layers to new output layer.
            //The receiving Neuron must ensure that it doesn't process more inputs
            //than what it has weights for.
            for (Neuron curNeuron : candidateLayer) {
                curNeuron.calculateActivation(activationCache.get(curPattern));
            }

            //calculate mse
            errorVisitor.setInput(trainingSet.getRow(curPattern));
            errorVisitor.visit(neuralNetwork.getArchitecture());
            for (Numeric curOutput : errorVisitor.getOutput()) {
                mse += Math.pow(curOutput.doubleValue(), 2);
            }
        }

        return new MinimisationFitness(mse / (candidateLayer.size()*trainingSet.size()));
    }

    /**
     * Generates the cache of NN activations. Since the NN always produce the same
	 * activations in the layers preceding the output layer, this cache can be
	 * used instead of the actual NN.
     */
    private void generateCache() {
        activationCache.clear();

        CascadeVisitor propegateVisitor = new CascadeVisitor();

        for (int curPattern = 0; curPattern < trainingSet.size(); ++curPattern) {
            //propegate input
            propegateVisitor.setInput(trainingSet.getRow(curPattern));
            propegateVisitor.visit(neuralNetwork.getArchitecture());

            //Consolidate multiple layers into a single input.
            Layer consolidatedLayer = new Layer();
            for (Layer curLayer : neuralNetwork.getArchitecture().getLayers()) {
                for (int curNeuron = 0; curNeuron < curLayer.size(); curNeuron++) {
                    consolidatedLayer.add(curLayer.getNeuron(curNeuron));
                }
            }
            activationCache.add(consolidatedLayer.getClone());
        }
    }

    /**
     * Gets the current cache of neuron activations.
     * These activations must not be changed by the caller.
     * @return The activation cache.
     */
    public ArrayList<Layer> getActivationCache() {
        return activationCache;
    }

    /**
     * Gets the number of weight evaluations performed. This only includes
     * weight evaluations performed in the output layer.
     * @return The number of weight evaluations.
     */
    public int getWeightEvaluationCount() {
        return weightEvaluationCount;
    }
}
