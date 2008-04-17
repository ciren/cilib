/*
 * Harmony.java
 *
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
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.calculator.VectorBasedFitnessCalculator;

/**
 * TODO: Complete this javadoc.
 *
 */
public class Harmony extends AbstractEntity {
	private static final long serialVersionUID = -4941414797957384798L;
	private VectorBasedFitnessCalculator calculator;
	
	public Harmony() {
		this.calculator = new VectorBasedFitnessCalculator();
	}
	
	public Harmony(Harmony copy) {
		super(copy);
		this.calculator = copy.calculator.getClone();
	}

	public void calculateFitness() {
		calculateFitness(true);
	}

	public void calculateFitness(boolean count) {
		Fitness fitness = calculator.getFitness(getContents(), count);
    	this.properties.put("fitness", fitness);
	}

	public int compareTo(Entity o) {
		return this.getFitness().compareTo(o.getFitness());
	}

	public Harmony getClone() {
		return new Harmony(this);
	}

	public int getDimension() {
		return getContents().getDimension();
	}

	public void initialise(OptimisationProblem problem) {
		Type harmony = problem.getDomain().getBuiltRepresenation().getClone();
		harmony.randomise();
		
		setContents(harmony);
		this.properties.put("fitness", InferiorFitness.instance());
	}

	public void reinitialise() {
		getContents().randomise();
	}

}
