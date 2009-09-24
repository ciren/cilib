/*
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
package net.sourceforge.cilib.gd;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.visitors.BackpropagationVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implementing the gradien decent backpropagation training algorithm. The
 * error measure used is MSE and it supports both learning rate and momemtum
 * parameters.
 * @author andrich
 */
public class GradientDescentBackpropagationTraining extends AbstractAlgorithm implements SingularAlgorithm {
    private static final long serialVersionUID = 7984749431187521004L;

    private ControlParameter learningRate;
    private ControlParameter momentum;
    private double errorTraining;
    private BackpropagationVisitor bpVisitor;
    private double[][] previousWeightChanges;

    /**
     * Default constructor.
     */
    public GradientDescentBackpropagationTraining() {
        learningRate = new ConstantControlParameter(0.1);
        momentum = new ConstantControlParameter(0.9);
        bpVisitor = new BackpropagationVisitor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInitialisation() {
        NNDataTrainingProblem problem = (NNDataTrainingProblem) getOptimisationProblem();
        problem.initialise();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        try {
            NNDataTrainingProblem problem = (NNDataTrainingProblem) getOptimisationProblem();
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
                    errorTraining += real.getReal()*real.getReal();
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
    public Algorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        NNDataTrainingProblem problem = (NNDataTrainingProblem) getOptimisationProblem();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        return new OptimisationSolution(neuralNetwork.getWeights(), new MinimisationFitness(errorTraining));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        ArrayList<OptimisationSolution> list = new ArrayList<OptimisationSolution>();
        NNDataTrainingProblem problem = (NNDataTrainingProblem) getOptimisationProblem();
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
