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

import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.container.SortedList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Harmony;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
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
public class HS extends SingularAlgorithm {
	private static final long serialVersionUID = 8019668923312811974L;	
	private RandomNumber random1;
	private RandomNumber random2;
	private RandomNumber random3;
	private ControlParameter harmonyMemorySize;
	private ControlParameter harmonyMemoryConsideringRate;
	private ControlParameter pitchAdjustingRate;
	private ControlParameter distanceBandwidth;
	private SortedList<Harmony> harmonyMemory;
	
	public HS() {
		this.random1 = new RandomNumber();
		this.random2 = new RandomNumber();
		this.random3 = new RandomNumber();
		
		this.harmonyMemorySize = new ConstantControlParameter(20); //should be equal to number of individuals
		this.harmonyMemoryConsideringRate = new ConstantControlParameter(0.9);
		this.pitchAdjustingRate = new ConstantControlParameter(0.35);
		this.distanceBandwidth = new ConstantControlParameter(0.5);
		
		this.harmonyMemory = new SortedList<Harmony>();
	}
	
	public HS(HS copy) {
		this.random1 = copy.random1.getClone();
		this.random2 = copy.random2.getClone();
		this.random3 = copy.random3.getClone();
		
		this.harmonyMemorySize = copy.harmonyMemorySize.getClone();
		this.harmonyMemoryConsideringRate = copy.harmonyMemoryConsideringRate.getClone();
		this.pitchAdjustingRate = copy.pitchAdjustingRate.getClone();
		this.distanceBandwidth = copy.distanceBandwidth.getClone();
		
		this.harmonyMemory = copy.harmonyMemory.getClone();
	}
	
	public HS getClone() {
		return new HS(this);
	}
	
	@Override
	public void performInitialisation() {
		for (int i = 0; i < harmonyMemorySize.getParameter(); i++) {
			Harmony harmony = new Harmony();
			harmony.initialise(getOptimisationProblem());
			this.harmonyMemory.add(harmony);
		}
	}

	public ControlParameter getHarmonyMemoryConsideringRate() {
		return harmonyMemoryConsideringRate;
	}

	public void setHarmonyMemoryConsideringRate(
			ControlParameter HarmonyMemoryConsideringRate) {
		this.harmonyMemoryConsideringRate = HarmonyMemoryConsideringRate;
	}

	public ControlParameter getHarmonyMemorySize() {
		return harmonyMemorySize;
	}

	public void setHarmonyMemorySize(ControlParameter HarmonyMemorySize) {
		this.harmonyMemorySize = HarmonyMemorySize;
	}

	public ControlParameter getPitchAdjustingRate() {
		return pitchAdjustingRate;
	}

	public void setPitchAdjustingRate(ControlParameter PitchAdjustingRate) {
		this.pitchAdjustingRate = PitchAdjustingRate;
	}
	
	public ControlParameter getDistanceBandwidth() {
		return distanceBandwidth;
	}

	public void setDistanceBandwidth(ControlParameter DistanceBandwidth) {
		this.distanceBandwidth = DistanceBandwidth;
	}

	@Override
	public void algorithmIteration() {
		//TO-DO: Make sure that all fitnesses are evaluated initially, and
		//that FE is incremented only once per iteration
		
		//calculate a new harmony
		Harmony newHarmony = new Harmony();
		newHarmony.initialise(getOptimisationProblem());
		Vector newHarmonyVector = (Vector) newHarmony.getContents();
		
		OptimisationProblem problem = getOptimisationProblem();
		Real newHarmonyValue;
		for (int i = 0; i < problem.getDomain().getDimension(); ++i) {
			if (random1.getUniform() < harmonyMemoryConsideringRate.getParameter()) {
				Harmony selectedHarmony = (Harmony) this.harmonyMemory.get((int) random2.getUniform(0, harmonyMemory.size()-1));
				Vector selectedHarmonyContents = (Vector) selectedHarmony.getContents();
				newHarmonyValue = (Real) selectedHarmonyContents.get(i);
				if (random1.getUniform() < pitchAdjustingRate.getParameter()) {
					double pitchedValue = newHarmonyValue.getReal() + random3.getUniform(-1, 1) * distanceBandwidth.getParameter();
					if ((pitchedValue > newHarmonyValue.getLowerBound()) && (pitchedValue < newHarmonyValue.getUpperBound()))
						newHarmonyValue.setReal(pitchedValue);
				}
				
				newHarmonyVector.set(i, new Real(newHarmonyValue));
			}
			else {
				double upper = ((AbstractList) problem.getDomain().getBuiltRepresenation()).getNumeric(i).getUpperBound();
				double lower = ((AbstractList) problem.getDomain().getBuiltRepresenation()).getNumeric(i).getLowerBound();
				newHarmonyVector.set(i, new Real(random3.getUniform(lower, upper)));
			}
		}
		
		newHarmony.calculateFitness();
		harmonyMemory.add(newHarmony);
		harmonyMemory.remove(harmonyMemory.getFirst()); // Remove the worst harmony in the memory
	}

	@Override
	public double accept(TopologyVisitor visitor) {
		visitor.setCurrentAlgorithm(this);
//		visitor.visit(algorithm)
		return 0;
	}

	@Override
	public OptimisationSolution getBestSolution() {
		return new OptimisationSolution(getOptimisationProblem(), this.harmonyMemory.getFirst().getContents());
	}

	@Override
	public List<OptimisationSolution> getSolutions() {
		return Arrays.asList(getBestSolution());
	}
}

