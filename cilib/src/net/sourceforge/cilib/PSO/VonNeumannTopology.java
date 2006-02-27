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
public class VonNeumannTopology extends Topology {
    
    /**
     * Creates a new instance of <code>VonNeumannTopology</code>.
     */
    public VonNeumannTopology() {
        particles = new ArrayList();
        lastRow = 0;
        lastCol = -1;
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
        
       	lastRow = particles.size() - 1;
        lastCol = ((ArrayList) particles.get(lastRow)).size() - 1;
        
    }
    
    public int getSize() {
    	int size = 0;
    	Iterator i = particles.iterator();
    	while (i.hasNext()) {
    		size += ((ArrayList) i.next()).size();
    	}
    	return size;
    }
    
    private void remove(int x, int y) {
    	ArrayList row = (ArrayList) particles.get(x);
        row.remove(y);
        if (row.size() == 0) {
        	particles.remove(x);
        }
       	lastRow = particles.size() - 1;
        lastCol = ((ArrayList) particles.get(lastRow)).size() - 1;        
    }
    
    private ArrayList particles;
    private int lastRow;
    private int lastCol;
    
    private interface MatrixIterator extends Iterator {
        public int getRow();
        public int getCol();
    }
    
    private class VonNeumannTopologyIterator implements MatrixIterator {
        
        public VonNeumannTopologyIterator(VonNeumannTopology topology) {
            this.topology = topology;
            row = 0;
            col = -1;
        }
        
        public boolean hasNext() {
        	return row != topology.lastRow || col != topology.lastCol;
        }
        
        public Object next() {
            if (row == topology.lastRow && col == topology.lastCol) {
                throw new NoSuchElementException();
            }
            
            ++col;
            if (col >= ((ArrayList) particles.get(row)).size()) {
                ++row;
                col = 0;
            }
            
            return ((ArrayList) topology.particles.get(row)).get(col);
        }
        
        public void remove() {
            if (col == -1) {
                throw new IllegalStateException();
            }
            
            topology.remove(row, col);

            --col;
            if (row != 0 && col < 0) {
            	--row;
            	col = ((ArrayList) topology.particles.get(row)).size() - 1;
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
            if (iterator.getCol() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            row = x = iterator.getRow();
            col = y = iterator.getCol();
            index = CENTER;
        }
        
        public boolean hasNext() {
            return (index != DONE);
        }
        
        public Object next() {
            switch (index) {
            	case CENTER: { 
            		row = x; 
            		col = y; 
            		break;
            	}
            	
            	case NORTH: {
            		row = x - 1;
            		col = y;
            		while (true) {
            			if (row < 0) {
            				row = topology.particles.size() - 1;
            			}
            			if (col < ((ArrayList) topology.particles.get(row)).size()) {
            				break;
            			}
            			--row;
            		}
            		break;
            	}
            	
            	case EAST: {
            		row = x;
            		col = y + 1;
            		if (col >= ((ArrayList) topology.particles.get(row)).size()) {
            			col = 0;
            		}
            		break;
            	}
            	
            	case SOUTH: {
            		row = x + 1;
            		col = y;
            		while (true) {
            			if (row >= topology.particles.size()) {
            				row = 0;
            			}
            			if (col < ((ArrayList) topology.particles.get(row)).size()) {
            				break;
            			}
            			++row;
            		}
            		break;
            	}
            	
            	case WEST: {
            		row = x;
            		col = y - 1;
            		if (col < 0) {
            			col = ((ArrayList) topology.particles.get(row)).size() - 1;
            		}
            		break;
            	}
            	
            	default: throw new NoSuchElementException();
            }
            
            ++index;
            return ((ArrayList) topology.particles.get(row)).get(col);
        }
        
        public void remove() {
        	topology.remove(row, col);
        	if (index == CENTER) {
        		index = DONE;
        	}
        }
        
        public int getRow() {
            return row;
        }
        
        public int getCol() {
            return col;
        }
        
        private int x;
        private int y;
        private int row;
        private int col;
        private int index;
        private VonNeumannTopology topology;
        
        private static final int CENTER = 0;
        private static final int NORTH = 1;
        private static final int EAST = 2;
        private static final int SOUTH = 3;
        private static final int WEST = 4;
        private static final int DONE = 5;
    }
}
