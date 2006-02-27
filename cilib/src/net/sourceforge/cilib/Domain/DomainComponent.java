/*
 * DomainComponent.java
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
public abstract class DomainComponent {
    
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
    
    public boolean isMixed() {
    	return false;
    }
    
    /**
     * Returns the i'th subcomponent of this domain component.
     * 
     * @param i the index of the subcomponent
     * @return the i'th subcomponent
     */
    public DomainComponent getComponent(int i) {
        return this;
    }
    
    /**
     * Determines if the given object conforms to the specifications of this domain structure.
     * 
     * @param item the object to check.
     * @return true if the given object is an instance of the domain represented by this component.
     */
    public boolean isInside(Object item) {
    	return isInside(item, -1);
    }
    
    /**
     * Returns true if item is inside the domain specified by this component. The offset applies to groups
     * of items. If the offset is -1 then item is treated as a non-vector item.
     * 
     * For example:
     * 
     * The Int component treats the group case as int[] while it treats non-vector types as Integer.
     * The Bit component treats the group case as BitSet and the non-vector case as Byte
     *
     * @param the offset in the group, -1 if item is not a grouping of items
     *
     */
    protected abstract boolean isInside(Object item, int offset);
    
    /**
     * Serialise the given object into an <code>ObjectOutputStream</code>.according to the domain structure.
     * 
     * @param oos the output stream to which the serialised data should be written/ 
     * @param item the object to be serialised.
     * @throws IOException if any IOException occurs during serialisation.
     */
    public  void serialise(ObjectOutputStream oos, Object item) throws IOException {
    	serialise(oos, item, -1);
    }
    
    protected abstract void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException;
    
    /**
     * Deserialise an object from an <code>ObjectInputStream</code> according to the domain structure.
     * 
     * @param ois the input stream from which the data for the object should be read.
     * @return the object that has been deserialised.
     * @throws IOException if any IOException occurs during deserialisation.
     */
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return Array.get(deserialiseGroup(ois), 0);
    }
    
    protected abstract Object deserialiseGroup(ObjectInputStream ois) throws IOException;
    
    public Object getRandom(Random randomiser) {
    	return Array.get(getRandomGroup(randomiser), 0);
    }
    
    protected abstract Object getRandomGroup(Random randomiser);
    
    protected Object mergeGroups(Object a, Object b) {
    	int a_length = Array.getLength(a);
    	int b_length = Array.getLength(b);
    	
    	Object tmp = Array.newInstance(Array.get(a, 0).getClass(), a_length + b_length);
		int i = 0;
		for (; i < a_length; ++i) {
			Array.set(tmp, i, Array.get(a, i));
		}
		for (int j = 0; j < b_length; ++i, ++j) {
			Array.set(tmp, i, Array.get(b, j));
		}
		
		return tmp;
    }

 	/**
 	 * Accepts a DomainVisitor in order to traverse one or more instances of
 	 * this DomainComponent...
 	 *
 	 * @param dv The DomainVisitor that needs to visit all the instances.
 	 * @param instances An array containing all the instances to visit.
 	 *
 	 * @author jkroon
 	 */
 	public abstract void acceptDomainVisitor(DomainVisitor dv, Object []instances);
 
 	/**
 	 * Special case for use by the Compound.acceptDomainVisitor().  This also
 	 * supplies a default implementation that packs stuff into an Object[].
 	 *
 	 * @param dv The DomainVisitor to use.
 	 * @param instances The various instances to make use of.
 	 *
 	 * @author jkroon
 	 */
 	protected void acceptGroupedDomainVisitor(DomainVisitor dv, Object []instances) {
 		// If this throws an ClassCastException it probably means that some
 		// subclass has forgotten to override this method.

 		int numinstances = instances.length;
 		Object[][] objInstances = new Object[numinstances][];
 		for (int i = 0; i < numinstances; ++i) {
 			objInstances[i] = (Object[]) instances[i];
 		}
 		
 		int dimension = objInstances[0].length;
 		Object []passDown = new Object[numinstances];
 		
 		for(int i = 0; i < dimension; i++) {
 			for(int j = 0; j < numinstances; j++) {
 				passDown[j] = objInstances[j][i];
 			}
 			acceptDomainVisitor(dv, passDown);
 		}
 	}
}
