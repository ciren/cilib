/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * An abstract visitor class implements the {@link ArchitectureVisitor} interface
 * and adds the functionality to the visitor of having an input/output
 * operations where the input is a {@link StandardPattern} and the output is a
 * {@link Vector}
 */
public abstract class ArchitectureOperationVisitor implements ArchitectureVisitor {

    protected StandardPattern input;
    protected Vector output;

    /**
     * Default constructor.
     */
    public ArchitectureOperationVisitor() {
    }

    public ArchitectureOperationVisitor(ArchitectureOperationVisitor rhs) {
        input = rhs.input;
        output = rhs.output;
    }

    public abstract ArchitectureOperationVisitor getClone();

    /**
     * Constructor taking the input StandardPattern as argument.
     * @param input the StandardPattern that serves as input for the visit operation.
     */
    public ArchitectureOperationVisitor(StandardPattern input) {
        this.input = input;
    }

    /**
     * Get the input for the visit operation.
     * @return the input for the visit operation.
     */
    public StandardPattern getInput() {
        return input;
    }

    /**
     * Set the input for the visit operation.
     * @param input the new input for the visit operation.
     */
    public void setInput(StandardPattern input) {
        this.input = input;
    }

    /**
     * Get the output of the visit operation.
     * @return the output of the visit operation.
     */
    public Vector getOutput() {
        return output;
    }

    /**
     * Set the output of the visit operation.
     * @param output the new output of the visit operation.
     */
    public void setOutput(Vector output) {
        this.output = output;
    }
}
