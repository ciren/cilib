/*
 * MOFitness.java 
 * 
 * Created on Aug 27, 2004
 *
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.problem;

/**
 * @author Edwin Peer
 *
 */
public class MOFitness implements Fitness{

	private static final long serialVersionUID = 1477723759384827131L;

	public MOFitness(MOOptimisationProblem problem, Object solution, boolean count) {
		int size = problem.getProblemCount();
		fitnesses = new Fitness[size];
		for (int i = 0; i < size; ++i) {
			fitnesses[i] = problem.getFitness(i, solution, count);
		}
	}
	
	public Double getValue() {
		// TODO: Figure out what to do here
		throw new UnsupportedOperationException();
	}

	public int compareTo(Fitness other) {
		MOFitness tmp = (MOFitness) other;
		
		boolean AdominateB = false;
		boolean BdominateA = false;
		boolean AmaydominateB = true;
		boolean BmaydominateA = true;
		
		for(int i = 0; i < fitnesses.length; i++) {
			int r = fitnesses[i].compareTo(tmp.fitnesses[i]);
			
			if(r < 0) {
				AdominateB = true;
				BmaydominateA = false;
			} else if(r > 0) {
				BdominateA = true;
				AmaydominateB = false;
			}
			
		}

		if(AdominateB && AmaydominateB) {
			return -1;
		} else if(BdominateA && BmaydominateA) {
			return 1;
		} else {
			return 0;
		}
	}

	Fitness [] fitnesses;
	
}
