/*
 * Created on Apr 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.cilib.PSO;

import java.util.Random;

import net.sourceforge.cilib.Random.KnuthSubtractive;
import net.sourceforge.cilib.Type.Types.Vector;




/**
 * @author engel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LinearVelocityUpdate extends StandardVelocityUpdate {

	public LinearVelocityUpdate() {
		super();
		socialComponent = new ConstantAcceleration();
		cognitiveComponent = new ConstantAcceleration();
		socialRandomGenerator = new KnuthSubtractive();
		cognitiveRandomGenerator = new KnuthSubtractive();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.VelocityUpdate#updateVelocity(net.sourceforge.cilib.PSO.Particle)
	 */
	public void updateVelocity(Particle particle) {
		//double[] velocity = particle.getVelocity();
		//double[] position = particle.getPosition();
		//double[] bestPosition = particle.getBestPosition();
		//double[] nBestPosition = particle.getNeighbourhoodBest().getBestPosition();
		Vector velocity = particle.getVelocity();
		Vector position = particle.getPosition();
		Vector bestPosition = particle.getBestPosition();
		Vector nBestPosition = particle.getNeighbourhoodBest().getBestPosition();
		
		float social = socialRandomGenerator.nextFloat();
		float cognitive = cognitiveRandomGenerator.nextFloat();
		
		for (int i = 0; i < particle.getDimension(); ++i) {
			/*velocity[i] = inertiaComponent.get() * velocity[i]
				+ cognitive * (bestPosition[i] - position[i]) * cognitiveComponent.get()
				+ social * (nBestPosition[i] - position[i]) * socialComponent.get();*/
			
			double tmp = inertiaComponent.get()*velocity.getReal(i)
				+ cognitive * (bestPosition.getReal(i) - position.getReal(i)) * cognitiveComponent.get()
				+ social * (nBestPosition.getReal(i) - position.getReal(i)) * socialComponent.get();
			velocity.setReal(i, tmp);
			
			clamp(velocity, i);
		}
		
	}

	
	
	/**
	 * @return Returns the congnitiveRandomGenerator.
	 */
	public Random getCongnitiveRandomGenerator() {
		return cognitiveRandomGenerator;
	}

	/**
	 * @param congnitiveRandomGenerator The congnitiveRandomGenerator to set.
	 */
	public void setCongnitiveRandomGenerator(Random congnitiveRandomGenerator) {
		this.cognitiveRandomGenerator = congnitiveRandomGenerator;
	}

	/**
	 * @return Returns the socialRandomGenerator.
	 */
	public Random getSocialRandomGenerator() {
		return socialRandomGenerator;
	}

	/**
	 * @param socialRandomGenerator The socialRandomGenerator to set.
	 */
	public void setSocialRandomGenerator(Random socialRandomGenerator) {
		this.socialRandomGenerator = socialRandomGenerator;
	}


	private Random socialRandomGenerator;
	private Random cognitiveRandomGenerator;
	
}
