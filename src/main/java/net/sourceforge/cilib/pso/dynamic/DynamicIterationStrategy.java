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
package net.sourceforge.cilib.pso.dynamic;

import java.util.Iterator;
import java.util.Random;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Type;

/**
 * Dynamic iteration strategy for PSO in dynamic environments. 
 * In each iteration, it checks for an environmental change, and reinitialises 
 * a percentage of the swarm once such a change is detected in order to preserve
 * diversity.
 * 
 * The algorithm is adopted from<br/>
 * @book{focsi,
 * 		author=         {Andries P. Engelbrecht},
 * 		title=          {{Fundamentals of Computational Swarm Intelligence}},
 *		publisher=      {John Wiley \& Sons, Ltd},
 * 		year=           {2005}
 * }
 * 
 * @author Anna Rakitianskaia
 */
public class DynamicIterationStrategy implements IterationStrategy<PSO> {
	private static final long serialVersionUID = -4441422301948289718L;
	
	private IterationStrategy<PSO> iterationStrategy;
	//private DetectionStrategy<PSO> detection
	//private ReactionStrategy<PSO> reaction
	
	private Random randomiser; // necessary to randomly pick a sentry particle
	private Double theta; // environmental change threshold
	private Double reinitialisationRatio; // percentage of the swarm that is going to be re-initialised in case of an environment change

	/**
	 * Create a new instance of {@linkplain DynamicIterationStrategy}.
	 * <p>
	 * The following defaults are set in the constructor: 
	 * randomiser is instantiated as a MersenneTwister, 
	 * theta is set to 0.001, 
	 * reinitialisationRatio is set to 0.5 (reinitialise one half of the swarm)
	 */
	public DynamicIterationStrategy() {
		randomiser = new MersenneTwister();
		theta = new Double(0.001);
		reinitialisationRatio = new Double(0.5);
	}
	
	@Override
	public DynamicIterationStrategy getClone() {
		return new DynamicIterationStrategy();
	}

	/**
	 * @see net.sourceforge.cilib.PSO.IterationStrategy#performIteration()
	 * 
	 * Structure of Dynamic iteration strategy with re-initialisation:
	 * 
	 * <ol>
	 *   <li>Check for environment change</li> 
	 *   <li>If the environment has changed:</li>
	 *   <ol>
	 *     <li>Reinitialise positions of a certain percentage of particles</li>
	 *     <li>For all particles:</li>
	 *     <ol>
	 *       <li>Reevaluate current position</li>
	 *       <li>Update personal best</li>
	 *       <li>Update neighbourhood best</li>
	 *     </ol>
	 *   </ol>
	 *   <li>For all particles:</li>
	 *   <ol>
	 *     <li>Update the particle velocity</li>
	 *     <li>Update the particle position</li>
	 *   </ol>
	 *   <li>For all particles:</li>
	 *   <ol>
	 *     <li>Calculate the particle fitness</li>
	 *     <li>For all particles in the current particle's neighbourhood</li>
	 *     <li>Update the neighbourhood best</li>
	 *   </ol>
	 * </ol>  
	 */
	public void performIteration(PSO algorithm) {
		Topology<Particle> topology = algorithm.getTopology();
		/* 1. Check for environment change:
		 * Pick a random particle (sentry) and evaluate its current position. 
		 * If the difference between the old fitness and the newly generated one is significant (exceeds a predefined theta),
		 * assume that the environment has changed.
		 */		
		// TODO: There are actually several ways to check for environment change. Should it be refined into a set of strategies?..		

		boolean envChangeOccured = false;
		Particle sentry = (Particle) topology.get(randomiser.nextInt(algorithm.getPopulationSize()));
		if(sentry.getFitness().compareTo(InferiorFitness.instance()) != 0) { // check for environmental change only if it's not the 1st iteration
			double oldSentryFitness = sentry.getFitness().getValue();
			double newSentryFitness = algorithm.getOptimisationProblem().getFitness(sentry.getPosition(), false).getValue();
			
			if(Math.abs(oldSentryFitness - newSentryFitness) >=  theta.doubleValue()) {
				envChangeOccured = true;
			}
		}
		// 2. If the environment has changed...
		if(envChangeOccured) {
			// 2.1 Reset positions of a certain percentage of particles (the percentage can be set by setting reinitialisationRatio variable)
			boolean [] used = new boolean[algorithm.getPopulationSize()];
			for (int i = 0; i < algorithm.getPopulationSize(); ++i) used[i] = false;
			int numParticlesToReinitialise = (int) Math.floor(algorithm.getPopulationSize() * reinitialisationRatio.doubleValue());
			int run = 0;
			while(run < numParticlesToReinitialise) {
				++run;
				boolean gotParticle = false;
				while(!gotParticle) {
					int index = randomiser.nextInt(algorithm.getPopulationSize());
					if(used[index]) continue;
					// ELSE
					Particle aParticle = (Particle) topology.get(index);
					Type position = aParticle.getPosition();
					position.randomise();
					used[index] = true;
					gotParticle = true;
				}
			}
			//	2.2 Reevaluate current position. Update personal best (done by reevaluate()).
			for (Particle particle : topology) {
				DynamicParticle current = (DynamicParticle) particle;
				current.reevaluate();
			}
			// 2.3 Update the neighbourhood best
			for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext();) {
				Particle current = i.next();
	            for (Iterator<? extends Particle> j = topology.neighbourhood(i); j.hasNext();) {
	            	Particle other = j.next();
	            	if (current.getSocialBestFitness().compareTo(other.getNeighbourhoodBest().getSocialBestFitness()) > 0) {
	            		other.setNeighbourhoodBest(current);
	                }
	            }
			}
		} // end if
		
		iterationStrategy.performIteration(algorithm);
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

	/**
	 * @return the theta
	 */
	public Double getTheta() {
		return theta;
	}

	/**
	 * @param theta the theta to set
	 */
	public void setTheta(Double theta) {
		this.theta = theta;
	}

	/**
	 * @return the reinitialisationRatio
	 */
	public Double getReinitialisationRatio() {
		return reinitialisationRatio;
	}

	/**
	 * @param reinitialisationRatio the reinitialisationRatio to set
	 */
	public void setReinitialisationRatio(Double reinitialisationRatio) {
		this.reinitialisationRatio = reinitialisationRatio;
	}
}
