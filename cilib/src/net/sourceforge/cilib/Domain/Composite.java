/*
 * Composite.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is used to represent domains of the form [C, C, ..., C] where C is another domain component.
 * 
 * @author espeer
 */
public class Composite extends Vector {

    public Composite(ArrayList components) {
        this.components = components;
    }
    
    public Composite(String representation) {
        if (representation.charAt(0) != '[' || representation.charAt(representation.length() - 1) != ']') {
            throw new IllegalArgumentException();
        }
        
        components = new ArrayList();
        
        int brackets = 0;
        int startIndex = 1;
        for (int i = startIndex; i < representation.length() - 1; ++i) {
            char current = representation.charAt(i);
            // Commas inside other brackets do not belong to us
            if (current == '(' || current == '{' || current == '[' || current == '<') {
                ++brackets;
            }
             else if (current == ')' || current == '}' || current == ']' || current == '>') {
                --brackets;
            }
            if (brackets == 0 && current == ',') {
                components.add(ComponentFactory.instance().newComponent(representation.substring(startIndex, i)));
                startIndex = i + 1;
            }
        }
        components.add(ComponentFactory.instance().newComponent(representation.substring(startIndex, representation.length() - 1)));
    }
    
    public int getDimension() {
    	int dimension = 0;
    	for (Iterator i = components.iterator(); i.hasNext(); ) {
    		dimension += ((DomainComponent) i.next()).getDimension(); 
    	}
    	return dimension;
    }
    
    public String getRepresentation() {
        StringBuffer representation = new StringBuffer("[");
        for (int i = 0; i < components.size(); ++i) {
            if (i != 0) {
                representation.append(", ");
            }
            representation.append(((DomainComponent) components.get(i)).getRepresentation());
        }
        representation.append("]");
        return representation.toString();
    }
    
    public DomainComponent getComponent(int component) {
    	int total = 0;
    	for (Iterator i = components.iterator(); i.hasNext(); ) {
    		DomainComponent current = (DomainComponent) i.next();
    		int probe = component - total;
    		int dimension = current.getDimension();
    		if (probe < dimension) {
    			return current.getComponent(probe);
    		}
    		total += dimension;
    	}
    	throw new NoSuchElementException();
    }
 	
	/**
 	 * Accepts a set of instances of the Composite variation.  This function
 	 * will get called if you have mixed types (the , modifier, for
 	 * example B,R).
 	 * 
 	 * @param dv The DomainVisitor to use.
 	 * @param instances The Object[] instances, will get passed down as Object[]
 	 * 		for the generic case.
 	 *
 	 * @author jkroon
 	 */
    /*
 	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
 		int numinstances = instances.length;
 		Object[][] objInstances = new Object[numinstances][];
 		for (int i = 0; i < numinstances; ++i) {
 			objInstances[i] = (Object[]) instances[i];
 		}
 		
 		Object []passDown = new Object[numinstances];
 		for(int i = 0; i < components.size(); i++) {
 			for(int j = 0; j < numinstances; j++)
 				passDown[j] = objInstances[j][i];
 			((DomainComponent)components.get(i)).acceptDomainVisitor(dv, passDown);
 		}
 	}
 	*/
  
    private ArrayList components;
}
