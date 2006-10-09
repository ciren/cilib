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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.algorithm.AlgorithmFactory;
import net.sourceforge.cilib.algorithm.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
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
public class VEPSO extends MultiPopulationBasedAlgorithm {

	public void performInitialisation() {
		
	}
	
	public void performIteration() {
		//Get the global guides for each subswarm
		List<Entity> globalGuides = new ArrayList<Entity>();
		
		// for each subswarm get the global best -> guide
		
		// Set the global guides from each subswarm to anther subswarm
		// KnowledgeTransferStrategy.....
		
		// perform the iteration for each subswarm with the guides within the subswarms
		
		
		
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
		
		// for each subswarm select the global best and add it to this solution
		
		return null;
	}
	
	public List<OptimisationSolution> getObjectiveSolutions() {
		return null;
	}

	private MOOptimisationProblem problem;
	private AlgorithmFactory factory;
	private PSO[] swarms;
	private Object[] catalogue;
	
	
	
	

	public int getPopulationSize() {
		// TODO Auto-generated method stub
		return 0;
	}

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

	public double getDiameter() {
		// TODO Auto-generated method stub
		return 0;
	}


	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
}
