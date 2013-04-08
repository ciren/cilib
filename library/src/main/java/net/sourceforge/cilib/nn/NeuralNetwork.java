/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.visitors.ArchitectureOperationVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.FeedForwardVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.WeightRetrievalVisitor;
import net.sourceforge.cilib.nn.architecture.visitors.WeightSettingVisitor;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents a Neural Network and encapsulates an {@link Architecture}
 * and a default {@link ArchitectureOperationVisitor}. It has a number of
 * convenience methods for common operations.
 */
public class NeuralNetwork {

    private Architecture architecture;
    private ArchitectureOperationVisitor operationVisitor;

    /**
     * Default constructor. The default operation visitor is a {@link FeedForwardVisitor}
     */
    public NeuralNetwork() {
        architecture = new Architecture();
        operationVisitor = new FeedForwardVisitor();
    }

    public NeuralNetwork(NeuralNetwork rhs) {
        architecture = new Architecture(rhs.architecture);
        operationVisitor = rhs.operationVisitor.getClone();
    }

    /**
     * Initialises the network by initialising the architecture.
     */
    public void initialise() {
        architecture.initialise();
    }

    /**
     * Evaluates a pattern by passing it as input to the default operation visitor
     * and returning the result.
     * @param pattern the pattern to evaluate.
     * @return the result of the evaluation.
     */
    public Vector evaluatePattern(StandardPattern pattern) {
        operationVisitor.setInput(pattern);
        architecture.accept(operationVisitor);
        return operationVisitor.getOutput();
    }

    /**
     * Gets the neural network architecture.
     * @return the neural network architecture.
     */
    public Architecture getArchitecture() {
        return architecture;
    }

    /**
     * Sets the neural network architecture.
     * @param architecture the new neural network architecture.
     */
    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Gets the operation visitor.
     * @return the operation visitor.
     */
    public ArchitectureOperationVisitor getOperationVisitor() {
        return operationVisitor;
    }

    /**
     * Sets the operation visitor.
     * @param operationVisitor the new operation visitor.
     */
    public void setOperationVisitor(ArchitectureOperationVisitor operationVisitor) {
        this.operationVisitor = operationVisitor;
    }

    /**
     * Gets the weights contained in the network.
     * @return the weights contained in the network.
     */
    public Vector getWeights() {
        WeightRetrievalVisitor visitor = new WeightRetrievalVisitor();
        this.architecture.accept(visitor);
        return visitor.getOutput();
    }

    /**
     * Sets the weights contained in the network.
     * @param weights the new weights contained in the network.
     */
    public void setWeights(Vector weights) {
        WeightSettingVisitor visitor = new WeightSettingVisitor(weights);
        this.architecture.accept(visitor);
    }
}
