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

package net.sourceforge.cilib.pso;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.AlgorithmFactory;
import net.sourceforge.cilib.algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * @author Edwin Peer
 *
 * TODO: Warning, this class is not yet finished.
 */
public class VEPSO extends PopulationBasedAlgorithm implements OptimisationAlgorithm {

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
	
	
	
	
	@Override
	public int getPopulationSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPopulationSize(int populationSize) {
		// TODO Auto-generated method stub
		
	}


	public Topology<Particle> getTopology() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTopology(Topology topology) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getDiameter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
}
