/*
 * Bit.java 
 * 
 * Created on Apr 17, 2004
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
 * @author espeer
 */
public class Bit extends Quantitative {

    public static final String PREFIX = "B";
    
    public Bit(String representation) {
        if (! representation.equals(PREFIX)) {
            throw new IllegalArgumentException();
        }
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Quantitative#getLowerBound()
     */
    public Number getLowerBound() {
        return new Byte((byte) 0);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Quantitative#getUpperBound()
     */
    public Number getUpperBound() {
        return new Byte((byte) 1);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Component#getRepresentation()
     */
    public String getRepresentation() {
        return PREFIX;
    }

    public boolean isInDomain(Object item) {
    	if (item instanceof Byte) {
    		byte value = ((Byte) item).byteValue();
    		if (value == (byte) 0 || value == (byte) 1) {
    			return true;
    		}
    		else {
    			return false;
    		}
    	}
    	else {
    		return false;
    	}
    }
    
    public void serialise(ObjectOutputStream oos, Object item) throws IOException {
    	oos.writeByte(((Byte) item).byteValue());
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return new Byte(ois.readByte());
    }

    public Object getRandom(Random generator) {
    	if (generator.nextBoolean()) {
    		return new Byte((byte) 1);
    	}
    	else {
    		return new Byte((byte) 0);
    	}
    }

}
