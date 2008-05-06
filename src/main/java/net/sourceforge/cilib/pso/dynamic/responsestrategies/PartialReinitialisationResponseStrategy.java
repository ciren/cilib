/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author Anna Rakitianskaia
 */
public class PartialReinitialisationResponseStrategy extends
		ParticleReevaluationResponseStrategy {
	private double reinitialisationRatio;
	private Random randomiser;
	
	public PartialReinitialisationResponseStrategy() {
		super();
		reinitialisationRatio = 0.5;
		randomiser = new MersenneTwister();
	}
	
	public PartialReinitialisationResponseStrategy(PartialReinitialisationResponseStrategy copy) {
		this.reinitialisationRatio = copy.reinitialisationRatio;
		this.randomiser = copy.randomiser.getClone();
	}

	@Override
	public PartialReinitialisationResponseStrategy getClone() {
		return new PartialReinitialisationResponseStrategy(this);
	}

	/** 
	 * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best,
	 * and reinitialising the positions of a specified percentage of particles.
	 * @param algorithm PSO algorithm that has to respond to environment change
	 */
	@Override
	public void respondToChange(PSO algorithm) {
		// Reset positions:
		Topology<Particle> topology = algorithm.getTopology();
		int populationSize = algorithm.getPopulationSize();
		boolean [] used = new boolean[populationSize];
		for (int i = 0; i < populationSize; ++i) used[i] = false;
		int numParticlesToReinitialise = (int) Math.floor(populationSize * reinitialisationRatio);
		int run = 0;
		while (run < numParticlesToReinitialise) {
			++run;
			boolean gotParticle = false;
			while (!gotParticle) {
				int index = randomiser.nextInt(populationSize);
				if(used[index]) continue;
				// ELSE
				Particle aParticle = (Particle) topology.get(index);
				Type position = aParticle.getPosition();
				position.randomise();
				used[index] = true;
				gotParticle = true;
			}
		}
		// Re-evaluate:
		reevaluateParticles(algorithm); // super class method
	}

	/**
	 * @return the reinitialisationRatio
	 */
	public double getReinitialisationRatio() {
		return reinitialisationRatio;
	}

	/**
	 * @param reinitialisationRatio the reinitialisationRatio to set
	 */
	public void setReinitialisationRatio(double reinitialisationRatio) {
		this.reinitialisationRatio = reinitialisationRatio;
	}

	/**
	 * @return the randomiser
	 */
	public Random getRandomiser() {
		return randomiser;
	}

	/**
	 * @param randomiser the randomiser to set
	 */
	public void setRandomiser(Random randomiser) {
		this.randomiser = randomiser;
	}
}
