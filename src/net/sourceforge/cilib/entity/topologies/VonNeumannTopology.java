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

package net.sourceforge.cilib.entity.topologies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.particle.Particle;

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
 * @author  Edwin Peer
 */
public class VonNeumannTopology extends Topology<Particle> {
    
    /**
     * Creates a new instance of <code>VonNeumannTopology</code>.
     */
    public VonNeumannTopology() {
        particles = new ArrayList<ArrayList<Particle>>();
        lastRow = 0;
        lastCol = -1;
    }
    
    public Iterator<Particle> neighbourhood(Iterator<Particle> iterator) {
        MatrixIterator i = (MatrixIterator) iterator;
        return new VonNeumannNeighbourhoodIterator(this, i);
    }
    

    public Iterator<Particle> iterator() {
        return new VonNeumannTopologyIterator(this);
    }
    
    public boolean add(Particle particle) {
    	int min = particles.size();
        ArrayList<Particle> shortest = null;
        for (ArrayList<Particle> tmp : particles) {	
            if (tmp.size() < min) {
                shortest = tmp;
                min = tmp.size();
            }
        }
        if (shortest == null) {
            shortest = new ArrayList<Particle>(particles.size() + 1);
            particles.add(shortest);
        }
        shortest.add(particle);
        
       	lastRow = particles.size() - 1;
        lastCol = ((ArrayList) particles.get(lastRow)).size() - 1;
        
        return true;        
    }
    
    public boolean addAll(Collection<? extends Particle> set) {
    	for (Iterator<? extends Particle> i = set.iterator(); i.hasNext(); ) {
    		this.add(i.next());
    	}
    	
    	return true;
    }
    
    public int size() {
    	int size = 0;
    	Iterator<ArrayList<Particle>> i = particles.iterator();
    	while (i.hasNext()) {
            size += i.next().size();
    	}
    	return size;
    }
    
    private void remove(int x, int y) {
    	ArrayList<Particle> row = particles.get(x);
        row.remove(y);
        if (row.size() == 0) {
        	particles.remove(x);
        }
       	lastRow = particles.size() - 1;
        lastCol = ((ArrayList) particles.get(lastRow)).size() - 1;        
    }
    
    private ArrayList<ArrayList<Particle>> particles;
    private int lastRow;
    private int lastCol;
    
    private interface MatrixIterator extends Iterator<Particle> {
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
        
        public Particle next() {
            if (row == topology.lastRow && col == topology.lastCol) {
                throw new NoSuchElementException();
            }
            
            ++col;
            if (col >= particles.get(row).size()) {
                ++row;
                col = 0;
            }
            
            return topology.particles.get(row).get(col);
        }
        
        public void remove() {
            if (col == -1) {
                throw new IllegalStateException();
            }
            
            topology.remove(row, col);

            --col;
            if (row != 0 && col < 0) {
            	--row;
            	col = topology.particles.get(row).size() - 1;
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
        
        public Particle next() {
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
            return topology.particles.get(row).get(col);
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

    
    
    
    
    
    
    
    
    
    
    
    
    
	public void add(Collection<Particle> set) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public boolean remove(Particle indiv) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public Particle get(int index) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public Particle set(int index, Particle indiv) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public void setAll(Collection<? extends Particle> set) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public Collection<Particle> getAll() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public boolean isEmpty() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public void clear() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public Particle next() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}
	
	
	
	

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public boolean addAll(int index, Collection<? extends Particle> c) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public void add(int index, Particle element) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");		
	}

	public Particle remove(int index) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public int indexOf(Object o) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public ListIterator<Particle> listIterator() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public ListIterator<Particle> listIterator(int index) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public List<Particle> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
			
	}

}
