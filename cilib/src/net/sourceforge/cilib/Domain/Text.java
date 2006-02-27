/*
 * Text.java
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
public class Text extends Qualitative {

    public static final String PREFIX = "T";
    
    public Text(String representation) {
        if (! representation.equals(PREFIX)) {
            throw new IllegalArgumentException();
        }
    }
    
    public String getRepresentation() {
        return PREFIX;
    }
    
    protected boolean isInside(Object item, int offset) {
    	if (offset == -1) {
    		if (item instanceof String) {
    			return true;
    		}
    		else {
    			return false;
    		}
    	}
    	else if (item instanceof String[]) {
    		String[] tmp = (String[]) item;
    		if (offset >= tmp.length) {
        		throw new ArrayIndexOutOfBoundsException();
    		}
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    protected void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException {
    	if (offset == -1) {
    		oos.writeUTF((String) item);
    	}
    	else {
    		oos.writeUTF(((String[]) item)[offset]);
    	}
    }
    
    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	return new String[] { ois.readUTF() };
    }

    protected Object getRandomGroup(Random randomiser) {
    	int length = randomiser.nextInt(255);
		StringBuffer buffer = new StringBuffer(length);
		for (int i = 0; i < length; ++i) {
			buffer.append(characterSet[randomiser.nextInt(characterSet.length)]);
		}
		return new String[] { buffer.toString() };
    }
 	
	/**
 	 * Accepts a DomainVisitor on a set of Text node instances.  This simply
 	 * casts to the correct type and call DomainVisitor.visitSet().
 	 *
 	 * @param dv DomainVisitor to use
 	 * @param instances The String instances to use.
 	 *
 	 * @author jkroon
 	 */
 	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
 		String[] strInstances = new String[instances.length];
 		for (int i = 0; i < instances.length; ++i) {
 			strInstances[i] = (String) instances[i];
 		}
 		
 		dv.visitText(strInstances);
 	}
    
    private static char[] characterSet = { 'A', 'B','C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
																			   '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
}


