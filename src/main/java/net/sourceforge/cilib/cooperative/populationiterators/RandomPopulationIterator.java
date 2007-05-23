package net.sourceforge.cilib.cooperative.populationiterators;

import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.math.random.RandomNumber;

/**
 * TODO test this class
 * @author Theuns Cloete
 */
public class RandomPopulationIterator<E extends Algorithm> implements PopulationIterator<E> {
	List<E> populations = null;
	RandomNumber random = null;
	int iterations = 0;

	public RandomPopulationIterator() {
		random = new RandomNumber();
		iterations = 0;
	}
	
	@SuppressWarnings("unchecked")
	public RandomPopulationIterator(RandomPopulationIterator rhs) {
		populations = rhs.populations;
		random = new RandomNumber();
		iterations = rhs.iterations;
	}

	public RandomPopulationIterator clone() {
		return new RandomPopulationIterator(this);
	}

	public boolean hasNext() {
		return iterations < populations.size();
	}

	@SuppressWarnings("unchecked")
	public E next() {
		E population = null;
		do {
			population = populations.get((int)random.getUniform(0, populations.size()));
		} while(((ParticipatingAlgorithm)population).participated());

		iterations++;
		return population;
	}

	public void remove() {
		throw new UnsupportedOperationException("Removal of a ParticipatingAlgorithm from a CooperativeOptimisationAlgorithm is not supported");
	}

	public void setPopulations(List<E> p) {
		populations = p;
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
		return this.populations.get(iterations);
	}
}
