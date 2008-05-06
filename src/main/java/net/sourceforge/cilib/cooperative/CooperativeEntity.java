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
package net.sourceforge.cilib.cooperative;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.VectorBasedFitnessCalculator;

/**
 * 
 * @author Theuns Cloete
 */
public class CooperativeEntity extends AbstractEntity {
	private static final long serialVersionUID = -8298684370426283216L;
	
	protected Vector context = null;
	protected Fitness fitness = null;
	protected FitnessCalculator fitnessCalculator;

	public CooperativeEntity() {
		context = new Vector();
		fitness = InferiorFitness.instance();
		fitnessCalculator = new VectorBasedFitnessCalculator();
	}
	
	public CooperativeEntity(CooperativeEntity rhs) {
		context = rhs.context.getClone();
		fitness = rhs.fitness;
		fitnessCalculator = rhs.fitnessCalculator;//.clone();
	}

	public CooperativeEntity getClone() {
		return new CooperativeEntity(this);
	}
	
	public void reset() {
		context.clear();
	}

	public int compareTo(Entity o) {
		return getFitness().compareTo(o.getFitness());
	}

	public void append(Type value) {
		if(value instanceof Vector)
			context.append((Vector)value);
		else
			context.append(value);
	}
	
	public void append(Entity entity) {
		append(entity.getContents());
	}
	
	public void update(Entity src, int srcPos, int dstPos, int length) {
		for(int i = dstPos; i < dstPos + length; ++i) {
			context.setReal(i, ((Vector)src.getContents()).getReal(srcPos + i - dstPos));
		}
	}
	
	public Type getContents() {
		return context;
	}

	public void setContents(Type type) {
		context.clear();
		append(type);
	}

	public int getDimension() {
		return context.getDimension();
	}

	public void setDimension(int dim) {
		throw new UnsupportedOperationException("The dimension of a CooperativeEntity is determined by its context");
	}

	public Fitness getFitness() {
		return fitness;
	}

	public void setFitness(Fitness f) {
		fitness = f;
	}

	public String getId() {
		throw new UnsupportedOperationException("If you want to use this, implement it");
	}
	
	public void setId(String i) {
		throw new UnsupportedOperationException("If you want to use this, implement it");
	}

	public void initialise(OptimisationProblem problem) {
		context = (Vector) problem.getDomain().getBuiltRepresenation().getClone();
	}

	public void reinitialise() {
		// TODO Auto-generated method stub
		
	}
	
	public void calculateFitness() {
		calculateFitness(true);
	}

	public void calculateFitness(boolean count) {
		fitness = fitnessCalculator.getFitness(context, count);
	}
}
