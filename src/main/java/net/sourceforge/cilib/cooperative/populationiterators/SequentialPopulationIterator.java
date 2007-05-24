/*
 * SequentialPopulationIterator.java
 * 
 * Created on May 24, 2007
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
package net.sourceforge.cilib.cooperative.populationiterators;

import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * TODO test this class
 * @author Theuns Cloete
 */
public class SequentialPopulationIterator<E extends Algorithm> implements PopulationIterator<E> {
	List<E> populations = null;
	int iterations = 0;

	public SequentialPopulationIterator() {
	}

	@SuppressWarnings("unchecked")
	public SequentialPopulationIterator(SequentialPopulationIterator rhs) {
		populations = rhs.populations;
		iterations = 0;
	}

	public SequentialPopulationIterator clone() {
		return new SequentialPopulationIterator(this);
	}

	public boolean hasNext() {
		return iterations < populations.size();
	}

	@SuppressWarnings("unchecked")
	public E next() {
		E algorithm = populations.get(iterations++);
		return (E) algorithm.getCurrentAlgorithm();
	}

	public void remove() {
		throw new UnsupportedOperationException("Removal of an Algorithm from a MultiPopulationBasedAlgorithm is not supported, yet");
	}

	public void setPopulations(List<E> p) {
		populations = p;
		iterations = 0;
	}

	public void add(E o) {
		populations.add(o);
	}

	public boolean hasPrevious() {
		return iterations > 0;
	}

	public int nextIndex() {
		return iterations + 1;
	}

	@SuppressWarnings("unchecked")
	public E previous() {
		E algorithm = populations.get(--iterations);
		return (E) algorithm.getCurrentAlgorithm();
	}

	public int previousIndex() {
		return iterations - 1;
	}

	public void set(E o) {
		populations.set(iterations, o);
	}

	public E current() {
		return populations.get(iterations);
	}
}
