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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import org.nfunk.jep.JEP;

/**
 * A Function class that defines a function based on a predefined string which is
 * parsed and interpreted during evaluation.
 */
public class ExpressionFunction extends ContinuousFunction {
    private static final long serialVersionUID = -7072775317449355858L;
    private JEP parser;
    private String expression;

    /**
     * Create a new instance of the {@linkplain ExpressionFunction}.
     */
    public ExpressionFunction() {
        initialiseParser();
        this.expression = "";
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ExpressionFunction(ExpressionFunction copy) {
        super(copy);
        initialiseParser();
        this.expression = copy.expression;
        this.parser.parseExpression(this.expression);
    }

    protected void initialiseParser() {
        this.parser = new JEP();
        this.parser.addStandardFunctions();
        this.parser.addStandardConstants();
        this.parser.setImplicitMul(true);
        this.parser.setAllowUndeclared(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpressionFunction getClone() {
        return new ExpressionFunction(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        for (int i = 0; i < getDimension(); i++) {
            this.parser.addVariable("x" + Integer.toString(i + 1), input.getReal(i));
        }
        return this.parser.getValue();
    }

    /**
     * Get the defined function.
     * @return The defined function string.
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * Set the parseable function.
     * @param expression The string to parse and set as the function.
     */
    public void setExpression(String expression) {
        this.expression = expression;
        this.parser.parseExpression(this.expression);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj instanceof ExpressionFunction) {
            return this.expression.equals(((ExpressionFunction)obj).expression);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.expression.hashCode();
    }
}
