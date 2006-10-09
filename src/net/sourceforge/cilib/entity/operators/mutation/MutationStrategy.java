/*
 * MutationStrategy.java
 * 
 * Created on Apr 1, 2006
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;

import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.RandomNumber;

/**
 * 
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public abstract class MutationStrategy {
	
	private ControlParameterUpdateStrategy mutationProbability;
	private RandomNumber randomNumber;
	private String operator;
	private MutationOperatorStrategy operatorStrategy;
	
		
	public MutationStrategy() {
		operator = "+";
		operatorStrategy = new AdditionMutationOperatorStrategy();
		
		mutationProbability = new ConstantUpdateStrategy(0.03);
		randomNumber = new RandomNumber();
	}
	
	
	public abstract void mutate(List<? extends Entity> entity);

	
	/**
	 * 
	 * @return
	 */
	public ControlParameterUpdateStrategy getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * 
	 * @param mutationProbability
	 */
	public void setMutationProbability(
			ControlParameterUpdateStrategy mutationProbability) {
		this.mutationProbability = mutationProbability;
	}


	public RandomNumber getRandomNumber() {
		return randomNumber;
	}


	public void setRandomNumber(RandomNumber randomNumber) {
		this.randomNumber = randomNumber;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
		
		MutationOperatorFactory factory = new MutationOperatorFactory();
		this.operatorStrategy = factory.getOperatorStrategy(operator);
	}
	
	
	public MutationOperatorStrategy getOperatorStrategy() {
		return this.operatorStrategy;
	}
	
	
}
