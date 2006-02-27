/*
 * GBestTopology.java
 *
 * Created on January 17, 2003, 8:07 PM
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
 * This class implements the gbest neighbourhood topology, reference:
 *      1.  R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 *          chapter 6, pp. 212-226. Academic Press Professional, 1996.
 */

package net.sourceforge.cilib.PSO;

import java.util.*;
import net.sourceforge.cilib.Algorithm.*;

/**
 *
 * @author  espeer
 */
public class GBestTopology implements Topology {
    
    public GBestTopology() {
        size = 20;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getSize() {
        return size;
    }
    
    public Iterator particles() {
        return new GBestTopologyIterator(this);
    }
    
    public Iterator neighbourhood(Iterator iterator) {
        return new GBestTopologyIterator(this);
    }
  
    public void initialise(Class particleClass) {
        particles = new Particle[size];
        for (int i = 0; i < size; ++i) {
            try {
                particles[i] = (Particle) particleClass.newInstance();
            }
            catch (Exception e) {
                throw new InitialisationException("Could not instantiate particle");
            }
        }
    }
    
    private int size;
    Particle[] particles;

    protected interface ArrayIterator extends Iterator {
        public int getIndex();
    }

    private class GBestTopologyIterator implements ArrayIterator {
  
        public GBestTopologyIterator(GBestTopology topology) {
            this.topology = topology;
            index = -1;
            lastIndex = topology.size - 1;
        }
        
        public int getIndex() {
            return index;
        }
        
        public boolean hasNext() {
            return (index != lastIndex);
        }
        
        public Object next() {
            if (index == lastIndex) {
                throw new NoSuchElementException();
            }
            ++index;
            return topology.particles[index];
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
       
        private int index;
        private int lastIndex;
        private GBestTopology topology;
    }
}
