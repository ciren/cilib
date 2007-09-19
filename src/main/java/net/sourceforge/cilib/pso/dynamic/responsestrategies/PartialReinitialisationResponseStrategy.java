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
		this.randomiser = copy.randomiser.clone();
	}

	@Override
	public PartialReinitialisationResponseStrategy clone() {
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
		int numParticlesToReinitialise = (int)Math.floor(populationSize * reinitialisationRatio);
		int run = 0;
		while(run < numParticlesToReinitialise) {
			++run;
			boolean gotParticle = false;
			while(!gotParticle) {
				int index = randomiser.nextInt(populationSize);
				if(used[index]) continue;
				// ELSE
				Particle aParticle = (Particle)topology.get(index);
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
