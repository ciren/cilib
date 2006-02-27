/*
 * Int.java
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
import java.util.Random;

/**
 * This class can be used to represent integer valued components.
 * 
 * @author espeer
 */
public class Int extends Quantitative {

    public static final String PREFIX = "Z";
    
    public Int(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    public Int(String representation) {
        if (! representation.startsWith(PREFIX)) {
            throw new IllegalArgumentException();
        }
        
        lowerBound = Integer.MIN_VALUE;
        upperBound = Integer.MAX_VALUE;
        
        int openParenIndex = representation.indexOf('(');
        int closeParenIndex = representation.indexOf(')');
        if (openParenIndex > 0 && closeParenIndex != representation.length() - 1) {
            throw new IllegalArgumentException();
        }
        
        if (openParenIndex > 0 && closeParenIndex > openParenIndex + 1) {
            String pair = representation.substring(openParenIndex + 1, closeParenIndex);
            int commaIndex = pair.indexOf(',');
            if (commaIndex > -1) {
                try {
                    lowerBound = Integer.parseInt(pair.substring(0, commaIndex).trim());
                }
                catch (NumberFormatException ex) {  }
                try {
                    upperBound = Integer.parseInt(pair.substring(commaIndex + 1, pair.length()).trim());
                }
                catch (NumberFormatException ex) { }
            }
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound is larger than upper bound");
        }
    }
    
    public String getRepresentation() {
        StringBuffer representation = new StringBuffer(PREFIX);
        if (lowerBound != Integer.MIN_VALUE || upperBound != Integer.MAX_VALUE) {
            representation.append("(");
            if (lowerBound != Integer.MIN_VALUE) {
                representation.append(String.valueOf(lowerBound));
            }
            representation.append(",");
            if (upperBound != Integer.MAX_VALUE) {
                representation.append(String.valueOf(upperBound));
            }
            representation.append(")");
        }
        
        return representation.toString();
    }

    public Number getLowerBound() {
        return new Integer(lowerBound);
    }

    public Number getUpperBound() {
        return new Integer(upperBound);
    }
    
    protected boolean isInside(Object item, int offset) {
    	int value = 0;
    	if (offset == -1) {
    		if (item instanceof Integer) {
    			value = ((Integer) item).intValue();
    		}
    		else {
    			return false;
    		}
    	}
    	else if (item instanceof int[]) {
    		value = ((int[]) item)[offset];
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
    		oos.writeInt(((Integer) item).intValue());
    	}
    	else {
    		oos.writeInt(((int[]) item)[offset]);
    	}
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return new Integer(ois.readInt());
    }
    
    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	return new int[] { ois.readInt() };
    }
   
	protected Object getRandomGroup(Random randomiser) {
		int tmp[] = new int[1];
		if (lowerBound == Integer.MIN_VALUE || upperBound == Integer.MAX_VALUE) {
			do {
				tmp[0] = randomiser.nextInt();
			} while (tmp[0] < lowerBound || tmp[0] > upperBound);
		}
		else {
			tmp[0] = randomiser.nextInt(upperBound - lowerBound + 1) + lowerBound;
		}
		return tmp;
	}
	
	protected Object mergeGroups(Object _a, Object _b) {
		int[] a = (int[]) _a;
		int[] b = (int[]) _b;
		int[] tmp = new int[a.length + b.length];
		
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
	 * Accepts a set of instances of the Integer variation.  This function
	 * will only get called with the Z case of the domain, Z^?? is
	 * handled by the protected variation for use by Compound.
	 *
	 * @param dv The DomainVisitor to use.
	 * @param instances The Integer instances, will get passed down as Object[]
	 * 		for the generic case.
	 *
	 * @author jkroon
	 */
	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
		Integer[] intInstances = new Integer[instances.length];
		for (int i = 0; i < instances.length; ++i) {
			intInstances[i] = (Integer) instances[i];
		}

		dv.visitInteger(intInstances);
	}

	/**
	 * Accepts a set of instances of the int[] variation.  This gets passed
	 * down as Object[] for genericness.
	 *
	 * @param dv The DomainVisitor to use.
	 * @param instances The int[] instances to use.
	 *
	 * @author jkroon
	 */
	protected void acceptGroupedDomainVisitor(DomainVisitor dv, Object []instances) {
		int[][] intArrInstances = new int[instances.length][];
		for (int i = 0; i < instances.length; ++i) {
			intArrInstances[i] = (int[]) instances[i];
		}

		dv.visitIntegerArray(intArrInstances);
	}

	private int lowerBound;
    private int upperBound;

}
