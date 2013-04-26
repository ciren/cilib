/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.ShuffleOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.nn.NeuralNetworks;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.problem.AbstractProblem;
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
    private boolean initialised;

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
        initialised = false;
    }

    /**
     * Initialises the problem by reading in the data and constructing the datatable,
     * as well as the initial training and generalisation sets. Also initialises (constructs) the neural network.
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
            dataTable = dataTableBuilder.getDataTable();

            int trainingSize = (int)(windowSize * trainingSetPercentage);
            int generalisationSize = windowSize - trainingSize;

            StandardPatternDataTable candidateSet = new StandardPatternDataTable();
            trainingSet = new StandardPatternDataTable();
            generalisationSet = new StandardPatternDataTable();

            for (int i = 0; i < windowSize; i++) { // fetch patterns to fill the initial window
                candidateSet.addRow((StandardPattern) dataTable.removeRow(0));
            }

            ShuffleOperator initialShuffler = new ShuffleOperator();
            initialShuffler.operate(candidateSet);


            for (int i = 0; i < trainingSize; i++) {
                trainingSet.addRow((StandardPattern) candidateSet.getRow(i));
            }

            for (int i = trainingSize; i < generalisationSize + trainingSize; i++) {
                generalisationSet.addRow((StandardPattern) candidateSet.getRow(i));
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
     * the MSE (which is minimized). Also checks whether the window has to be slided,
     * and slides the window when necessary by adjusting the training and generalisation sets.
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

        if(currentIteration - changeFrequency * dataChangesCounter == 0 && currentIteration != previousIteration) { // update training & generalisation sets (slide the window)
            try {
                previousIteration = currentIteration;
                dataChangesCounter++;

                StandardPatternDataTable candidateSet = new StandardPatternDataTable();
                for (int i = 0; i < stepSize; i++) {
                    candidateSet.addRow((StandardPattern) dataTable.removeRow(0));
                }

                ShuffleOperator initialShuffler = new ShuffleOperator();
                initialShuffler.operate(candidateSet);

                int trainingStepSize = (int)(stepSize * trainingSetPercentage);
                int generalisationStepSize = stepSize - trainingStepSize;

                for (int t = 0; t < trainingStepSize; t++){
                    trainingSet.removeRow(0);
                    trainingSet.addRow(candidateSet.removeRow(0));
                }

                for (int t = 0; t < generalisationStepSize; t++){
                    generalisationSet.removeRow(0);
                    generalisationSet.addRow(candidateSet.removeRow(0));
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
        if (!initialised) {
            this.initialise();
        }
        int numWeights = NeuralNetworks.countWeights(neuralNetwork);
        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        StringBasedDomainRegistry stringBasedDomainRegistry = new StringBasedDomainRegistry();
        stringBasedDomainRegistry.setDomainString(domainString + "^" + numWeights);
        return stringBasedDomainRegistry;
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
