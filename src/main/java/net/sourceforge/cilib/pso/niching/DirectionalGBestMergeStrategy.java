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
package net.sourceforge.cilib.pso.niching;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * 
 * @author Edrich van Loggerenberg
 * 
 * @param <E> The type that should be a {@linkplain PopulationBasedAlgorithm}.
 */
public class DirectionalGBestMergeStrategy<E extends PopulationBasedAlgorithm>
	implements MergeStrategy<E> {

    private ControlParameter threshold;

    /**
     * Create a new instance.
     */
    public DirectionalGBestMergeStrategy() {
    	this.threshold = new ConstantControlParameter(0.001);
    }

    /**
     * {@inheritDoc}
     */
    public void merge(PSO mainSwarm, List<PopulationBasedAlgorithm> subSwarms) {
		for (ListIterator<PopulationBasedAlgorithm> i = subSwarms.listIterator(); i.hasNext();) {
		    PSO subSwarm1 = (PSO) i.next();
	
		    if (subSwarm1 != null) {
		    	Particle gBestParticle1 = subSwarm1.getTopology().getBestEntity();
	
		    	for (ListIterator<PopulationBasedAlgorithm> j = subSwarms.listIterator(); j.hasNext();) {
		    		PSO subSwarm2 = (PSO) j.next();	
		    		if (subSwarm2 != null) {
		    			if (subSwarm1 != subSwarm2) {
		    				Particle gBestParticle2 = subSwarm2.getTopology().getBestEntity();
	
		    				if (calculateDotProduct(calculateDirectionalVector(gBestParticle1), calculateDirectionalVector(gBestParticle2)) < 0.0) {
		    					if (testNearZero(getRadius(subSwarm1))	&& testNearZero(getRadius(subSwarm2))) {
		    						if (normalise(mainSwarm, gBestParticle1, gBestParticle2) < threshold.getParameter()) {
		    							subSwarm1.getTopology().addAll(subSwarm2.getTopology());
		    							// 	the two swarms are now merged, so
		    							// delete the one
		    							j.remove();
		    							i = subSwarms.listIterator();
	
		    							subSwarm1.getInitialisationStrategy().setEntityNumber(subSwarm1.getTopology().size());
		    							subSwarm2.getInitialisationStrategy().setEntityNumber(subSwarm2.getTopology().size());
		    							//System.out.println("merged (normalised): S1R: "
		    							//	+ subSwarm1.getRadius()
		    							//	+ " S2R: "
		    							//	+ subSwarm2.getRadius());
		    							break;
		    						}
		    					}
		    					else {
		    						DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
		    						// double distance =
		    						// distanceMeasure.distance((Vector)gBestParticle1.getPosition(),
		    						// (Vector)gBestParticle2.getPosition());
	
		    						double distance = distanceMeasure.distance(getAverageVector(subSwarm1), getAverageVector(subSwarm2));
	
		    						if ((distance < (getRadius(subSwarm1) + getRadius(subSwarm2))) && (distance < 0.001)) {
		    							subSwarm1.getTopology().addAll(subSwarm2.getTopology());
		    							// the two swarms are now merged, so
		    							// delete the one
		    							j.remove();
		    							i = subSwarms.listIterator();
	
		    							subSwarm1.getInitialisationStrategy().setEntityNumber(subSwarm1.getTopology().size());
		    							subSwarm2.getInitialisationStrategy().setEntityNumber(subSwarm2.getTopology().size());
		    							//System.out.println("merged: "
		    							//	+ distance + " S1R: "
		    							//	+ subSwarm1.getRadius()
		    							//	+ " S2R: "
		    							//	+ subSwarm2.getRadius());
		    							break;
		    						}
		    					}
		    				}
		    			}
		    		}
		    	}
		    }
		}
    }

    private Vector getAverageVector(PSO subSwarm) {
    	AveragePositionVisitor averagePositionVisitor = new AveragePositionVisitor();
    	subSwarm.accept(averagePositionVisitor);
    	return averagePositionVisitor.getAveragePosition();
	}

	private double getRadius(PSO subSwarm) {
    	RadiusVisitor radiusVisitor = new RadiusVisitor();
    	subSwarm.accept(radiusVisitor);
    	return radiusVisitor.getResult();
    }

	/**
	 * Test whether the given value is close to a specified epsilon.
	 * @param value The value to test.
	 * @return <code>true</code> if the value is less that the defined epsilon,
	 *         <code>false</code> otherwise. 
	 */
	public boolean testNearZero(double value) {
		if (value < 0.0001)
			return true;

		return false;
    }

	/**
	 * Normalise the two {@linkplain Particle}s.
	 * @param mainSwarm The mainswarm
	 * @param gBest1 The first {@linkplain Particle}.
	 * @param gBest2 The second {@linkplain Particle}.
	 * @return The normalised value.
	 */
    public double normalise(PSO mainSwarm, Particle gBest1, Particle gBest2) {
		Real range = (Real) ((Vector) mainSwarm.getOptimisationProblem().getDomain().getBuiltRepresenation().getClone()).get(0);
	
		Vector gBestPos1 = (Vector) gBest1.getPosition();
		Vector gBestPos2 = (Vector) gBest2.getPosition();
	
		Vector v1 = new Vector();
		Vector v2 = new Vector();
	
		for (int i = 0; i < gBest1.getPosition().getDimension(); i++) {
		    v1.append(new Real((gBestPos1.getReal(i) - range.getLowerBound()) / (range.getUpperBound() - range.getLowerBound())));
		    v2.append(new Real((gBestPos2.getReal(i) - range.getLowerBound()) / (range.getUpperBound() - range.getLowerBound())));
		}
	
		double sum = 0.0;
		for (int i = 0; i < gBest1.getPosition().getDimension(); i++)
		    sum += (v1.getReal(i) - v2.getReal(i)) * (v1.getReal(i) - v2.getReal(i));
		
		sum = sum / gBest1.getPosition().getDimension();
		sum = Math.sqrt(sum);
	
		return sum;
    }

    /**
     * Get the {@linkplain ControlParameter} defining the threshold value.
     * @return The threshold value.
     */
    public ControlParameter getThreshold() {
    	return threshold;
    }

    /**
     * Set the threshold {@linkplain ControlParameter}.
     * @param threshold The value to set.
     */
    public void setThreshold(ControlParameter threshold) {
    	this.threshold = threshold;
    }

    /**
     * Calculate the directional vector from the given {@linkplain Particle}.
     * @param particle The {@linkplain Particle} to calculate the directional vector from.
     * @return The directional {@linkplain Vector}.
     */
    public Vector calculateDirectionalVector(Particle particle) {
    	Vector newPosition = (Vector) particle.getPosition().getClone();
    	Vector velocity = (Vector) particle.getVelocity().getClone();

    	for (int i = 0; i < newPosition.getDimension(); i++) {
    		double value = newPosition.getReal(i);
    		value += velocity.getReal(i);
    		newPosition.setReal(i, value);
    	}

    	Vector direction = new Vector();
    	direction = newPosition.subtract((Vector) particle.getPosition());
	
    	return direction;
    }

    /**
     * Calculate the dot product from {@linkplain Vector} <code>a</code> and 
     * {@linkplain Vector} <code>b</code>.
     * @param a The first {@linkplain Vector}.
     * @param b The second {@linkplain Vector}.
     * @return The dot product value.
     */
    public double calculateDotProduct(Vector a, Vector b) {
    	return a.dot(b);
    }
}
