/*
 * Vector.java
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
import java.util.ArrayList;
import java.util.Random;

/**
 * @author espeer
 */
public abstract class Vector extends DomainComponent {
    
    public boolean isMixed() {
    	DomainComponent base = getComponent(0);
    	for (int i = 1; i < getDimension(); ++i) {
    		if (! base.getClass().equals(getComponent(i).getClass())) {
    			return true;
    		}
    	}
    	return false;
    }
	
    public void serialise(ObjectOutputStream oos, Object item) throws IOException {
    	int dimension = getDimension();
    	if (dimension == Compound.UNBOUNDED_DIMENSION) {
    		dimension = Array.getLength(item);
    		oos.writeInt(dimension);
    	}

    	if (isMixed()) {
    		Object[] mixed = (Object[]) item;
    		DomainComponent component = getComponent(0);
    		int offset = 0, selector = 0, size = 1;
    		for (int i = 1; i < dimension; ++i) {
    			DomainComponent current = getComponent(i);
    			if (current.getClass().equals(component.getClass())) {
    				++size;
    			}
    			else {
    				serialise(oos, mixed[selector++], size,  offset);
    				offset = i;
    				component = current;
    				size = 1;
    			}
    		}
    		serialise(oos, mixed[selector], size, offset);
    	}
    	else {
    		serialise(oos, item, getDimension(), 0);
    	}
    }
    
    private void serialise(ObjectOutputStream oos, Object item, int size, int offset) throws IOException {
    	for (int i = 0; i < size; ++i) {
       		getComponent(offset + i).serialise(oos, item, i);
    	}
    }
    
    protected void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException {
    	throw new UnsupportedOperationException();
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return deserialiseGroup(ois);
    }

    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	
    	int dimension = getDimension();
    	if (dimension == Compound.UNBOUNDED_DIMENSION) {
    		dimension = ois.readInt();    		
    	}

    	ArrayList contents = new ArrayList();
    	
    	DomainComponent component = getComponent(0);
    	Object instance = component.deserialiseGroup(ois);
    	
    	for (int i = 1; i < dimension; ++i) {
    		DomainComponent current = getComponent(i);
    		if (current.getClass().equals(component.getClass())) {
    			instance = current.mergeGroups(instance, current.deserialiseGroup(ois));
    		}
    		else {
    			contents.add(instance);
    			component = current;
    			instance = current.deserialiseGroup(ois);
    		}
    	}
    	
    	contents.add(instance);
    	
    	if (contents.size() == 1) {
    		return contents.get(0);
    	}
    	else {
    		return contents.toArray();
    	}
    }

    
    public Object getRandom(Random randomiser) {
    	return getRandomGroup(randomiser);
    }
    	
    protected Object getRandomGroup(Random randomiser) {
    	
    	int dimension = getDimension();

    	ArrayList contents = new ArrayList();
    	
    	DomainComponent component = getComponent(0);
    	Object instance = component.getRandomGroup(randomiser);
    	
    	for (int i = 1; i < dimension; ++i) {
    		DomainComponent current = getComponent(i);
    		if (current.getClass().equals(component.getClass())) {
    			instance = current.mergeGroups(instance, current.getRandomGroup(randomiser));
    		}
    		else {
    			contents.add(instance);
    			component = current;
    			instance = current.getRandomGroup(randomiser);
    		}
    	}
    	
    	contents.add(instance);
    	
    	if (contents.size() == 1) {
    		return contents.get(0);
    	}
    	else {
    		return contents.toArray();
    	}
    }
    
    public boolean isInside(Object item) {
    	try {
    		if (isMixed()) {
    			if (! (item instanceof Object[])) {
    				return false;
    			}
    			Object[] mixed = (Object[]) item;
    			DomainComponent component = getComponent(0);
    			int offset = 0, selector = 0, size = 1, dimension = getDimension();
    			for (int i = 1; i < dimension; ++i) {
    				DomainComponent current = getComponent(i);
    				if (current.getClass().equals(component.getClass())) {
    					++size;
    				}
    				else {
    					if (! isInside(mixed[selector++], size, offset)) {
    						return false;
    					}
    					offset = i;
    					component = current;
    					size = 1;
    				}
    			}
    			return isInside(mixed[selector], size, offset);
    		}
    		else {
    			return isInside(item, getDimension(), 0);
    		}
    	}
    	catch (ArrayIndexOutOfBoundsException ex) {
    		return false;
    	}
    }
    
    private boolean isInside(Object item, int size, int offset) {
    	for (int i = 0; i < size; ++i) {
    		if (! getComponent(offset + i).isInside(item, i)) {
    			return false;
    		}
    	}
    	try {
    		getComponent(offset).isInside(item, size);
        	return false;
    	}
    	catch (ArrayIndexOutOfBoundsException ex) {
    		return true;
    	}
    }

    public boolean isInside(Object item, int offset) {
    	throw new UnsupportedOperationException();
    }

	/**
 	 * Accepts a set of instances of the Composite variation.  This function
 	 * will get called if you have mixed types (the , modifier, for
 	 * example B,R).
 	 * 
 	 * @param dv The DomainVisitor to use.
 	 * @param instances The instances, will get passed down to
 	 * acceptGroupedDomainVisitor().
 	 *
 	 * @author jkroon
 	 */
 	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
		if(isMixed()) {
			int numinstances = instances.length;
			
			Object[][]arrInstances = new Object[numinstances][];
			for (int i = 0; i < numinstances; ++i) {
				arrInstances[i] = (Object[]) instances[i];
			}
			
			Object []passdown = new Object[numinstances];
			
			int selector = 0;
			
			Class class_pdc = DomainComponent.class;
			int dim = getDimension();

			
			for(int i = 0; i < dim; i++) {
				DomainComponent dc = getComponent(i);
				Class class_cdc = dc.getClass();

				if(!class_cdc.equals(class_pdc)) {
					class_pdc = class_cdc;
					
					for(int j = 0; j < numinstances; j++) {
						passdown[j] = arrInstances[j][selector];
					}
					++selector;

					dc.acceptGroupedDomainVisitor(dv, passdown);
				}
			}
		}
		else {
			getComponent(0).acceptGroupedDomainVisitor(dv, instances);
		}
 	}

}
