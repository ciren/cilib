/*
 * Unknown.java
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
 * @author espeer
 */
public class Unknown extends Qualitative {

    public static final String PREFIX = "?";
    
    public Unknown() {
    	
    }
    
    public Unknown(String representation) {
        if (! representation.equals(PREFIX)) {
            throw new IllegalArgumentException();
        }
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Component#getRepresentation()
     */
    public String getRepresentation() {
        return PREFIX;
    }
    
    public boolean isInDomain(Object item) {
    	return true;
    }
    
    public void serialise(ObjectOutputStream oos, Object item) throws IOException {
    	oos.writeObject(item);
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	try {
    		return ois.readObject();
    	}
    	catch (ClassNotFoundException ex) {
    		return null;
    	}
    }

	public Object getRandom(Random generator) {
		return null;
	}
	
}


