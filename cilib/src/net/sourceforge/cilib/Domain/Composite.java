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
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Component#getDimension()
     */
    public int getDimension() {
        return components.size();
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Component#getRepresentation()
     */
    public String getRepresentation() {
        StringBuffer representation = new StringBuffer("[");
        for (int i = 0; i < components.size(); ++i) {
            if (i != 0) {
                representation.append(", ");
            }
            representation.append(((Component) components.get(i)).getRepresentation());
        }
        representation.append("]");
        return representation.toString();
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Component#getComponent(int)
     */
    public Component getComponent(int i) {
        return (Component) components.get(i);
    }
    
    private ArrayList components;

    
}
