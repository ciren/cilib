package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A Decorator pattern class to wrap a normal function to perform Angle Modulation
 * 
 * @author Gary Pampara
 *
 */
public class AngleModulation extends ContinuousFunction {
	private static final long serialVersionUID = -3492262439415251355L;
	private int precision;
	private int requiredBits;
	private double lowerBound;
	private double upperBound;
	private Function function;
	
	/**
	 * 
	 *
	 */
	public AngleModulation() {
		setDomain("R(-1.0,1.0)^4");
		precision = 3;
		requiredBits = 0;
	}
	
	/**
	 * 
	 */
	public Object getMinimum() {
		return function.getMinimum();
	}
	
	/**
	 * 
	 */
	public Object getMaximum() {
		return function.getMaximum();
	}
	
	/**
	 * 
	 */
	public double evaluate(Vector x) {
		String solution = generateBitString(x);
		Vector expandedVector = decodeBitString(solution);
		
		return function.evaluate(expandedVector);
	}
		
	/**
	 * 
	 * @return
	 */
	public int getPrecision() {
		return this.precision;
	}
	
	/**
	 * 
	 * @param precision
	 */
	public void setPrecision(int precision) {
		this.precision = precision;		
	}
	
	/**
	 * 
	 * @return
	 */
	public Function getFunction() {
		return this.function;
	}	
	
	/**
	 * 
	 * @param funciton
	 */
	public void setFunction(Function decoratedFunciton) {
		this.function = decoratedFunciton;
		requiredBits = getRequiredNumberOfBits(function.getDomainRegistry());
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public int getRequiredNumberOfBits(DomainRegistry domain) {
		
		if (domain.getDomainString().contains("B")) {
			return 1;
		}
		else {
			String range = new String(domain.getDomainString());
			
			// now remove all the irrelevant details from the domain provided
			range = range.substring(range.indexOf('(')+1);
			range = range.substring(0, range.indexOf(')'));
			
			String [] bounds = range.split(",");
			
			lowerBound = Double.valueOf(bounds[0]).doubleValue();
			upperBound = Double.valueOf(bounds[1]).doubleValue();
			
			double greaterRange = Math.abs(lowerBound) + Math.abs(upperBound);
			double expandedRange = greaterRange * Math.pow(10, getPrecision());
			
			return Double.valueOf(Math.ceil(Math.log(expandedRange) / Math.log(2.0))).intValue();
		}
		
	}

	/**
	 * @TODO: Change this to use something better than a string
	 * @TODO: complete this method
	 * 
	 * @param x
	 * @return
	 */
	public String generateBitString(Vector x) {
		double a = x.getReal(0);
		double b = x.getReal(1);
		double c = x.getReal(2);
		double d = x.getReal(3);
		
		StringBuffer str = new StringBuffer();
		
		for (int i = 0; i < requiredBits*function.getDimension(); i++) {
			double result =  Math.sin(2*Math.PI*(i-a) * b * Math.cos(2*Math.PI*c*(i-a))) + d;
			
			if (result > 0.0)
				str.append('1');
			else
				str.append('0');
		}
		
		return str.toString();
	}
	
	/**
	 * 
	 * @param bits
	 * @return
	 */
	private Vector decodeBitString(String bits) {
		Vector vector = new MixedVector();
		
		for (int i = 0; i < bits.length(); ) {
			double tmp = valueOf(bits, i, i+requiredBits);
			tmp = transform(tmp);

			vector.append(new Real(tmp));
			i += requiredBits;
		}
		
		return vector;
	}
	
	/**
	 * Determine the numeric value of the given bitstring.
	 * 
	 * TODO: Move this into a class that will make sense.
	 * 
	 * @param bitString The bitsting as a string
	 * @param i The starting index
	 * @param j The ending index
	 * @return The value of the bitstring
	 */
	public double valueOf(String bitString, int startIndex, int endIndex) {
		double result = 0.0;
		
		String substring = bitString.substring(startIndex, endIndex);
		result = Integer.valueOf(substring, 2).intValue();
		
		return result;
	}
	
	public double valueOf(String bitString, int index) {
		String substring = bitString.substring(index);
		return Integer.valueOf(substring, 2).intValue();
	}
		
	public double valueOf(String bitString) {
		return Integer.valueOf(bitString, 2).intValue();
	}
	
	/**
	 * 
	 * @param number
	 * @return
	 */
	private double transform(double number) {
		double result = number;

		int tmp = 1;
		tmp <<= this.requiredBits-1;
		result -= tmp;
		result /= Math.pow(10, getPrecision());

		return result;
	}

}
