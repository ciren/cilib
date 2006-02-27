/*
 * LBestTopology.java
 *
 * Created on January 17, 2003, 6:34 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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
 *
 * This class implements the lbest neighbourhood topology, reference:
 *      1.  R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 *          chapter 6, pp. 212-226. Academic Press Professional, 1996.
 *   
 */

package net.sourceforge.cilib.PSO;

import java.lang.*;
import java.util.*;

/**
 *
 * @author  espeer
 */
public class LBestTopology extends GBestTopology {
    
    public LBestTopology() {
        super();
        neighbourhoodSize = -1;
    }
    
    public Iterator neighbourhood(Iterator iterator) {
        ArrayIterator i = (ArrayIterator) iterator;
        return new LBestNeighbourhoodIterator(this, i);
    }
    
    public void initialise(Class particleClass) {
        super.initialise(particleClass);
        if (neighbourhoodSize == -1) {
            if (getSize() < 3) {
                neighbourhoodSize = getSize();
            }
            else {
                neighbourhoodSize = 3;
            }
        }
    }
    
    public void setNeighbourhoodSize(int neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }
    
    public int getNeighbourhoodSize() {
        return neighbourhoodSize;
    }
    
    private int neighbourhoodSize;
    
    private class LBestNeighbourhoodIterator implements ArrayIterator {
        
        public LBestNeighbourhoodIterator(LBestTopology topology, ArrayIterator iterator) {
            if (iterator.getIndex() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            index = iterator.getIndex() - (topology.neighbourhoodSize / 2) - 1;
            if (index < 0) {
                index += topology.getSize();
            }
            count = 0;
        }
        
        public int getIndex() {
            return index;
        }
        
        public boolean hasNext() {
            return (count != topology.neighbourhoodSize);
        }
        
        public Object next() {
            if (count == topology.neighbourhoodSize) {
                throw new NoSuchElementException();
            }
            ++index;
            ++count;
            if (index == topology.getSize()) {
               index = 0; 
            }
            return topology.particles[index];
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private LBestTopology topology;
        private int index;
        private int count;
    }
}
