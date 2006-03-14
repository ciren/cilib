/*
 * BreedingPSO.java
 * 
 * Oct 15, 2005 10:16:34 AM
 *
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
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

import net.sourceforge.cilib.ec.crossoveroperators.ArithmeticCrossOver;
import net.sourceforge.cilib.ec.crossoveroperators.CrossOverOperator;
import net.sourceforge.cilib.ec.selectionoperators.RandomSelection;
import net.sourceforge.cilib.ec.selectionoperators.SelectionOperator;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * @author otter
 */
public class BreedingPSO extends PSO {
	
	private CrossOverOperator<Particle> crossover;
	private SelectionOperator<Particle> particleSelector;
	
	public BreedingPSO() {
		crossover = new ArithmeticCrossOver<Particle>();
		particleSelector = new RandomSelection<Particle>();
	}
	
	protected void performIteration() {
		//do stuff, in what sequebce????
		super.performIteration();
		//do breeding step...
		this.breed();
	}
	
	/**
	 * Want to select two particle perform cross-over to produce two new particles which
	 * replaces the parent particles.
	 */
	private void breed() {
		//list containing the parents.
		ArrayList<Particle> parents = (ArrayList<Particle>)particleSelector.select(this.getTopology(), crossover.numberOffParentsNeeded());
		//indexes to remember the positions of the parents within the topology so that they van be easily replaced
		//with the generated particle offspring.
		int parentindex0 = this.getTopology().indexOf(parents.get(0));
		int parentindex1 = this.getTopology().indexOf(parents.get(1));
		//get the particle offspring.
		ArrayList<Particle> kiddies = (ArrayList<Particle>)crossover.reproduce(parents);
		//do the replacement...
		this.getTopology().set(parentindex0, kiddies.get(0));
		this.getTopology().set(parentindex1, kiddies.get(1));
	}

	public CrossOverOperator<Particle> getCrossover() {
		return crossover;
	}

	public void setCrossover(CrossOverOperator<Particle> crossover) {
		this.crossover = crossover;
	}

	public SelectionOperator<Particle> getParticleSelector() {
		return particleSelector;
	}

	public void setParticleSelector(SelectionOperator<Particle> particleSelector) {
		this.particleSelector = particleSelector;
	}
}

