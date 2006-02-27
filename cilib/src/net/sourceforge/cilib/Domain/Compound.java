/*
 * Compound.java
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




/**
 * This class can be used to represent domains of the form C^N where C is another domain component 
 * and N is an integer representing the dimension of this component. A special case is where N is the 
 * character 'N' which represents an unkown or unbounded dimension.
 * 
 * @author espeer
 */
public class Compound extends Vector {

    public static final int UNBOUNDED_DIMENSION = -1;

    public Compound(int dimension, DomainComponent component) {
        this.dimension = dimension;
        this.component = component;
    }
    
    public Compound(String representation) {
        int caretIndex = representation.lastIndexOf('^');
        if (caretIndex == -1 || caretIndex == representation.length() - 1) {
            throw new IllegalArgumentException();
        }
        
        component = ComponentFactory.instance().newComponent(representation.substring(0, caretIndex));
        
        if (representation.indexOf('N') > caretIndex) {
            dimension = UNBOUNDED_DIMENSION;
        }
        else {
            try {
                dimension = Integer.parseInt(representation.substring(caretIndex + 1, representation.length()));
                if (dimension < 0) {
                    throw new IllegalArgumentException("Invalid dimension");
                }
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid dimension");
            }
        }
    }
    
    public int getDimension() {
    	return dimension * component.getDimension();
    }

    public String getRepresentation() {
        StringBuffer representation = new StringBuffer(component.getRepresentation());
        representation.append("^");
        if (dimension == UNBOUNDED_DIMENSION) {
            representation.append("N");
        }
        else {
            representation.append(String.valueOf(dimension));
        }
        return representation.toString();
    }
    
    public DomainComponent getComponent(int i) {
    	return component.getComponent(i % component.getDimension());
    }
 	
     
    private DomainComponent component;
    private int dimension;

}
