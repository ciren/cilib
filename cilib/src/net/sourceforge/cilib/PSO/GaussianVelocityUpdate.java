/*
 * Created on Jul 26, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.cilib.PSO;

import java.util.Random;

import net.sourceforge.cilib.Random.KnuthSubtractive;

/**
 * @author espeer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GaussianVelocityUpdate implements VelocityUpdate {

	public GaussianVelocityUpdate() {
		randomizer = new KnuthSubtractive();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.VelocityUpdate#updateVelocity(net.sourceforge.cilib.PSO.Particle)
	 */
	public void updateVelocity(Particle particle) {
        double[] velocity = particle.getVelocity();
        double[] position = particle.getPosition();
        double[] bestPosition = particle.getBestPosition();
        double[] nBestPosition = particle.getNeighbourhoodBest().getBestPosition();

        for (int i = 0; i < particle.getDimension(); ++i) {
        	double tmp = position[i] - nBestPosition[i];
        	velocity[i] = randomizer.nextGaussian() *  tmp * tmp * (position[i] + nBestPosition[i]) / 2;
        }
	}
	
	public void setRandomizer(Random randomizer) {
		this.randomizer = randomizer;
	}
	
	public Random getRandomizer() {
		return randomizer;
	}

	private Random randomizer;
	
}
