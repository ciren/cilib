/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import com.google.common.annotations.VisibleForTesting;
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
import net.sourceforge.cilib.nn.domain.*;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents a {@link NNTrainingProblem} where the goal is to optimize
 * the set of weights of a neural network to best fit a given static dataset (either
 * regression, classification etc.).
 */
public class NNDataTrainingProblem extends NNTrainingProblem {
    private static final long serialVersionUID = -8765101028460476990L;

    private DataTableBuilder dataTableBuilder;
    private DomainInitialisationStrategy domainInitialisationStrategy;
    private SolutionConversionStrategy solutionConversionStrategy;
    private int previousShuffleIteration;
    private boolean initialised;

    /**
     * Default constructor.
     */
    public NNDataTrainingProblem() {
        super();
        dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        domainInitialisationStrategy = new WeightBasedDomainInitialisationStrategy();
        solutionConversionStrategy = new WeightSolutionConversionStrategy();
        previousShuffleIteration = -1;
        initialised = false;
    }

    /**
     * Initialises the problem by reading in the data and constructing the training
     * and generalisation sets. Also initialises (constructs) the neural network.
     */
    @Override
    public void initialise() {
        if (initialised) {
            return;
        }
        try {
            dataTableBuilder.addDataOperator(new TypeConversionOperator());
            dataTableBuilder.addDataOperator(patternConversionOperator);
            dataTableBuilder.buildDataTable();
            DataTable dataTable = dataTableBuilder.getDataTable();

            ShuffleOperator initialShuffler = new ShuffleOperator();
            initialShuffler.operate(dataTable);

            int trainingSize = (int) (dataTable.size() * trainingSetPercentage);
            int validationSize = (int) (dataTable.size() * validationSetPercentage);
            int generalisationSize = dataTable.size() - trainingSize - validationSize;

            trainingSet = new StandardPatternDataTable();
            validationSet = new StandardPatternDataTable();
            generalisationSet = new StandardPatternDataTable();

            for (int i = 0; i < trainingSize; i++) {
                trainingSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            for (int i = trainingSize; i < validationSize + trainingSize; i++) {
                validationSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            for (int i = validationSize + trainingSize; i < generalisationSize + validationSize + trainingSize; i++) {
                generalisationSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            neuralNetwork.initialise();
        } catch (CIlibIOException exception) {
            exception.printStackTrace();
        }
        initialised = true;
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
     * the MSE (which is minimized).
     *
     * @param solution the weights representing a solution.
     * @return a new MinimisationFitness wrapping the MSE training error.
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

        neuralNetwork.getArchitecture().accept(solutionConversionStrategy.interpretSolution(solution));

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
        if (!initialised) {
            this.initialise();
        }
        return initialiseDomain();
    }

    @VisibleForTesting
    protected DomainRegistry initialiseDomain() {
        solutionConversionStrategy.initialise(neuralNetwork);
        return domainInitialisationStrategy.initialiseDomain(neuralNetwork);
    }

    /**
     * Gets the datatable builder.
     *
     * @return the datatable builder.
     */
    public DataTableBuilder getDataTableBuilder() {
        return dataTableBuilder;
    }

    /**
     * Sets the datatable builder.
     *
     * @param dataTableBuilder the new datatable builder.
     */
    public void setDataTableBuilder(DataTableBuilder dataTableBuilder) {
        this.dataTableBuilder = dataTableBuilder;
    }

    /**
     * Gets the source URL of the the datatable builder.
     *
     * @return the source URL of the the datatable builder.
     */
    public String getSourceURL() {
        return dataTableBuilder.getSourceURL();
    }

    /**
     * Sets the source URL of the the datatable builder.
     *
     * @param sourceURL the new source URL of the the datatable builder.
     */
    public void setSourceURL(String sourceURL) {
        dataTableBuilder.setSourceURL(sourceURL);
    }

    public DomainInitialisationStrategy getDomainInitialisationStrategy() {
        return domainInitialisationStrategy;
    }

    public void setDomainInitialisationStrategy(DomainInitialisationStrategy domainInitialisationStrategy) {
        this.domainInitialisationStrategy = domainInitialisationStrategy;
    }

    public SolutionConversionStrategy getSolutionConversionStrategy() {
        return solutionConversionStrategy;
    }

    public void setSolutionConversionStrategy(SolutionConversionStrategy solutionConversionStrategy) {
        this.solutionConversionStrategy = solutionConversionStrategy;
    }
}
