/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.problem.coevolution;

import net.sourceforge.cilib.coevolution.score.fitnesscalculation.FitnessCalculationStrategy;
import net.sourceforge.cilib.coevolution.score.fitnesscalculation.TotalFitnessCalculationStrategy;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;

/**
 * @author leo
 * This class represents all kinds of problems where an entity's fitness is determined by performing
 * a task a number of times.
 * For example when a game player is optimized the fitness is determined by playing the game a number of times and a score
 * is assigned based on the entitie's performance in the game.
 */
public abstract class PerformanceEvaluationOptimizationProblem extends
		OptimisationProblemAdapter {
	private static final long serialVersionUID = -736481594565770996L;

	protected int numberOfEvaluations;
	//this class determines how the fitness values from each evaluation will be combined
	protected FitnessCalculationStrategy fitnessCalculation;
	//scoring strategy
	public PerformanceEvaluationOptimizationProblem() {
		numberOfEvaluations = 1;
		fitnessCalculation = new TotalFitnessCalculationStrategy();
	}

	/**
	 * Copy constructor
	 * @param copy
	 */
	public PerformanceEvaluationOptimizationProblem(
			PerformanceEvaluationOptimizationProblem copy) {
		super(copy);
		numberOfEvaluations = copy.numberOfEvaluations;
		fitnessCalculation = copy.fitnessCalculation;
	}

	public void setFitnessCalculation(FitnessCalculationStrategy fitnessCalculation) {
		this.fitnessCalculation = fitnessCalculation;
	}

	public int getNumberOfEvaluations() {
		return numberOfEvaluations;
	}

	public void setNumberOfEvaluations(int amountEvaluations) {
		this.numberOfEvaluations = amountEvaluations;
	}
}
