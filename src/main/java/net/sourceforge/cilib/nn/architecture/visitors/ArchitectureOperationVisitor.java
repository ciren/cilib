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
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * An abstract visitor class implements the {@link ArchitectureVisitor} interface
 * and adds the functionality to the visitor of having an input/output
 * operations where the input is a {@link StandardPattern} and the output is a
 * {@link Vector}
 * @author andrich
 */
public abstract class ArchitectureOperationVisitor implements ArchitectureVisitor {

    protected StandardPattern input;
    protected Vector output;

    /**
     * Default constructor.
     */
    public ArchitectureOperationVisitor() {
    }

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
