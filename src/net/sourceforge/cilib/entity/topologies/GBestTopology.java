/*
 * GBestTopology.java
 *
 * Created on January 17, 2003, 8:07 PM
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
public class GBestTopology extends Topology<Particle> {
	
	protected ArrayList<Particle> particles;

    /**
     * Creates a new instance of <code>GBestTopology</code>.
     */
    public GBestTopology() {
        particles = new ArrayList<Particle>();
    }
    
    public Iterator<Particle> iterator() {
        return new GBestTopologyIterator(this);
    }

    public Iterator<Particle> neighbourhood(Iterator<Particle> iterator) {
        return new GBestTopologyIterator(this);
    }
    
    public boolean add(Particle particle) {
        return particles.add(particle);
    }
    
    public boolean addAll(Collection<? extends Particle> set) {
    	for (Iterator<? extends Particle> i = set.iterator(); i.hasNext(); ) {
    		this.add(i.next());
    	}
    	
    	return true;
    }

    public int size() {
        return particles.size();
    }

    protected interface ArrayIterator extends Iterator<Particle> {
        public int getIndex();
    }

    private class GBestTopologyIterator implements ArrayIterator {

    	public GBestTopologyIterator(GBestTopology topology) {
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

        public Particle next() {
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
        private GBestTopology topology;
    }

    

    
    
    
    public boolean remove(Particle indiv) {
		return particles.remove(indiv);
	}

	public Particle get(int index) {
		return this.particles.get(index);
	}

	public Particle set(int index, Particle particle) {
		this.particles.set(index, particle);
		return particle;
	}

	public void setAll(Collection<? extends Particle> set) {
		this.particles.clear();
		this.particles.addAll(set);
	}

	
	public List<Particle> getAll() {
		return this.particles;
	}


	public boolean isEmpty() {
		return this.particles.isEmpty();
	}


	public void clear() {
		this.particles.clear();		
	}

	// TODO: Jan to explain next()
	public Particle next() {
		throw new UnsupportedOperationException("next() is not supported in GBestTopology");
	}

	
	
	
	
	
	
	
	
	
	
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
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
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
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public boolean addAll(int index, Collection<? extends Particle> c) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public void add(int index, Particle element) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public Particle remove(int index) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public int indexOf(Object o) {
		return this.particles.indexOf(o);
		//throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public ListIterator<Particle> listIterator() {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public ListIterator<Particle> listIterator(int index) {
		throw new UnsupportedOperationException("Method not supported in GBestTopology");
	}

	public List<Particle> subList(int fromIndex, int toIndex) {
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

