/*
 * ExpressionFunction.java
 *
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

import org.apache.log4j.Logger;
import org.nfunk.jep.JEP;

/**
 *
 */
public class ExpressionFunction extends ContinuousFunction {

	private static final long serialVersionUID = -7072775317449355858L;
	private Logger log = Logger.getLogger(getClass());
	private JEP parser;
	private String function;
	
	public ExpressionFunction() {
		parser = new JEP();		
	}
	
	public ExpressionFunction(ExpressionFunction copy) {
		this.function = copy.function;		
	}
	
	public ExpressionFunction getClone() {
		return new ExpressionFunction(this);
	}

	@Override
	public double evaluate(Vector x) {
		double result = 0;
		
		for (int i = 0; i < x.getDimension(); i++) {
			log.debug("Parameter value: " + x.getReal(i));
			parser.addVariable("x", x.getReal(i));
			log.debug("Parser value: " + parser.getValue());
			result += parser.getValue();
			
			log.debug("hasError? : " + parser.getErrorInfo());
		}
		
		return result;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		System.out.println("Setting function: " + function);
		this.function = function;
		
		this.parser.addVariable("x", 0);
		this.parser.addStandardFunctions();
		this.parser.addStandardConstants();
		
		this.parser.parseExpression(function);
	}
	
}
