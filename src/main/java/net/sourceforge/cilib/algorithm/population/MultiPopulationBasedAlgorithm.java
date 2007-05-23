/*
 * MultiPopulationBasedAlgorithm.java
 * 
 * Created on Feb 10, 2006
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
package net.sourceforge.cilib.algorithm.population;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.cooperative.populationiterators.PopulationIterator;
import net.sourceforge.cilib.cooperative.populationiterators.SequentialPopulationIterator;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * 
 * @author Gary Pampara
 *
 */
public abstract class MultiPopulationBasedAlgorithm extends PopulationBasedAlgorithm implements Iterable<Algorithm> {
	
	protected List<PopulationBasedAlgorithm> populationBasedAlgorithms;
	protected PopulationIterator<PopulationBasedAlgorithm> algorithmIterator;
	
	public MultiPopulationBasedAlgorithm() {
		this.populationBasedAlgorithms = new ArrayList<PopulationBasedAlgorithm>();
		this.algorithmIterator = new SequentialPopulationIterator<PopulationBasedAlgorithm>();
		this.algorithmIterator.setPopulations(this.populationBasedAlgorithms);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<Algorithm> iterator() {
		return this.algorithmIterator.clone();		
	}

	@Override
	public Algorithm getCurrentAlgorithm() {
		return this.algorithmIterator.current().getCurrentAlgorithm();
	}

	/**
	 * 
	 */
	public abstract void performIteration();


	public List<PopulationBasedAlgorithm> getPopulations() {
		return populationBasedAlgorithms;
	}


	public void setPopulations(List<PopulationBasedAlgorithm> populationBasedAlgorithms) {
		this.populationBasedAlgorithms = populationBasedAlgorithms;
	}
	
	public void addPopulationBasedAlgorithm(PopulationBasedAlgorithm algorithm) {
		this.populationBasedAlgorithms.add(algorithm);
	}
	
	public void removePopulationBasedalgorithm(PopulationBasedAlgorithm algorithm) {
		this.populationBasedAlgorithms.remove(algorithm);
	}
	
	public OptimisationProblem getOptimisationProblem() {
		return this.optimisationProblem;
	}
	
	public void setOptimisationProblem(OptimisationProblem problem) {
		this.optimisationProblem = problem;
	}

	public PopulationIterator<PopulationBasedAlgorithm> getAlgorithmIterator() {
		return algorithmIterator;
	}

	public void setAlgorithmIterator(PopulationIterator<PopulationBasedAlgorithm> algorithmIterator) {
		this.algorithmIterator = algorithmIterator;
		this.algorithmIterator.setPopulations(this.populationBasedAlgorithms);
	}
	
	

}
