/*
 * Copyright (C) 2003 - 2008
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Function class that defines a function based on a predefined string which is
 * parsed and interpreted during evaluation.
 */
public class ExpressionFunction extends ContinuousFunction {
	private static final long serialVersionUID = -7072775317449355858L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	private JEP parser;
	private String function;

	/**
	 * Create a new instance of the {@linkplain ExpressionFunction}.
	 */
	public ExpressionFunction() {
		parser = new JEP();
	}

	/**
	 * Copy constructor. Create a copy of the provided instance.
	 * @param copy The instance to copy.
	 */
	public ExpressionFunction(ExpressionFunction copy) {
		this.function = copy.function;
	}

	/**
	 * {@inheritDoc}
	 */
	public ExpressionFunction getClone() {
		return new ExpressionFunction(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double evaluate(Vector x) {
		double result = 0;

		for (int i = 0; i < x.getDimension(); i++) {
			logger.debug("Parameter value: " + x.getReal(i));
			parser.addVariable("x", x.getReal(i));
			logger.debug("Parser value: " + parser.getValue());
			result += parser.getValue();

			logger.debug("hasError? : " + parser.getErrorInfo());
		}

		return result;
	}

	/**
	 * Get the defined function.
	 * @return The defined function string.
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Set the parseable function.
	 * @param function The string to parse and set as the function.
	 */
	public void setFunction(String function) {
		System.out.println("Setting function: " + function);
		this.function = function;

		this.parser.addVariable("x", 0);
		this.parser.addStandardFunctions();
		this.parser.addStandardConstants();

		this.parser.parseExpression(function);
	}

}
