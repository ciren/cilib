/*
 * VEPSO.java
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

package net.sourceforge.cilib.PSO;

import java.util.Collection;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.Algorithm.AlgorithmFactory;
import net.sourceforge.cilib.Problem.MOOptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationSolution;

/**
 * @author espeer
 *
 * TODO: Warning, this class is not yet finished.
 */
public class VEPSO extends Algorithm implements OptimisationAlgorithm {

	protected void performInitialisation() {
		int count = problem.getProblemCount();
		swarms = new PSO[count];
		for (int i = 0; i < count; ++i) {
			swarms[i] = (PSO) factory.newAlgorithm();
			swarms[i].setOptimisationProblem(problem.getOptimisationProblem(i));
			catalogue[i] = swarms[i].getBestSolution();
		}
	}
	
	protected void performIteration() {
		// TODO Auto-generated method stub
		
	}

	public void setPSOAlgorithmFactory(AlgorithmFactory factory) {
		this.factory = factory;
	}
	
	public AlgorithmFactory getPSOAlgorithmFactory() {
		return factory;
	}
	
	public void setOptimisationProblem(OptimisationProblem problem) {
		this.problem = (MOOptimisationProblem) problem;
	}

	public OptimisationProblem getOptimisationProblem() {
		return problem;
	}

	public OptimisationSolution getBestSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<OptimisationSolution> getSolutions() {
		// TODO Auto-generated method stub
		return null;
	}

	private MOOptimisationProblem problem;
	private AlgorithmFactory factory;
	private PSO[] swarms;
	private Object[] catalogue;
}
