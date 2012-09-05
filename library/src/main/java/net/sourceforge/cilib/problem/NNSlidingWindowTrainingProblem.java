/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.ShuffleOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents a {@link NNTrainingProblem} where the goal is to optimize
 * the set of weights of a neural network to best fit a given dynamic dataset (either
 * regression, classification etc.). Sliding window is used to simulate dynamic changes.
 * User-specified step size, frequency, and sliding window size control the dynamics
 * of the sliding window. Sliding window moves over the dataset and presents patterns
 * to the neural network in batches equal to the size of the window.
 */
public class NNSlidingWindowTrainingProblem extends NNTrainingProblem {
    private static final long serialVersionUID = -8765101028460476990L;

    private DataTableBuilder dataTableBuilder;
    private DataTable dataTable; // stores the entire data set from which training & generalisation sets are sampled
    private int previousShuffleIteration;
    private int previousIteration;
    private boolean initialized;

    private int dataChangesCounter = 1; // # times the dataset was dynamically updated (has to start with 1)
    private int stepSize; // step size for each set, i.e. # patterns by which the sliding window moves forward in each dynamic step
    private int changeFrequency; // # algorithm iterations after which the window will slide
    private int windowSize; // number of patterns in the active set

    /**
     * Default constructor.
     */
    public NNSlidingWindowTrainingProblem() {
        super();
        dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        previousShuffleIteration = -1;
        previousIteration = -1;
        initialized = false;
    }

    /**
     * Initializes the problem by reading in the data and constructing the datatable,
     * as well as the initial training and generalization sets. Also initializes (constructs) the neural network.
     */
    @Override
    public void initialise() {
        if (initialized) {
            return;
        }
        try {
            dataTableBuilder.addDataOperator(new TypeConversionOperator());
            dataTableBuilder.addDataOperator(patternConverstionOperator);
            dataTableBuilder.buildDataTable();
            dataTable = (StandardPatternDataTable) dataTableBuilder.getDataTable();

            int trainingSize = (int)(windowSize * trainingSetPercentage);
            int generalizationSize = windowSize - trainingSize;

            StandardPatternDataTable candidateSet = new StandardPatternDataTable();
            trainingSet = new StandardPatternDataTable();
            generalizationSet = new StandardPatternDataTable();

            for (int i = 0; i < windowSize; i++) { // fetch patterns to fill the initial window
                candidateSet.addRow((StandardPattern) dataTable.removeRow(0));
            }

            shuffler = new ShuffleOperator();
            shuffler.operate(candidateSet);


            for (int i = 0; i < trainingSize; i++) {
                trainingSet.addRow((StandardPattern) candidateSet.getRow(i));
            }

            for (int i = trainingSize; i < generalizationSize + trainingSize; i++) {
                generalizationSet.addRow((StandardPattern) candidateSet.getRow(i));
            }

            neuralNetwork.initialize();
        } catch (CIlibIOException exception) {
            exception.printStackTrace();
        }
        initialized = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractProblem getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Calculates the fitness of the given solution by setting the neural network
     * weights to the solution and evaluating the training set in order to calculate
     * the MSE (which is minimized). Also checks whether the window has to be slided,
     * and slides the window when necessary by adjusting the training and generalization sets.
     * @param solution the weights representing a solution.
     * @return a new MinimizationFitness wrapping the MSE training error.
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        if (trainingSet == null) {
            this.initialise();
        }

        int currentIteration = AbstractAlgorithm.get().getIterations();
        if (currentIteration != previousShuffleIteration) {
            try {
                shuffler.operate(trainingSet);
            } catch (CIlibIOException exception) {
                exception.printStackTrace();
            }
        }

        if(currentIteration - changeFrequency * dataChangesCounter == 0 && currentIteration != previousIteration) { // update training & generalisation sets (slide the window)
            try {
                previousIteration = currentIteration;
                dataChangesCounter++;

                StandardPatternDataTable candidateSet = new StandardPatternDataTable();
                for (int i = 0; i < stepSize; i++) {
                    candidateSet.addRow((StandardPattern) dataTable.removeRow(0));
                }

                shuffler = new ShuffleOperator();
                shuffler.operate(candidateSet);

                int trainingStepSize = (int)(stepSize * trainingSetPercentage);
                int generalizationStepSize = stepSize - trainingStepSize;

                for (int t = 0; t < trainingStepSize; t++){
                    trainingSet.removeRow(0);
                    trainingSet.addRow(candidateSet.removeRow(0));
                }

                for (int t = 0; t < generalizationStepSize; t++){
                    generalizationSet.removeRow(0);
                    generalizationSet.addRow(candidateSet.removeRow(0));
                }
            } catch (CIlibIOException exception) {
                exception.printStackTrace();
            }
        }

        neuralNetwork.setWeights((Vector) solution);

        double errorTraining = 0.0;
        OutputErrorVisitor visitor = new OutputErrorVisitor();
        Vector error = null;
        for (StandardPattern pattern : trainingSet) {
            Vector output = neuralNetwork.evaluatePattern(pattern);
            visitor.setInput(pattern);
            neuralNetwork.getArchitecture().accept(visitor);
            error = visitor.getOutput();
            for (Numeric real : error) {
                errorTraining += real.doubleValue() * real.doubleValue();
            }
        }
        errorTraining /= trainingSet.getNumRows() * error.size();

        return objective.evaluate(errorTraining);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainRegistry getDomain() {
        if (!initialized) {
            this.initialise();
        }
        int numWeights = neuralNetwork.getWeights().size();
        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        StringBasedDomainRegistry dr = new StringBasedDomainRegistry();
        dr.setDomainString(domainString + "^" + numWeights);
        return dr;
    }

    /**
     * Gets the datatable builder.
     * @return the datatable builder.
     */
    public DataTableBuilder getDataTableBuilder() {
        return dataTableBuilder;
    }

    /**
     * Sets the datatable builder.
     * @param dataTableBuilder the new datatable builder.
     */
    public void setDataTableBuilder(DataTableBuilder dataTableBuilder) {
        this.dataTableBuilder = dataTableBuilder;
    }

    /**
     * Gets the source URL of the the datatable builder.
     * @return the source URL of the the datatable builder.
     */
    public String getSourceURL() {
        return dataTableBuilder.getSourceURL();
    }

    /**
     * Sets the source URL of the the datatable builder.
     * @param sourceURL the new source URL of the the datatable builder.
     */
    public void setSourceURL(String sourceURL) {
        dataTableBuilder.setSourceURL(sourceURL);
    }

    /**
     * Gets the change frequency value.
     * @return the change frequency value.
     */
    public int getChangeFrequency() {
        return changeFrequency;
    }

    /**
     * Sets the change frequency value.
     * @param changeFrequency the change frequency value.
     */
    public void setChangeFrequency(int changeFrequency) {
        this.changeFrequency = changeFrequency;
    }

    /**
     * Gets the sliding window step size.
     * @return the sliding window step size.
     */
    public int getStepSize() {
        return stepSize;
    }

    /**
     * Sets the sliding window step size.
     * @param stepSize the sliding window step size.
     */
    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * Gets the sliding window size.
     * @return the sliding window size.
     */
    public int getWindowSize() {
        return windowSize;
    }

    /**
     * Sets the sliding window size.
     * @param windowSize the sliding window size.
     */
    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }
}
