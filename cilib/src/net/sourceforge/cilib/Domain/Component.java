/*
 * Component.java
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
 * A composite structure of  <code>Component</code> classes describe the legal values for an object
 * that is a member of a domain. Domain components can be used to specify the search space of optimisation 
 * problems. 
 * <p />
 * A domain component can be used to check whether an object falls within the specified search space (as defined by 
 * the composite structure) using {@link #isInDomain(Object)}. The domain can further be used to serialise and deserialise 
 * objects that conform to the domain specifications in an optimal way. In addition, the component structure can
 * be used to generate random objects that fall within the search space specified by the domain.
 * <p />
 * Domain component structures can be created from a string representation for the domain using the {@link ComponentFactory} singleton.
 * <p />
 * This class plays the role of the base Component type in the composite design pattern (GoF).
 *  
 * @author espeer
 */
public abstract class Component {
    
	/**
	 * Returns the string representation for this domain component. 
	 * 
	 * @return the string representation for this domain component
	 */
    public abstract String getRepresentation();
    
    /**
     * Returns the dimension of this domain component.
     * 
     * @return the dimension of this domain component.
     */
    public int getDimension() {
        return 1;
    }
    
    /**
     * Returns the i'th subcomponent of this domain component.
     * 
     * @param i the index of the subcomponent
     * @return the i'th subcomponent
     */
    public Component getComponent(int i) {
        return this;
    }
    
    /**
     * Determines if the given object conforms to the specifications of this domain structure.
     * 
     * @param item the object to check.
     * @return true if the given object is an instance of the domain represented by this component.
     */
    public abstract boolean isInDomain(Object item);
    
    /**
     * Serialise the given object into an <code>ObjectOutputStream</code>.according to the domain structure.
     * 
     * @param oos the output stream to which the serialised data should be written/ 
     * @param item the object to be serialised.
     * @throws IOException if any IOException occurs during serialisation.
     */
    public abstract void serialise(ObjectOutputStream oos, Object item) throws IOException;
    
    /**
     * Deserialise an object from an <code>ObjectInputStream</code> according to the domain structure.
     * 
     * @param ois the input stream from which the data for the object should be read.
     * @return the object that has been deserialised.
     * @throws IOException if any IOException occurs during deserialisation.
     */
    public abstract Object deserialise(ObjectInputStream ois) throws IOException;
    
    /**
     * Generates a random object that conforms to the domain described by this component structure.
     * 
     * @param generator The randomizer to be used.
     * @return an instance of the domain describe by this component.
     */
    public abstract Object getRandom(Random generator);
    
}
