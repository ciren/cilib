/*
 * VonNeumannTopology.java
 *
 * Created on January 18, 2003, 10:42 AM
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
 *
 *    
 */

package net.sourceforge.cilib.PSO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implementation of the Von Neumann neighbourhood topology. The Von Neumann topology is 
 * a two dimensional grid of particles with wrap around.
 * </p><p>
 * Refereces:
 * </p><p><ul><li>
 * J. Kennedy and R. Mendes, "Population structure and particle swarm performance,"
 * in Proceedings of the IEEE Congress on Evolutionary Computation,
 * (Honolulu, Hawaii USA), May 2002.
 * </li></ul></p>
 *
 * @author  espeer
 */
public class VonNeumannTopology implements Topology {
    
    /**
     * Creates a new instance of <code>VonNeumannTopology</code>.
     */
    public VonNeumannTopology() {
        particles = new ArrayList();
    }
    
    public Iterator neighbourhood(Iterator iterator) {
        MatrixIterator i = (MatrixIterator) iterator;
        return new VonNeumannNeighbourhoodIterator(this, i);
    }
    
    public Iterator particles() {
        return new VonNeumannTopologyIterator(this);
    }
    
    public void add(Particle particle) {
        int min = particles.size();
        ArrayList shortest = null;
        Iterator i = particles.iterator();
        while (i.hasNext()) {
            ArrayList tmp = (ArrayList) i.next();
            if (tmp.size() < min) {
                shortest = tmp;
                min = tmp.size();
            }
        }
        if (shortest == null) {
            shortest = new ArrayList(particles.size() + 1);
            particles.add(shortest);
        }
        shortest.add(particle);
    }
    
    public int getSize() {
    	int size = 0;
    	Iterator i = particles.iterator();
    	while (i.hasNext()) {
    		size += ((ArrayList) i.next()).size();
    	}
    	return size;
    }
    
    private ArrayList particles;
   
    private interface MatrixIterator extends Iterator {
        public int getRow();
        public int getCol();
    }
    
    private class VonNeumannTopologyIterator implements MatrixIterator {
        
        public VonNeumannTopologyIterator(VonNeumannTopology topology) {
            this.topology = topology;
            row = -1;
            col = 0;
        }
        
        public boolean hasNext() {
            int lastRow = topology.particles.size() - 1;
            int lastCol = ((ArrayList) topology.particles.get(lastRow)).size() - 1;
            return row != lastRow || col != lastCol;
        }
        
        public Object next() {
            int lastRow = topology.particles.size() - 1;
            int lastCol = ((ArrayList) topology.particles.get(lastRow)).size() - 1;
            
            if (row == lastRow && col == lastCol) {
                throw new NoSuchElementException();
            }
            
            ++row;
            if (row > lastRow) {
                row = 0;
                ++col;
            }
            
            return ((ArrayList) topology.particles.get(row)).get(col);
        }
        
        public void remove() {
            if (row == -1) {
                throw new IllegalStateException();
            }
            
            ((ArrayList) topology.particles.get(row)).remove(col);

            --row;
            if (col != 0 && row < 0) {
            	row = topology.particles.size() - 1;
            	--col;
            }
        }
        
        public int getRow() {
            return row;
        }
        
        public int getCol() {
            return col;
        }
        
        private int row;
        private int col;
        private VonNeumannTopology topology;
    }
    
    private class VonNeumannNeighbourhoodIterator implements MatrixIterator {
        
        public VonNeumannNeighbourhoodIterator(VonNeumannTopology topology, MatrixIterator iterator) {
            if (iterator.getRow() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            row = iterator.getRow();
            col = iterator.getCol();
            index = 0;
        }
        
        public boolean hasNext() {
            return (index != 5);
        }
        
        public Object next() {
            if (index == 5) {
                throw new NoSuchElementException();
            }
            
            int lastRow = topology.particles.size() - 1;
            int lastCol = ((ArrayList) topology.particles.get(lastRow)).size() - 1;
            
            row = (row + deltaRow[index] + lastRow) % lastRow;
            col = (col + deltaCol[index] + lastCol) % lastCol;
            
            ++index;
            return ((ArrayList) topology.particles.get(row)).get(col);
        }
        
        public void remove() {
            ((ArrayList) topology.particles.get(row)).remove(col);
        }
        
        public int getRow() {
            return row;
        }
        
        public int getCol() {
            return col;
        }
        
        private int row;
        private int col;
        private int index;
        private VonNeumannTopology topology;
        private int[] deltaRow = {0, -1, 1,  1, -1};
        private int[] deltaCol = {0,  0, 1, -1, -1};
    }
}
