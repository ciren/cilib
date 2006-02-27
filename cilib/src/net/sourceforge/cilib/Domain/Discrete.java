/*
 * Discrete.java
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
public class Discrete extends Quantitative {

    public static final String PREFIX = "Z";
    
    public Discrete(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    public Discrete(String representation) {
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
                    lowerBound = Integer.parseInt(pair.substring(0, commaIndex));
                }
                catch (NumberFormatException ex) {  }
                try {
                    upperBound = Integer.parseInt(pair.substring(commaIndex + 1, pair.length()));
                }
                catch (NumberFormatException ex) {  }
            }
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound is larger than upper bound");
        }
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Component#getRepresentation()
     */
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

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Quantitative#getLowerBound()
     */
    public Number getLowerBound() {
        return new Integer(lowerBound);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Quantitative#getUpperBound()
     */
    public Number getUpperBound() {
        return new Integer(upperBound);
    }
    
    public boolean isInDomain(Object item) {
    	if (item instanceof Integer) {
    		int value = ((Integer) item).intValue();
    		if (value < lowerBound || value > upperBound) {
    			return false;
    		}
    		else {
    			return true;
    		}
    	}
    	else {
    		return false;
    	}
    }
    
    public void serialise(ObjectOutputStream oos, Object item) throws IOException {
    	oos.writeInt(((Integer) item).intValue());
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return new Integer(ois.readInt());
    }
    
	public Object getRandom(Random generator) {
		return new Integer(generator.nextInt(upperBound - lowerBound + 1) + lowerBound);
	}

	private int lowerBound;
    private int upperBound;

    
}
