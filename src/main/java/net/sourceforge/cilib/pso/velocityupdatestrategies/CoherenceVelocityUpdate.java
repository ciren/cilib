package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Iterator;
import java.util.Random;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.RandomisedParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.MathUtil;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.math.random.generator.KnuthSubtractive;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Velocity update for the Coher
 * @author Daniel Lowes
 */
public class CoherenceVelocityUpdate extends StandardVelocityUpdate {
	private static final long serialVersionUID = -9051938755796130230L;
	
	private Random socialRandomGenerator;
	private Random cognitiveRandomGenerator;
	private RandomNumber randomNumber;

	public CoherenceVelocityUpdate() {
		super();
		socialAcceleration = new RandomisedParameterUpdateStrategy();
		cognitiveAcceleration = new RandomisedParameterUpdateStrategy();
		socialRandomGenerator = new KnuthSubtractive();
		cognitiveRandomGenerator = new KnuthSubtractive();
	}

    public CoherenceVelocityUpdate clone() {
    	return new CoherenceVelocityUpdate(this);
    }

    public CoherenceVelocityUpdate(CoherenceVelocityUpdate copy) {
    	this.inertiaWeight = copy.inertiaWeight.clone();
    	this.cognitiveAcceleration = copy.cognitiveAcceleration.clone();
    	this.socialAcceleration = copy.socialAcceleration.clone();
    	this.vMax = copy.vMax.clone();
    	this.socialRandomGenerator = new KnuthSubtractive();
    	this.cognitiveRandomGenerator = new KnuthSubtractive();
    }


	public void updateVelocity(Particle particle) {
		Vector velocity = (Vector) particle.getVelocity();
		Vector position = (Vector) particle.getPosition();
		Vector bestPosition = (Vector) particle.getBestPosition();
		Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();

		float social = socialRandomGenerator.nextFloat();
		float cognitive = cognitiveRandomGenerator.nextFloat();

		//DistanceMeasure adm = new AbsoluteDistanceMeasure();
		//DistanceMeasure dm = new MetricDistanceMeasure();

		double avgv = 0.0;
		double swv = 0.0;
		Topology<Particle> topology = ((PSO)Algorithm.get()).getTopology();
  		Iterator<? extends Particle> it = topology.neighbourhood(null);
  		double[] al = new double[particle.getDimension()];
   		while (it.hasNext()) {
   			Particle pl = it.next();
   			double tmpv = 0.0;
   			//double tmpsv = 0.0;
   			for(int dim = 0; dim < particle.getDimension(); dim++) {
				al[dim] = al[dim]+((Vector)pl.getPosition()).getReal(dim);
   				tmpv += Math.pow(((Vector)pl.getPosition()).getReal(dim), 2);
   			}
   			tmpv = Math.sqrt(tmpv);
   			avgv += tmpv;
   		}
   		for(int i = 0; i < particle.getDimension(); i++) {
			//al.set(i, ;
			swv += (al[i]/30) * (al[i]/30);
		}
		swv = Math.sqrt(swv);

		for (int i = 0; i < particle.getDimension(); ++i) {
			double tmp = 0.0;
			tmp = inertiaWeight.getParameter()*velocity.getReal(i)
				+ cognitive * (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter()
				+ social * (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter();

			double avgdim = 0.0;
	  		it = topology.neighbourhood(null);
	   		while (it.hasNext()) {
	   			avgdim += ((Vector)(it.next().getPosition())).getReal(i);
	   		}
			avgdim /= particle.getDimension();

			double cvelocity = MathUtil.sigmoid(swv/avgv)*avgdim*randomNumber.getCauchy();

			//System.out.println(cvelocity);
			tmp += cvelocity;

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
}
