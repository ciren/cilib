/*
 * Real.java
 * 
 * Created on Apr 14, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.Domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Random;

/**
 * This class can be used to represent continuous valued components.
 * 
 * @author espeer
 */
public class Real extends Quantitative {

    public static final String PREFIX = "R";

    public Real(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    public Real(String representation) {
        if (! representation.startsWith(PREFIX)) {
            throw new IllegalArgumentException();
        }
        
        lowerBound = Double.NEGATIVE_INFINITY;
        upperBound = Double.POSITIVE_INFINITY;
        
        int openParenIndex = representation.indexOf('(');
        int closeParenIndex = representation.indexOf(')');
        if (openParenIndex > 0 && closeParenIndex != representation.length() - 1) {
            throw new IllegalArgumentException("No closing parenthesis");
        }
        
        if (openParenIndex > 0 && closeParenIndex > openParenIndex + 1) {
            String pair = representation.substring(openParenIndex + 1, closeParenIndex);
            int commaIndex = pair.indexOf(',');
            if (commaIndex > -1) {
                try {
                    lowerBound = Double.parseDouble(pair.substring(0, commaIndex).trim());
                }
                catch (NumberFormatException ex) {  }
                try {
                    upperBound = Double.parseDouble(pair.substring(commaIndex + 1, pair.length()).trim());
                }
                catch (NumberFormatException ex) {  }
            }
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound is larger than upper bound");
        }
    }
    
    public String getRepresentation() {
        StringBuffer representation = new StringBuffer(PREFIX);
        if (lowerBound != Double.NEGATIVE_INFINITY || upperBound != Double.POSITIVE_INFINITY) {
            representation.append("(");
            if (lowerBound != Double.NEGATIVE_INFINITY) {
                representation.append(String.valueOf(lowerBound));
            }
            representation.append(",");
            if (upperBound != Double.POSITIVE_INFINITY) {
                representation.append(String.valueOf(upperBound));
            }
            representation.append(")");
        }
        
        return representation.toString();
    }

    public Number getLowerBound() {
        return new Double(lowerBound);
    }

    public Number getUpperBound() {
        return new Double(upperBound);
    }
    
    protected boolean isInside(Object item, int offset) {
    	double value = 0;
    	if (offset == -1) {
    		if (item instanceof Double) {
    			value = ((Double) item).doubleValue();
    		}
    		else {
    			return false;
    		}
    	}
    	else if (item instanceof double[]) {
    		value = ((double[]) item)[offset];
    	}
    	else {
    		return false;
    	}
    	if (value < lowerBound || value > upperBound) {
			return false;
		}
		else {
			return true;
		}
    }
    
    protected void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException {
    	if (offset == -1) {
    		oos.writeDouble(((Double) item).doubleValue());
    	}
    	else {
    		oos.writeDouble(((double[]) item)[offset]);
    	}
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return new Double(ois.readDouble());
    }
    
    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	return new double[] { ois.readDouble() };
    }
    
	protected Object getRandomGroup(Random randomiser) {
		double[] tmp = new double[1];
		if (lowerBound == Double.NEGATIVE_INFINITY || upperBound == Double.POSITIVE_INFINITY) {
			BigDecimal bottom = (lowerBound == Double.NEGATIVE_INFINITY) ? new BigDecimal(- Double.MAX_VALUE) : new BigDecimal(lowerBound);
			BigDecimal top = (upperBound == Double.POSITIVE_INFINITY) ? new BigDecimal(Double.MAX_VALUE) : new BigDecimal(upperBound);
			tmp[0] =  top.subtract(bottom).multiply(new BigDecimal(randomiser.nextDouble())).add(bottom).doubleValue();
		}
		else {
			tmp[0] = (upperBound - lowerBound) * randomiser.nextDouble() + lowerBound;
		}
		return tmp;
	}
	
	protected Object mergeGroups(Object _a, Object _b) {
		double[] a = (double[]) _a;
		double[] b = (double[]) _b;
		double[] tmp = new double[a.length + b.length];
		
		int i = 0;
		for (; i < a.length; ++i) {
			tmp[i] = a[i];
		}
		for (int j = 0; j < b.length; ++i, ++j) {
			tmp[i] = b[j];
		}
		
		return tmp;
	}
 	
	/**
 	 * Accepts a set of instances of the Double variation.  This function
 	 * will only get called with the R case of the domain, R^?? is
 	 * handled by the protected variation for use by Compound.
 	 *
 	 * @param dv The DomainVisitor to use.
 	 * @param instances The Double instances, will get passed down as Object[]
 	 * 		for the generic case.
 	 *
 	 * @author jkroon
 	 */
 	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
 		Double[] doubleInstances = new Double[instances.length];
 		for (int i = 0; i < instances.length; ++i) {
 			doubleInstances[i] = (Double) instances[i];
 		}
 
 		dv.visitReal(doubleInstances);
 	}
 
 	/**
 	 * Accepts a set of instances of the double[] variation.  This gets passed
 	 * down as Object[] for genericness.
 	 *
 	 * @param dv The DomainVisitor to use.
 	 * @param instances The double[] instances to use.
 	 *
 	 * @author jkroon
 	 */
 	protected void acceptGroupedDomainVisitor(DomainVisitor dv, Object []instances) {
 		double[][] doubleArrInstances = new double[instances.length][];
 		for (int i = 0; i < instances.length; ++i) {
 			doubleArrInstances[i] = (double[]) instances[i];
 		}
 		
 		dv.visitRealArray(doubleArrInstances);
 	}
	
    private double lowerBound;
    private double upperBound;
}
