/*
 * LBestTopology.java
 *
 * Created on January 17, 2003, 6:34 PM
 *
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.PSO;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * <p>
 * Implementation of the lbest neighbourhood topology
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @author  espeer
 */
public class LBestTopology extends GBestTopology {
    
    /**
     * Creates a new instance of <code>LBestTopology</code>.
     */
    public LBestTopology() {
        super();
        neighbourhoodSize = 3;
    }
    
    public Iterator neighbourhood(Iterator iterator) {
        return new LBestNeighbourhoodIterator(this, (ArrayIterator) iterator);
    }
    
    /**
     * Sets the number particles in the neighbourhood of each particle. The default is 3.
     *
     * @param neighbourhoodSize The size of the neighbourhood.
     */
    public void setNeighbourhoodSize(int neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }
    
    /**
     * Accessor for the number of particles in a neighbourhood.
     *
     * @return The size of the neighbourhood.
     */
    public int getNeighbourhoodSize() {
    	if (super.getSize() == 0) { // to show a sensible default value in CiClops
    		return neighbourhoodSize;
    	}
        else if (neighbourhoodSize > super.getSize()) {
            return super.getSize();
        }
        else {
            return neighbourhoodSize;
        }
    }
    
    private int neighbourhoodSize;
    
    private class LBestNeighbourhoodIterator implements ArrayIterator {
        
        public LBestNeighbourhoodIterator(LBestTopology topology, ArrayIterator iterator) {
            if (iterator.getIndex() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            index = iterator.getIndex() - (topology.getNeighbourhoodSize() / 2) - 1;
            if (index < 0) {
                index += topology.getSize();
            }
            count = 0;
        }
        
        public int getIndex() {
            return index;
        }
        
        public boolean hasNext() {
            return (count != topology.getNeighbourhoodSize());
        }
        
        public Object next() {
            if (count == topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
            ++index;
            ++count;
            if (index == topology.getSize()) {
               index = 0; 
            }
            return topology.particles.get(index);
        }
        
        public void remove() {
            topology.particles.remove(index);
            --index;
            if (index < 0) {
            	index += topology.getSize();
            }
        }
        
        private LBestTopology topology;
        private int index;
        private int count;
    }
}
