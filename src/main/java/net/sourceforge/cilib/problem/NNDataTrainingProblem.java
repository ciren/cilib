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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.io.transform.ShuffleOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents an {@link OptimisationProblem} where the goal is to optimize
 * the set of weights of a neural network to best fit a given dataset (either
 * regression, classification etc.).
 * @author andrich
 */
public class NNDataTrainingProblem extends OptimisationProblemAdapter {
    private static final long serialVersionUID = -8765101028460476990L;

    private NeuralNetwork neuralNetwork;
    private DataTableBuilder dataTableBuilder;
    private StandardPatternDataTable trainingSet;
    private StandardPatternDataTable generalizationSet;
    private double trainingSetPercentage;
    private double generalizationSetPercentage;
    private int previousShuffleIteration;
    private ShuffleOperator shuffler;
    private boolean initialized;
    private DataOperator patternConverstionOperator;

    /**
     * Default constructor.
     */
    public NNDataTrainingProblem() {
        neuralNetwork = new NeuralNetwork();
        dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        previousShuffleIteration = -1;
        trainingSetPercentage = 0.66;
        generalizationSetPercentage = 0.34;
        patternConverstionOperator = new PatternConversionOperator();
        initialized = false;
    }

    /**
     * Initializes the problem by reading in the data and constructing the training
     * and generalization sets. Also initializes (constructs) the neural network.
     */
    public void initialise() {
        if (initialized) {
            return;
        }
        try {
            dataTableBuilder.addDataOperator(new TypeConversionOperator());
            dataTableBuilder.addDataOperator(patternConverstionOperator);
            dataTableBuilder.buildDataTable();
            DataTable dataTable = (StandardPatternDataTable) dataTableBuilder.getDataTable();

            shuffler = new ShuffleOperator();
            shuffler.operate(dataTable);

            int trainingSize = (int) (dataTable.size() * trainingSetPercentage);
            int generalizationSize = dataTable.size() - trainingSize;

            trainingSet = new StandardPatternDataTable();
            generalizationSet = new StandardPatternDataTable();

            for (int i = 0; i < trainingSize; i++) {
                trainingSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            for (int i = trainingSize; i < generalizationSize + trainingSize; i++) {
                generalizationSet.addRow((StandardPattern) dataTable.getRow(i));
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
    public OptimisationProblemAdapter getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Calculates the fitness of the given solution by setting the neural network
     * weights to the solution and evaluating the training set in order to calculate
     * the MSE (which is minimized).
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

        return new MinimisationFitness(errorTraining);
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
        StringBasedDomainRegistry domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString(domainString + "^" + numWeights);
        return domainRegistry;
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
     * Gets the generalization dataset.
     * @return the generalization dataset.
     */
    public StandardPatternDataTable getGeneralizationSet() {
        return generalizationSet;
    }

    /**
     * Sets the generalization dataset.
     * @param generalizationSet the new generalization dataset.
     */
    public void setGeneralizationSet(StandardPatternDataTable generalizationSet) {
        this.generalizationSet = generalizationSet;
    }

    /**
     * Gets the percentage of the training set to use for generalization.
     * @return the percentage of the training set to use for generalization.
     */
    public double getGeneralizationSetPercentage() {
        return generalizationSetPercentage;
    }

    /**
     * Sets the percentage of the training set to use for generalization.
     * @param generalizationSetPercentage the new percentage of the training set to use for generalization.
     */
    public void setGeneralizationSetPercentage(double generalizationSetPercentage) {
        this.generalizationSetPercentage = generalizationSetPercentage;
    }

    /**
     * Gets the neural network.
     * @return the neural network.
     */
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    /**
     * Sets the neural network.
     * @param neuralNetwork the new neural network.
     */
    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    /**
     * Gets the training dataset.
     * @return the training dataset.
     */
    public StandardPatternDataTable getTrainingSet() {
        return trainingSet;
    }

    /**
     * Sets the training dataset.
     * @param trainingSet the new training dataset.
     */
    public void setTrainingSet(StandardPatternDataTable trainingSet) {
        this.trainingSet = trainingSet;
    }

    /**
     * Gets the percentage of the training set to use for training.
     * @return the percentage of the training set to use for training.
     */
    public double getTrainingSetPercentage() {
        return trainingSetPercentage;
    }

    /**
     * Sets the percentage of the training set to use for training.
     * @param trainingSetPercentage the new percentage of the training set to use for training.
     */
    public void setTrainingSetPercentage(double trainingSetPercentage) {
        this.trainingSetPercentage = trainingSetPercentage;
    }

    /**
     * Gets the {@link ShuffleOperator}
     * @return the shuffle operator.
     */
    public ShuffleOperator getShuffler() {
        return shuffler;
    }

    /**
     * Sets the {@link ShuffleOperator}
     * @param shuffler the new shuffle operator.
     */
    public void setShuffler(ShuffleOperator shuffler) {
        this.shuffler = shuffler;
    }

    /**
     * Get the { @link PatternConversionOperator}
     * @return the pattern conversion operator
     */
    public DataOperator getPatternConversionOperator() {
        return patternConverstionOperator;
    }

    /**
     * Set the { @link PatternConversionOperator}
     * @param patternConverstionOperator the new pattern conversion operator
     */
    public void setPatternConversionOperator(DataOperator patternConverstionOperator) {
        this.patternConverstionOperator = patternConverstionOperator;
    }
}
