/*
 * Copyright (C) 2003 - 2008
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

/**
 * <p>
 * Implementation of the gbest neighbourhood topology.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @param <E> The {@linkplain Entity} type.
 * @author Edwin Peer
 */
public class GBestTopology<E extends Entity> extends Topology<E> {
	private static final long serialVersionUID = 3190027340582769112L;
	
	protected ArrayList<E> entities;

    /**
     * Creates a new instance of <code>GBestTopology</code>.
     */
    public GBestTopology() {
        entities = new ArrayList<E>();
    }
    
    @SuppressWarnings("unchecked")
	public GBestTopology(GBestTopology<E> copy) {
    	this.entities = new ArrayList<E>(copy.entities.size());
    	for (E entity : copy.entities) {
    		entity.reinitialise();
    		this.entities.add((E) entity.getClone());
    	}
    }
    
    public GBestTopology<E> getClone() {
    	return new GBestTopology<E>(this);
    }
    
    public Iterator<E> iterator() {
        return new GBestTopologyIterator<E>(this);
    }

    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new GBestTopologyIterator<E>(this);
    }
    
    public boolean add(E particle) {
        return entities.add(particle);
    }
    
    public boolean addAll(Collection<? extends E> set) {
    	this.entities.ensureCapacity(entities.size()+set.size());
    	return this.entities.addAll(set);
    }

    public int size() {
        return entities.size();
    }

    /**
     * Interface to define the manner in which the iterator is to be constructed for Array types.
     * @author gpampara
     *
     * @param <T> The {@linkplain Entity} type.
     */
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
            int lastIndex = topology.entities.size() - 1;
            return (index != lastIndex) && (lastIndex >= 0);
        }

        public T next() {
            int lastIndex = topology.entities.size() - 1;
            if (index == lastIndex) {
                throw new NoSuchElementException();
            }

            ++index;

            return topology.entities.get(index);
        }

        public void remove() {
            if (index == -1) {
                throw new IllegalStateException();
            }

            topology.entities.remove(index);
            --index;
        }

        private int index;
        private GBestTopology<T> topology;
    }

    

    
    
    
    public boolean remove(E indiv) {
		return entities.remove(indiv);
	}

	public E get(int index) {
		return this.entities.get(index);
	}

	public E set(int index, E particle) {
		this.entities.set(index, particle);
		return particle;
	}

	public List<Entity> asList() {
		return new ArrayList<Entity>(this.entities);
	}


	public boolean isEmpty() {
		return this.entities.isEmpty();
	}


	public void clear() {
		this.entities.clear();		
	}

	@Override
	public boolean contains(Object entity) {
		return this.entities.contains(entity);
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
		return this.entities.hashCode();
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
		return this.entities.toArray();
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
		return this.entities.remove(index);
	}

	public int indexOf(Object o) {
		return this.entities.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public ListIterator<E> listIterator() {
		return this.entities.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return this.entities.listIterator(index);
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

