package net.sourceforge.cilib.cooperative.populationiterators;

import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * TODO test this class
 * @author Theuns Cloete
 */
public class SequentialPopulationIterator implements PopulationIterator {
	List<PopulationBasedAlgorithm> populations = null;
	int iterations = 0;

	public SequentialPopulationIterator() {
	}
	
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
	public Algorithm next() {
		return populations.get(iterations++);
	}

	public void remove() {
		throw new UnsupportedOperationException("Removal of an Algorithm from a MultiPopulationBasedAlgorithm is not supported, yet");
	}

	public void setPopulations(List<PopulationBasedAlgorithm> p) {
		populations = p;
		iterations = 0;
	}

	public void add(Algorithm o) {
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

	public Algorithm previous() {
		// TODO Auto-generated method stub
		return null;
	}

	public int previousIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void set(Algorithm o) {
		// TODO Auto-generated method stub
		
	}

}
