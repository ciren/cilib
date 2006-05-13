/*
 * GBestTopology.java
 *
 * Created on January 17, 2003, 8:07 PM
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
 *
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

/**
 * <p>
 * Implementation of the gbest neighbourhood topology
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @author Edwin Peer
 */
public class GBestTopology<E extends Entity> extends Topology<E> {
	
	protected ArrayList<E> particles;

    /**
     * Creates a new instance of <code>GBestTopology</code>.
     */
    public GBestTopology() {
        particles = new ArrayList<E>();
    }
    
    public Iterator<E> iterator() {
        return new GBestTopologyIterator<E>(this);
    }

    public Iterator<E> neighbourhood(Iterator<E> iterator) {
        return new GBestTopologyIterator<E>(this);
    }
    
    public boolean add(E particle) {
        return particles.add(particle);
    }
    
    public boolean addAll(Collection<? extends E> set) {
    	return this.particles.addAll(set);
    }

    public int size() {
        return particles.size();
    }

    protected interface ArrayIterator<T extends Entity> extends Iterator<T> {
        public int getIndex();
    }

    private class GBestTopologyIterator<T extends Entity> implements ArrayIterator<T> {

    	public GBestTopologyIterator(GBestTopology<T> topology) {
            this.topology = topology;
            index = -1;
        }

        public int getIndex() {
            return index;
        }

        public boolean hasNext() {
            int lastIndex = topology.particles.size() - 1;
            return (index != lastIndex);
        }

        public T next() {
            int lastIndex = topology.particles.size() - 1;
            if (index == lastIndex) {
                throw new NoSuchElementException();
            }

            ++index;

            return topology.particles.get(index);
        }

        public void remove() {
            if (index == -1) {
                throw new IllegalStateException();
            }

            topology.particles.remove(index);
            --index;
        }

        private int index;
        private GBestTopology<T> topology;
    }

    

    
    
    
    public boolean remove(E indiv) {
		return particles.remove(indiv);
	}

	public E get(int index) {
		return this.particles.get(index);
	}

	public E set(int index, E particle) {
		this.particles.set(index, particle);
		return particle;
	}

	public void setAll(Collection<? extends E> set) {
		this.particles.clear();
		this.particles.addAll(set);
	}

	
	public List<E> getAll() {
		return this.particles;
	}


	public boolean isEmpty() {
		return this.particles.isEmpty();
	}


	public void clear() {
		this.particles.clear();		
	}

	// TODO: Jan to explain next()
	/*public E next() {
		throw new UnsupportedOperationException("next() is not supported in GBestTopology");
	}*/

	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	@Override
	public int hashCode() {
		return this.particles.hashCode();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	@Override
	public Object[] toArray() {
		return this.particles.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public void add(int index, E element) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public E remove(int index) {
		return this.particles.remove(index);
		//throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public int indexOf(Object o) {
		return this.particles.indexOf(o);
		//throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public ListIterator<E> listIterator() {
		return this.particles.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return this.particles.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	
	
	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
			
	}
	
	
	

}

