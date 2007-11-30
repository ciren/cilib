package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import org.apache.log4j.Logger;
import org.nfunk.jep.JEP;

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
