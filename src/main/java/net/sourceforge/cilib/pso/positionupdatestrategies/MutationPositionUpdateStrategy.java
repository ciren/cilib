/*
 * MutationPositionUpdateStrategy.java
 * 
 * Created on Nov 30, 2005
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.pso.positionupdatestrategies;

import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.BoundedControlParameter;
import net.sourceforge.cilib.controlparameterupdatestrategies.LinearDecreasingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.MathUtil;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Real;

/**
 * 
 * @author Gary Pampara
 * @author Auralia Edwards
 */
public class MutationPositionUpdateStrategy extends
		StandardPositionUpdateStrategy {
	private static final long serialVersionUID = -8369435727526797836L;
	
	private MaximumIterations maximumIterations;
	private BoundedControlParameter decreasingParameter;
	private RandomNumber randomNumber;
	
	
	/**
	 * Create the <tt>PositionUpdateStrategy</tt> and initialise the required objects.
	 * Create the <tt>decreasingParameter</tt> and intialise it with the bounds <tt>R(0,1)</tt>
	 *
	 */
	public MutationPositionUpdateStrategy() {
		super();
		
		decreasingParameter = new LinearDecreasingControlParameter();
		decreasingParameter.setUpperBound(1.0);
		decreasingParameter.setLowerBound(0.0);
		decreasingParameter.setParameter(1.0);
		
		randomNumber = new RandomNumber();
	}
	
	public MutationPositionUpdateStrategy(MutationPositionUpdateStrategy copy) {
		super(copy);
		
		this.decreasingParameter = copy.decreasingParameter.clone();
		this.randomNumber = copy.randomNumber;
	}
	
	public MutationPositionUpdateStrategy clone() {
		return new MutationPositionUpdateStrategy(this);
	}
	
	
	/**
	 * Get the <tt>ControlParameterUpdateStrategy</tt> representing the mutation probability.
	 * @return Returns the decreasingParameter (<tt>ControlParameterUpdateStrategy</tt>).
	 */
	public BoundedControlParameter getDecreasingParameter() {
		return decreasingParameter;
	}


	/**
	 * Set the <tt>ControlParameterUpdateStrategy</tt> representing the mutation probability.
	 * @param decreasingParameter The <tt>ControlParameterUpdateStrategy</tt> to set.
	 */
	public void setDecreasingParameter(BoundedControlParameter decreasingParameter) {
		this.decreasingParameter = decreasingParameter;
	}
	
	
	/**
	 * Update the position of the provided <tt>Particle</tt> and then mutate 
	 * the position vector if required.
	 * 
	 * @param particle The <tt>Particle</tt> on which to perform the update.
	 */
	public void updatePosition(Particle particle) {
		super.updatePosition(particle);
		
		if (maximumIterations == null) {
			Vector<StoppingCondition> vector = (Algorithm.get()).getStoppingConditions();
			for (Iterator<StoppingCondition> i = vector.iterator(); i.hasNext(); ) {
				StoppingCondition condition = i.next();
				if (condition instanceof MaximumIterations) {
					maximumIterations = (MaximumIterations) condition;
					break;
				}
			}
		}
		
		if (Algorithm.get().getIterations() < decreasingParameter.getParameter()*(double)maximumIterations.getMaximumIterations()) {
			mutate(particle);
		}
		
		decreasingParameter.updateParameter();
	}

	
	/**
	 * 
	 * @param particle
	 */
	private void mutate(Particle particle) {
		PSO p = (PSO) Algorithm.get();

		net.sourceforge.cilib.type.types.container.Vector position = (net.sourceforge.cilib.type.types.container.Vector) particle.getPosition();
		
		double tempLower = 0.0;
		double tempUpper = 0.0;
		
		for (int i = 0; i < position.getDimension(); ++i) { // Mutation
			double number = Math.pow((1.0 - (double)p.getIterations()/(maximumIterations.getMaximumIterations()*decreasingParameter.getParameter())),1.5);
			if (MathUtil.flip(number) == 1)
			{
				int dimension = Double.valueOf(randomNumber.getUniform(0, position.getDimension())).intValue();
				Real real = (Real) position.get(dimension);
				
				double range = ((real.getUpperBound() - real.getLowerBound())* strangeFunction(p, maximumIterations))/2.0;

				if ((real.getReal()-range) < real.getLowerBound())
					tempLower = real.getLowerBound();
				else
					tempLower = real.getReal()-range;
				
				if ((real.getReal()+range) > real.getUpperBound())
					tempUpper = real.getUpperBound();
				else
					tempUpper = real.getReal()+range;

				// Now reinitialise the number randomly between tempUpper and tempLower
				double tmp = randomNumber.getUniform(tempLower,tempUpper);

				position.setReal(i, tmp);
			}
		}

	}
	
	
	/**
	 * 
	 * @param p
	 * @param max
	 * @return
	 */
	private double strangeFunction(PSO p, MaximumIterations max) {
		return Math.pow(1.0 - (double)p.getIterations()/(max.getMaximumIterations()*decreasingParameter.getParameter()),1.5);
	}
	
}