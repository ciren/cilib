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
import net.sourceforge.cilib.problem.objective.Maximise;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents a {@link NNTrainingProblem} where the goal is to optimize
 * the set of weights of a candidate neuron in a cascade network. It treats the
 * correlation between the activation of the candidate neuron and the output
 * errors of the cascade network is a fitness that needs to be maximised.
 */
public class CascadeHiddenNeuronCorrelationProblem extends NNTrainingProblem {

    private Neuron neuron;
    private ArrayList<Layer> activationCache;
    private ArrayList<Vector> errorCache;
    private Vector errorMeans;
    private int weightEvaluationCount;

    public CascadeHiddenNeuronCorrelationProblem() {
        super();
        objective = new Maximise();
        neuron = new Neuron();
        activationCache = new ArrayList<Layer>();
        errorCache = new ArrayList<Vector>();
        errorMeans = Vector.of();
        weightEvaluationCount = 0;
    }

    public CascadeHiddenNeuronCorrelationProblem(CascadeHiddenNeuronCorrelationProblem rhs) {
        super(rhs);
        objective = new Maximise();
        neuron = rhs.neuron.getClone();
        errorMeans = rhs.errorMeans.getClone();
        weightEvaluationCount = rhs.weightEvaluationCount;

        activationCache = new ArrayList<Layer>();
        for (Layer curLayer : rhs.activationCache) {
            activationCache.add(curLayer.getClone());
        }

        errorCache = new ArrayList<Vector>();
        for (Vector curError : rhs.errorCache) {
            errorCache.add(curError.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CascadeHiddenNeuronCorrelationProblem getClone() {
        return new CascadeHiddenNeuronCorrelationProblem(this);
    }

    /**
     * Initializes the problem by generating caches of NN activity.
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

        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString(domainString + "^" + numWeights);
    }

    /**
     * Calculates the fitness of the given solution by setting the candidate neuron
     * weights to the solution and evaluating the training set in order to calculate
     * the correlation between the activation of the candidate neuron and the errors
     * of the NN.
     *
     * @param solution the weights representing a solution.
     * @return a new MaximizationFitness wrapping the correlation.
     */
    @Override
    protected MaximisationFitness calculateFitness(Type solution) {

        weightEvaluationCount += ((Vector) solution).size(); 
        neuron.setWeights((Vector) solution);

        //System.out.println(((Vector) solution).size() + " " + activationCache.get(0).size());

        //calculate activations
        double[] activations = new double[trainingSet.size()];
        for (int curPattern = 0; curPattern < activationCache.size(); ++curPattern) {
            //Feed consolidated layers to new neuron.
            //The receiving Neuron must ensure that it doesn't process more inputs
            //than what it has weights for.
            activations[curPattern] = neuron.calculateActivation(activationCache.get(curPattern));
        }

        //calculate means
        double totalActivation = activations[0];
        for (int curPattern = 1; curPattern < trainingSet.size(); ++curPattern) {
            totalActivation += activations[curPattern];
        }
        double meanActivation = totalActivation / trainingSet.size();

        //calculate correlation
        double correlation = 0.0;
        for (int curOutput = 0; curOutput < errorMeans.size(); ++curOutput) {
            double total = 0.0;
            for (int curPattern = 0; curPattern < trainingSet.size(); ++curPattern) {
                total += (activations[curPattern] - meanActivation)
                         * (errorCache.get(curPattern).get(curOutput).doubleValue() - errorMeans.get(curOutput).doubleValue());
            }
            correlation += Math.abs(total);
        }

        return new MaximisationFitness(correlation);
    }

    /**
     * Generates the caches of NN activity. Since the NN always produces the same
     * results when a candidate neuron's fitness is calculated, these caches can be
     * used instead of the NN itself.
     */
    private void generateCache() {
        activationCache.clear();
        errorCache.clear();

        CascadeVisitor propegateVisitor = new CascadeVisitor();
        OutputErrorVisitor errorVisitor = new OutputErrorVisitor();

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

            //calculate output errors
            errorVisitor.setInput(trainingSet.getRow(curPattern));
            errorVisitor.visit(neuralNetwork.getArchitecture());
            errorCache.add(errorVisitor.getOutput());
        }

        //calculate mean of errors
        Vector totalErrors = Vector.copyOf(errorCache.get(0));
        for (int curPattern = 1; curPattern < trainingSet.size(); ++curPattern) {
            totalErrors = totalErrors.plus(errorCache.get(curPattern));
        }
        errorMeans = totalErrors.divide(trainingSet.size());
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
     * Gets the current cache of NN output errors.
     * These errors must not be changed by the caller.
     * @return The error cache.
     */
    public ArrayList<Vector> getErrorCache() {
        return errorCache;
    }

    /**
     * Gets the cache of the means of the NN output errors.
     * These means must not be changed by the caller.
     * @return The mean errors cache.
     */
    public Vector getErrorMeans() {
        return errorMeans;
    }

    /**
     * Sets the prototype neuron which is used to determine the behaviour
     * of the candidate neuron being evaluated.
     * @param newNeuron The new neuron prototype.
     */
    public void setNeuron(Neuron newNeuron) {
        neuron = newNeuron;
    }

    /**
     * Gets the number of weight evaluations performed. This only includes
     * weight evaluations performed while evaluating new candidate hidden
     * neurons.
     * @return The number of weight evaluations.
     */
    public int getWeightEvaluationCount() {
        return weightEvaluationCount;
    }
}
