/*
 * HS.java
 *
 * Created on August 24, 2007
 *
 * Copyright (C) 2003 -- 2007 - CIRG@UP 
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
package net.sourceforge.cilib.hs;

import java.util.Collections;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.AbstractList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Harmony Search as published in K.S. Lee and Z.W. Geem, "A New Meta-Heuristic
 * Algorithm for Continuous Engineering Optimization: Harmony Search Theory and
 * Practice", Computer Methods in Applied Mechanics and Engineering, volume 194,
 * pages 3902--3933, 2005
 * 
 * @author Andries Engelbrecht
 */
public class HS extends IterationStrategy<EC> {
	private static final long serialVersionUID = 8019668923312811974L;	
	private RandomNumber random1;
	private RandomNumber random2;
	private RandomNumber random3;
	private ControlParameter HarmonyMemorySize;
	private ControlParameter HarmonyMemoryConsideringRate;
	private ControlParameter PitchAdjustingRate;
	private ControlParameter DistanceBandwidth;
	
	public HS() {
		this.random1 = new RandomNumber();
		this.random2 = new RandomNumber();
		this.random3 = new RandomNumber();
		
		this.HarmonyMemorySize = new ConstantControlParameter(20); //should be equal to number of individuals
		this.HarmonyMemoryConsideringRate = new ConstantControlParameter(0.9);
		this.PitchAdjustingRate = new ConstantControlParameter(0.35);
		this.DistanceBandwidth = new ConstantControlParameter(0.5);
	}
	
	public HS(HS copy) {
		this.random1 = copy.random1.clone();
		this.random2 = copy.random2.clone();
		this.random3 = copy.random3.clone();
		
		this.HarmonyMemorySize = copy.HarmonyMemorySize;
		this.HarmonyMemoryConsideringRate = copy.HarmonyMemoryConsideringRate;
		this.PitchAdjustingRate = copy.PitchAdjustingRate;
		this.DistanceBandwidth = copy.DistanceBandwidth;
	}
	
	public HS clone() {
		return new HS(this);
	}

	/**
	 * 
	 */
	public void performIteration(EC ec) {
		//TO-DO: Make sure that all fitnesses are evaluated initially, and
		//that FE is incremented only once per iteration
		
		//calculate a new harmony
		AbstractList newHarmony = new Vector();
		AbstractList vector;
		int dimension = ec.getTopology().get(0).getDimension();
		OptimisationProblem problem = (OptimisationProblem) ec.getOptimisationProblem();
		int newHarmonyIndex;
		double newHarmonyValue;
		for (int i = 0; i < dimension; ++i) {
			double upper = ((AbstractList) problem.getDomain().getBuiltRepresenation()).getNumeric(i).getUpperBound();
			double lower = ((AbstractList) problem.getDomain().getBuiltRepresenation()).getNumeric(i).getLowerBound();
			if (random1.getUniform() < HarmonyMemoryConsideringRate.getParameter()) {
				newHarmonyIndex = (int)random2.getUniform(0, ec.getTopology().size()-1);
				vector = (AbstractList) ec.getTopology().get(newHarmonyIndex).getContents();
				newHarmonyValue = vector.getReal(i);
				if (random1.getUniform() < PitchAdjustingRate.getParameter()) {
					double pitchedValue = newHarmonyValue + random3.getUniform(-1, 1) * DistanceBandwidth.getParameter();
					if ((pitchedValue > lower) && (pitchedValue < upper))
						newHarmonyValue = pitchedValue;
				}
				newHarmony.add(new Real(newHarmonyValue));
			}
			else
				newHarmony.add(new Real(random3.getUniform(lower, upper)));
		}
		
		Fitness newHarmonyFitness = problem.getFitness(newHarmony, false);
		
		//Topology<? extends Entity> topology = ec.getTopology();
		//for (Iterator<? extends Entity> iterator = topology.iterator(); iterator.hasNext(); ) {
		//	Entity current = iterator.next();
		//	current.calculateFitness(false);
		//}
		
		Collections.sort(ec.getTopology(), new DescendingFitnessComparator());
		
		//Find worst harmony in harmony memory
		AbstractList worstHarmony = (AbstractList) ec.getTopology().get(0).getContents();
		Fitness worstHarmonyFitness = problem.getFitness(worstHarmony, false);
		//new harmony is better than worse harmony
		if (newHarmonyFitness.compareTo(worstHarmonyFitness) > 0) {
			ec.getTopology().get(0).setContents(newHarmony);
			ec.getTopology().get(0).calculateFitness();
		}	
	}	
	
	public ControlParameter getHarmonyMemoryConsideringRate() {
		return HarmonyMemoryConsideringRate;
	}

	public void setHarmonyMemoryConsideringRate(
			ControlParameter HarmonyMemoryConsideringRate) {
		this.HarmonyMemoryConsideringRate = HarmonyMemoryConsideringRate;
	}

	public ControlParameter getHarmonyMemorySize() {
		return HarmonyMemorySize;
	}

	public void setHarmonyMemorySize(ControlParameter HarmonyMemorySize) {
		this.HarmonyMemorySize = HarmonyMemorySize;
	}

	public ControlParameter getPitchAdjustingRate() {
		return PitchAdjustingRate;
	}

	public void setPitchAdjustingRate(ControlParameter PitchAdjustingRate) {
		this.PitchAdjustingRate = PitchAdjustingRate;
	}
	
	public ControlParameter getDistanceBandwidth() {
		return DistanceBandwidth;
	}

	public void setDistanceBandwidth(ControlParameter DistanceBandwidth) {
		this.DistanceBandwidth = DistanceBandwidth;
	}
}

