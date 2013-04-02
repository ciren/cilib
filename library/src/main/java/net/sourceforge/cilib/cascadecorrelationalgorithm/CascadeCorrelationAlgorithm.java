/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.cascadecorrelationalgorithm;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
//import java.util.Collections;
import java.util.ArrayList;
//import java.util.Iterable;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
//import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.CascadeHiddenNeuronCorrelationProblem;
import net.sourceforge.cilib.problem.nn.CascadeOutputLayerTrainingProblem;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

public class CascadeCorrelationAlgorithm extends AbstractAlgorithm {

    private AbstractAlgorithm phase1Algorithm;
	private AbstractAlgorithm phase2Algorithm;
	//private Vector bestSolution;
    private Neuron neuronPrototype;

    public CascadeCorrelationAlgorithm() {
        neuronPrototype = new Neuron();
    }

    public CascadeCorrelationAlgorithm(CascadeCorrelationAlgorithm rhs) {
        neuronPrototype = rhs.neuronPrototype.getClone();
    }

    @Override
    public CascadeCorrelationAlgorithm getClone() {
        return new CascadeCorrelationAlgorithm(this);
    }

    @Override
    public void algorithmInitialisation() {
        //algorithm.setOptimisationProblem(optimisationProblem);
    }

    @Override
    protected void algorithmIteration() {
		//phase 2
    }

    @VisibleForTesting
    protected void phase1() {
        NNTrainingProblem problem = (NNTrainingProblem) optimisationProblem;
        NeuralNetwork network = problem.getNeuralNetwork();
        Vector bestSolution = network.getWeights();
        
        AbstractAlgorithm alg1 = (AbstractAlgorithm) phase1Algorithm.getClone();
        CascadeHiddenNeuronCorrelationProblem correlationProblem = new CascadeHiddenNeuronCorrelationProblem();
        correlationProblem.setNeuron(neuronPrototype);
        correlationProblem.setTrainingSet(problem.getTrainingSet());
        correlationProblem.setValidationSet(problem.getValidationSet());
        correlationProblem.setGeneralisationSet(problem.getGeneralisationSet());
        correlationProblem.setNeuralNetwork(network);

        alg1.setOptimisationProblem(correlationProblem);
        alg1.performInitialisation();
        alg1.run();

        List<OptimisationSolution> solutions = Lists.<OptimisationSolution>newLinkedList(alg1.getSolutions());

        List<LayerConfiguration> layers = network.getArchitecture().getArchitectureBuilder().getLayerConfigurations();
        int consolidatedLayerSize = 0;
        int insertionIndex = 0;
        for (int curLayer = 0; curLayer < layers.size()-1; ++curLayer) {
            insertionIndex += consolidatedLayerSize*layers.get(curLayer).getSize();
            consolidatedLayerSize += layers.get(curLayer).getSize();
            if (layers.get(curLayer).isBias())
                consolidatedLayerSize++;
        }

        for (OptimisationSolution curSolution : solutions) {
            for (Numeric curElement : ((Vector) curSolution.getPosition())) {
                bestSolution.insert(insertionIndex++, curElement);
            }
        }

        insertionIndex += consolidatedLayerSize;
        for (int curOutput = 0; curOutput < layers.get(layers.size()-1).getSize(); ++curOutput) {
            for (int curSolution = 0; curSolution < solutions.size(); ++curSolution) {
                bestSolution.insert(insertionIndex, Real.valueOf(Double.NaN));
            }

            insertionIndex += solutions.size() + consolidatedLayerSize;
        }
        
        //expand neural network
        LayerConfiguration targetLayerConfiguration = new LayerConfiguration(solutions.size(), neuronPrototype.getActivationFunction(), false);
        network.getArchitecture().getArchitectureBuilder().addLayer(layers.size()-1, targetLayerConfiguration);
        network.initialise();
        network.setWeights(bestSolution);
    }

    @Override
    public OptimisationSolution getBestSolution() {
        return null; // Collections.max(solutions);
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        return null; //solutions;
    }

    @Override
    public void setOptimisationProblem(Problem problem) {
        Preconditions.checkArgument(problem instanceof NNTrainingProblem,
                "CascadeCorrelationAlgorithm can only be used with NNTrainingProblem.");
        optimisationProblem = problem;
    }

    public void setPhase1Algorithm(AbstractAlgorithm algorithm) {
        this.phase1Algorithm = algorithm;
    }
}
