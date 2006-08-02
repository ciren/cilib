/*
 * VonNeumannTopology.java
 *
 * Created on January 18, 2003, 10:42 AM
 *
 * 
 * Copyright (C) 2003 - 2006 
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

import net.sourceforge.cilib.entity.Entity;
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
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class VonNeumannTopology<E extends Entity> extends Topology<E> {
	
	private enum Direction { CENTER, NORTH, EAST, SOUTH, WEST, DONE };
	private ArrayList<ArrayList<E>> particles;
    private int lastRow;
    private int lastCol;
    
    /**
     * Creates a new instance of <code>VonNeumannTopology</code>.
     */
    public VonNeumannTopology() {
        particles = new ArrayList<ArrayList<E>>();
        lastRow = 0;
        lastCol = -1;
    }
    
    public Iterator<E> neighbourhood(Iterator<E> iterator) {
        MatrixIterator<E> i = (MatrixIterator<E>) iterator;
        return new VonNeumannNeighbourhoodIterator<E>(this, i);
    }
    

    public Iterator<E> iterator() {
        return new VonNeumannTopologyIterator<E>(this);
    }
    
    public boolean add(E particle) {
    	int min = particles.size();
        ArrayList<E> shortest = null;
        for (ArrayList<E> tmp : particles) {	
            if (tmp.size() < min) {
                shortest = tmp;
                min = tmp.size();
            }
        }
        if (shortest == null) {
            shortest = new ArrayList<E>(particles.size() + 1);
            particles.add(shortest);
        }
        shortest.add(particle);
        
       	lastRow = particles.size() - 1;
        lastCol = ((ArrayList) particles.get(lastRow)).size() - 1;
        
        return true;        
    }
    
    public boolean addAll(Collection<? extends E> set) {
    	this.particles.ensureCapacity(this.particles.size()+set.size());
    	
    	for (Iterator<? extends E> i = set.iterator(); i.hasNext(); ) {
    		this.add(i.next());
    	}
    	
    	return true;
    }
    
    public int size() {
    	int size = 0;
    	Iterator<ArrayList<E>> i = particles.iterator();
    	while (i.hasNext()) {
            size += i.next().size();
    	}
    	return size;
    }
    
    private void remove(int x, int y) {
    	ArrayList<E> row = particles.get(x);
        row.remove(y);
        if (row.size() == 0) {
        	particles.remove(x);
        }
       	lastRow = particles.size() - 1;
        lastCol = ((ArrayList) particles.get(lastRow)).size() - 1;        
    }
    
    
    
    private interface MatrixIterator<T extends Entity> extends Iterator<T> {
        public int getRow();
        public int getCol();
    }
    
    private class VonNeumannTopologyIterator<T extends Entity> implements MatrixIterator<T> {
    	
        private int row;
        private int col;
        private VonNeumannTopology<T> topology;
        
        public VonNeumannTopologyIterator(VonNeumannTopology<T> topology) {
            this.topology = topology;
            row = 0;
            col = -1;
        }
        
        public boolean hasNext() {
        	return row != topology.lastRow || col != topology.lastCol;
        }
        
        public T next() {
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
        

    }
    
    private class VonNeumannNeighbourhoodIterator<T extends Entity> implements MatrixIterator<T> {
    	
    	private int x;
    	private int y;
    	private int row;
    	private int col;
    	private Direction index;
    	private VonNeumannTopology<T> topology;
        
        
    	public VonNeumannNeighbourhoodIterator(VonNeumannTopology<T> topology, MatrixIterator iterator) {
    		if (iterator.getCol() == -1) {
    			throw new IllegalStateException();
    		}	
    		this.topology = topology;
    		row = x = iterator.getRow();
    		col = y = iterator.getCol();
    		index = Direction.CENTER;
    	}
        
        public boolean hasNext() {
            return (index != Direction.DONE);
        }
        
        public T next() {
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
            
            index = Direction.values()[index.ordinal()+1];
            return topology.particles.get(row).get(col);
        }
        
        public void remove() {
        	topology.remove(row, col);
        	if (index == Direction.CENTER) {
        		index = Direction.DONE;
        	}
        }
        
        public int getRow() {
            return row;
        }
        
        public int getCol() {
            return col;
        }
        
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
	public void add(Collection<Particle> set) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public boolean remove(E indiv) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public E get(int index) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public E set(int index, E indiv) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public void setAll(Collection<? extends E> set) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}


	public List<E> getAll() {
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
		return this.particles.hashCode() + this.lastCol*6 + this.lastRow*8;
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

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public void add(int index, E element) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");		
	}

	public E remove(int index) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public int indexOf(Object o) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
	}

	public List<E> subList(int fromIndex, int toIndex) {
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
