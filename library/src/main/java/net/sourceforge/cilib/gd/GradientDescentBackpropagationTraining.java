/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.gd;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.visitors.BackPropagationVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implementing the gradient descent backpropagation training algorithm. The
 * error measure used is MSE and it supports both learning rate and momentum
 * parameters.
 */
public class GradientDescentBackpropagationTraining extends AbstractAlgorithm implements SingularAlgorithm {
    private static final long serialVersionUID = 7984749431187521004L;

    private ControlParameter learningRate;
    private ControlParameter momentum;
    private double errorTraining;
    private BackPropagationVisitor bpVisitor;
    private double[][] previousWeightChanges;

    /**
     * Default constructor.
     */
    public GradientDescentBackpropagationTraining() {
        learningRate = ConstantControlParameter.of(0.1);
        momentum = ConstantControlParameter.of(0.9);
        bpVisitor = new BackPropagationVisitor();
    }

    public GradientDescentBackpropagationTraining(GradientDescentBackpropagationTraining copy) {
        learningRate = copy.learningRate.getClone();
        momentum = copy.momentum.getClone();
        bpVisitor = copy.bpVisitor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmInitialisation() {
        NNTrainingProblem problem = (NNTrainingProblem) getOptimisationProblem();
        problem.initialise();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        try {
            NNTrainingProblem problem = (NNTrainingProblem) getOptimisationProblem();
            NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
            StandardPatternDataTable trainingSet = problem.getTrainingSet();
            problem.getShuffler().operate(trainingSet);
            bpVisitor.setLearningRate(this.learningRate.getParameter());
            bpVisitor.setMomentum(this.momentum.getParameter());

            errorTraining = 0.0;
            OutputErrorVisitor visitor = new OutputErrorVisitor();
            Vector error = null;
            for (StandardPattern pattern : trainingSet) {
                neuralNetwork.evaluatePattern(pattern);
                visitor.setInput(pattern);
                neuralNetwork.getArchitecture().accept(visitor);
                error = visitor.getOutput();
                for (Numeric real : error) {
                    errorTraining += real.doubleValue()*real.doubleValue();
                }

                // backpropagate
                bpVisitor.setPreviousPattern(pattern);
                bpVisitor.setPreviousWeightUpdates(previousWeightChanges);
                neuralNetwork.getArchitecture().accept(bpVisitor);
                previousWeightChanges = bpVisitor.getPreviousWeightUpdates();

            }
            errorTraining /= trainingSet.getNumRows() * error.size();

        } catch (CIlibIOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GradientDescentBackpropagationTraining getClone() {
        return new GradientDescentBackpropagationTraining(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        NNTrainingProblem problem = (NNTrainingProblem) getOptimisationProblem();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        return new OptimisationSolution(neuralNetwork.getWeights(), new MinimisationFitness(errorTraining));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        ArrayList<OptimisationSolution> list = new ArrayList();
        NNTrainingProblem problem = (NNTrainingProblem) getOptimisationProblem();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        list.add(new OptimisationSolution(neuralNetwork.getWeights(), new MinimisationFitness(errorTraining)));
        return list;
    }

    /**
     * Gets the learning rate control parameter.
     * @return the learning rate control parameter.
     */
    public ControlParameter getLearningRate() {
        return learningRate;
    }

    /**
     * Sets the learning rate control parameter.
     * @param learningRate the new learning rate control parameter.
     */
    public void setLearningRate(ControlParameter learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Gets the momentum control parameter.
     * @return the momentum control parameter.
     */
    public ControlParameter getMomentum() {
        return momentum;
    }

    /**
     * Sets the momentum control parameter.
     * @param momentum the new momentum control parameter.
     */
    public void setMomentum(ControlParameter momentum) {
        this.momentum = momentum;
    }
}
