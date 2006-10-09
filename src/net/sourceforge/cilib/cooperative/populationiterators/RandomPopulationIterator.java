package net.sourceforge.cilib.cooperative.populationiterators;

import java.util.List;
import java.util.Random;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.cooperative.ParticipatingAlgorithm;

/**
 * TODO test this class
 * @author Theuns Cloete
 */
public class RandomPopulationIterator implements PopulationIterator {
	List<Algorithm> populations = null;
	int iterations = 0;

	public RandomPopulationIterator() {
	}
	
	public RandomPopulationIterator(RandomPopulationIterator rhs) {
		populations = rhs.populations;
	}

	public RandomPopulationIterator clone() {
		return new RandomPopulationIterator(this);
	}

	public boolean hasNext() {
		return iterations < populations.size();
	}

	@SuppressWarnings("unchecked")
	public Algorithm next() {
		Algorithm population = null;
		Random random = new Random();
		do {
			population = populations.get(random.nextInt(populations.size()));
		} while(!((ParticipatingAlgorithm)population).participated());

		iterations++;
		return population;
	}

	public void remove() {
		throw new UnsupportedOperationException("Removal of a ParticipatingAlgorithm from a CooperativeOptimisationAlgorithm is not supported");
	}

	public void setPopulations(List<Algorithm> p) {
		populations = p;
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
