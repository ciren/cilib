/*
 * LBestTopology.java
 *
 * Created on January 17, 2003, 6:34 PM
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;

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
 * @author Edwin Peer
 * @author Theuns Cloete (converted {@link #neighbourhoodSize} to be a {@linkplain ControlParameterUpdateStrategy}
 */
public class LBestTopology<E extends Entity> extends GBestTopology<E> {
	private static final long serialVersionUID = 93039445052676571L;

	private ControlParameter neighbourhoodSize;

	/**
	 * Creates a new instance of <code>LBestTopology</code>. The default
	 * {@link #neighbourhoodSize} is a {@linkplain ConstantUpdateStrategy} with it's parameter set to
	 * 3.
	 */
	public LBestTopology() {
		super();
		neighbourhoodSize = new ConstantControlParameter(3);
	}

    public LBestTopology(LBestTopology<E> copy) {
    	super(copy);
    	this.neighbourhoodSize = copy.neighbourhoodSize;
    }
    
    public LBestTopology<E> clone() {
    	return new LBestTopology<E>(this);
    }

	/**
	 * Recalculate the {@link #neighbourhoodSize} by updating the
	 * {@link ControlParameterUpdateStrategy} and then construct a new iterator to be returned.
	 * @return a new iterator for this topology.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
		neighbourhoodSize.updateParameter();
		return new LBestNeighbourhoodIterator<E>(this, (ArrayIterator<E>) iterator);
	}

	/**
	 * Sets the {@linkplain ControlParameterUpdateStrategy} that should be used to determine the
	 * number of particles in the neighbourhood of each particle. The default is a
	 * {@linkplain ConstantUpdateStrategy} with the parameter set to 3.
	 * @param neighbourhoodSize The {@linkplain ControlParameterUpdateStrategy} to use.
	 */
	public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
		this.neighbourhoodSize = neighbourhoodSize;
	}

	/**
	 * Accessor for the number of particles in a neighbourhood. NOTE: This method does not return the
	 * {@linkplain ControlParameterUpdateStrategy} but the parameter that is changed / updated by it
	 * rounded to the nearest integer.
	 * @return The size of the neighbourhood.
	 */
	public int getNeighbourhoodSize() {
		int rounded = Long.valueOf(Math.round(neighbourhoodSize.getParameter())).intValue();

		if (super.size() == 0) // to show a sensible default value in CiClops
			return rounded;

		if (rounded > super.size())
			return super.size();

		return rounded;
	}

    private class LBestNeighbourhoodIterator<T extends Entity> implements ArrayIterator<T> {
        
        public LBestNeighbourhoodIterator(LBestTopology<T> topology, ArrayIterator<T> iterator) {
            if (iterator.getIndex() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            index = iterator.getIndex() - (topology.getNeighbourhoodSize() / 2) - 1; // TODO: get ediwn to explain this
            if (index < 0) {
                index += topology.size();
            }
            count = 0;
        }
        
        public int getIndex() {
            return index;
        }
        
        public boolean hasNext() {
            return (count != topology.getNeighbourhoodSize());
        }
        
        public T next() {
            if (count == topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
            ++index;
            ++count;
            if (index == topology.size()) {
               index = 0; 
            }
            return topology.entities.get(index);
        }
        
        public void remove() {
            topology.entities.remove(index);
            --index;
            if (index < 0) {
            	index += topology.size();
            }
        }
        
        private LBestTopology<T> topology;
        private int index;
        private int count;
    }
}
