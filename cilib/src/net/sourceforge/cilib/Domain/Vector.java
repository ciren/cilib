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
import java.util.Random;

/**
 * @author espeer
 */
public abstract class Vector extends Component {
    public abstract Component getComponent(int i);
    
    public boolean isInDomain(Object item) {
    	if (item.getClass().isArray()) {
    		if (getDimension() != Compound.UNBOUNDED_DIMENSION && getDimension() != Array.getLength(item)) {
    			return false;
    		}
    		for (int i = 0; i < Array.getLength(item); ++i) {
    			if (! getComponent(i).isInDomain(Array.get(item, i))) {
    				return false;
    			}
    		}
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    public void serialise(ObjectOutputStream oos, Object item) throws IOException {
    	int dimension = getDimension();
    	if (dimension == Compound.UNBOUNDED_DIMENSION) {
    		dimension = Array.getLength(item);
    		oos.writeInt(dimension);
    	}
    	for (int i = 0; i < dimension; ++i) {
    		getComponent(i).serialise(oos, Array.get(item, i));
    	}
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	int dimension = getDimension();
    	if (dimension == Compound.UNBOUNDED_DIMENSION) {
    		dimension = ois.readInt();    		
    	}
    	Object item = Array.newInstance(Object.class, dimension);
    	for (int i = 0; i < dimension; ++i) {
    		Array.set(item, i, getComponent(i).deserialise(ois));
    	}
    	return item;
    }
    
    public Object getRandom(Random generator) {
    	int dimension = getDimension();
    	if (dimension == Compound.UNBOUNDED_DIMENSION) {
    		dimension = generator.nextInt(255);
    	}
    	Object item = Array.newInstance(Object.class, dimension);
    	for (int i = 0; i < dimension; ++i) {
    		Array.set(item, i, getComponent(i).getRandom(generator));
    	}
    	return item;
    }

}
