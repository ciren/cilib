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
		// TODO Auto-generated method stub
		
	}

	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

	public int nextIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public E previous() {
		// TODO Auto-generated method stub
		return null;
	}

	public int previousIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void set(E o) {
		// TODO Auto-generated method stub
		
	}

	public E current() {
		return populations.get(iterations);
	}

}
