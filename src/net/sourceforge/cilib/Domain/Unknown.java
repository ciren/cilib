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
import java.lang.reflect.Array;
import java.util.Random;

/**
 * @author espeer
 */
public class Unknown extends DomainComponent {

    public static final String PREFIX = "?";
    
    public Unknown() {
    	
    }
    
    public Unknown(String representation) {
        if (! representation.equals(PREFIX)) {
            throw new IllegalArgumentException();
        }
    }
    
    public String getRepresentation() {
        return PREFIX;
    }
    
    protected boolean isInside(Object item, int offset) {
    	try {
    		if (offset == -1 || offset < Array.getLength(item)) {
    			return true;
    		}
    	}
    	catch (IllegalArgumentException ex) { }
    	return false;
    }
    
    protected void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException {
    	if (offset == -1) {
    		oos.writeObject(item);
    	}
    	else {
    		oos.writeObject(Array.get(item, offset));
    	}
    }
    
    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	try {
    		return new Object[] { ois.readObject() };
    	}
    	catch (ClassNotFoundException ex) {
    		return null;
    	}
    }
    
    protected Object getRandomGroup(Random randomiser) {
    	throw new UnsupportedOperationException();
    }
	
	/**
	 * Accepts a DomainVisitor on a set of Unknown node instances.  This simply
	 * casts to the correct type and call DomainVisitor.visitSet().
	 *
	 * @param dv DomainVisitor to use
	 * @param instances The Object instances to use.
	 *
	 * @author jkroon
	 */
	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
		dv.visitUnknown(instances);
	}
}


