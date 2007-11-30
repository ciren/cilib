package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.pso.PSO;

/**
 * @author Anna Rakitianskaia
 */
public class RandomSentryDetectionStrategy implements
		EnvironmentChangeDetectionStrategy {
	private int sentries;
	private double theta; 
	private Random randomiser;

	public RandomSentryDetectionStrategy() {
		sentries = 1;
		theta = 0.001;
		randomiser = new MersenneTwister();
	}
	
	public RandomSentryDetectionStrategy(RandomSentryDetectionStrategy copy) {
		this.sentries = copy.sentries;
		this.theta = copy.theta;
		this.randomiser = copy.randomiser.getClone();
	}
	
	@Override
	public RandomSentryDetectionStrategy getClone() {
		return new RandomSentryDetectionStrategy(this);
	}
	

	/** Check for environment change:
	 * Pick the specified number of random particles (sentries) and evaluate their current positions. 
	 * If the difference between the old fitness and the newly generated one is significant (exceeds a predefined theta)
	 * for one or more of the sentry particles, assume that the environment has changed.
	 * @param algorithm PSO algorithm that operates in a dynamic environment
	 * @return true if any changes are detected, false otherwise
	 */		
	public boolean detectChange(PSO algorithm) {
		Topology<Particle> topology = algorithm.getTopology();

		boolean envChangeOccured = false;
		ArrayList<Particle> sentryList = new ArrayList<Particle>();
		int populationSize = algorithm.getPopulationSize();
				
		for(int i = 0; i < sentries; i++) {
			int index = randomiser.nextInt(populationSize);
			sentryList.add((Particle)topology.get(index));
		}
		
		for(Particle nextSentry : sentryList) {
			double oldSentryFitness = nextSentry.getFitness().getValue();
			double newSentryFitness = algorithm.getOptimisationProblem().getFitness(nextSentry.getPosition(), false).getValue();
			
			if(Math.abs(oldSentryFitness - newSentryFitness) >=  theta) {
				envChangeOccured = true;
				break;
			}
		}
		return envChangeOccured;
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
	 * @return the sentries
	 */
	public int getSentries() {
		return sentries;
	}

	/**
	 * @param sentries the sentries to set
	 */
	public void setSentries(int sentries) {
		this.sentries = sentries;
	}

	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * @param theta the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
}
