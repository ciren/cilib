/*
 * Set.java
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 * This class can be used to represent set valued domains. Sets have the form S{element1, element2, ... element3} 
 * where the elements are just strings.
 * 
 * @author espeer
 */
public class Set extends Qualitative {

    public static final String PREFIX = "S";
    
    public Set(String representation) {
        if (! representation.startsWith(PREFIX)) {
            throw new IllegalArgumentException();
        }
        
        elements = new LinkedHashSet();
        
        int openBracIndex = representation.indexOf('{');
        int closeBracIndex = representation.indexOf('}');
        if (openBracIndex > 0 && closeBracIndex != representation.length() - 1) {
            throw new IllegalArgumentException();
        }
        

        if (openBracIndex > 0 && closeBracIndex > openBracIndex + 1) {
            StringBuffer contents = new StringBuffer(representation.substring(openBracIndex + 1, closeBracIndex));
            int startIndex = 0;
            for (int i = 0; i < contents.length(); ++i) {
                if (contents.charAt(i) == '\\') {
                    contents.deleteCharAt(i++);
                    continue;
                }
                if (contents.charAt(i) == ',') {
                    elements.add(contents.substring(startIndex, i));
                    startIndex = i + 1;
                }
            }
            elements.add(contents.substring(startIndex, contents.length()));
        }
    }
    
    public String getRepresentation() {
        StringBuffer representation = new StringBuffer(PREFIX);
        representation.append("{");
        for (Iterator i = elements.iterator(); i.hasNext(); ) {
            representation.append(escape(i.next().toString()));
            if (i.hasNext()) {
                representation.append(",");
            }
        }
        representation.append("}");
        return representation.toString();
    }
    
    private String escape(String s) {
        StringBuffer tmp = new StringBuffer(s);
        for (int i = 0; i < tmp.length(); ++i) {
            if (tmp.charAt(i) == ',' || tmp.charAt(i) == '\\') {
                tmp.insert(i++, '\\');
            }
        }
        return tmp.toString();
    }
    
    public java.util.Set getLegalElements() {
        return elements;
    }

    protected boolean isInside(Object item, int offset) {
    	java.util.Set set = null;
    	if (offset == -1 )
    		if (item instanceof java.util.Set) {
    			set = (java.util.Set) item;
    		}
    		else {
    			return false;
    	}
    	else if (item instanceof java.util.Set[]) {
    		set = ((java.util.Set[]) item)[offset];
    	}
    	else {
    		return false;
    	}
    	Iterator i = set.iterator();
    	while (i.hasNext()) {
    		if (! elements.contains(i.next())) {
    			return false;
    		}
    	}
    	return true;
    }
    
    protected void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException {
    	java.util.Set set = null;
    	if (offset == -1) {
    		set = (java.util.Set) item;
    	}
    	else {
    		set = ((java.util.Set[]) item)[offset];
    	}
    	
    	oos.writeShort(set.size());
    	
    	Iterator i = elements.iterator();
    	for (int id = 0; i.hasNext(); ++id) {
    		if (set.contains(i.next())) {
    			oos.writeShort(id);
    		}
    	}
    }
    
    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	java.util.Set set = new HashSet();
    	int size = ois.readShort();
    	
    	int current = 0;
    	Iterator i = elements.iterator();
    	for (int count = 0; count < size; ++count) {
    		int id = ois.readShort();
    		String element = (String) i.next();
    		while (current < id) {
    			element = (String) i.next();
    			++current;
    		}
    		set.add(element);
    	}
    	
    	return new java.util.Set[] { set };
    }
    
    protected Object getRandomGroup(Random randomiser) {
       	Object[] sample  = elements.toArray();
		for (int i = 0; i < sample.length; ++i) {
			int tmpIndex = randomiser.nextInt(sample.length);
			Object tmp = sample[i];
			sample[i] = sample[tmpIndex];
			sample[tmpIndex] = tmp;
		}
		java.util.Set set = new HashSet();
		int size = randomiser.nextInt(elements.size());
		for (int i = 0; i < size; ++i) {
			set.add(sample[i]);
		}
		return new java.util.Set[] { set };
    }
    
    private java.util.Set elements;

	/**
	 * Accepts a DomainVisitor on a set of Set node instances.  This simply
	 * casts to the correct type and call DomainVisitor.visitSet().
	 *
	 * @param dv DomainVisitor to use
	 * @param instances The java.util.Set instances to use.
	 *
	 * @author jkroon
	 */
	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
		java.util.Set[] setInstances = new java.util.Set[instances.length];
		for (int i = 0; i < instances.length; ++i) {
			setInstances[i] = (java.util.Set) instances[i];
		}
		
		dv.visitSet(setInstances);
	}
}
