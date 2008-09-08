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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Harmony getClone() {
		return new Harmony(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		
		if ((object == null) || (this.getClass() != object.getClass()))
			return false;
		
		Harmony other = (Harmony) object;
		return super.equals(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + super.hashCode();
		return hash;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void calculateFitness() {
		calculateFitness(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void calculateFitness(boolean count) {
		Fitness fitness = calculator.getFitness(this, count);
    	this.getProperties().put(EntityType.FITNESS, fitness);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Entity o) {
		return this.getFitness().compareTo(o.getFitness());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDimension() {
		return getCandidateSolution().getDimension();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialise(OptimisationProblem problem) {
		Type harmony = problem.getDomain().getBuiltRepresenation().getClone();
		harmony.randomise();
		
		setCandidateSolution(harmony);
		this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reinitialise() {
		getCandidateSolution().randomise();
	}

}
