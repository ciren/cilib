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
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionEvaluationList;
import net.sourceforge.cilib.coevolution.CompetitiveCoevolutionIterationStrategy;
import net.sourceforge.cilib.coevolution.score.EntityScoreboard;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * measurement that prints the number of victory of the best Entity.
 * @author  Julien Duhain
 */
public class CoevolutionFitness implements Measurement{
	private static final long serialVersionUID = 2724661105974842752L;

	/** Creates a new instance of Fitness. */
	public CoevolutionFitness() {

	}

	public CoevolutionFitness(CoevolutionFitness copy) {

	}

	public CoevolutionFitness getClone() {
		return new CoevolutionFitness(this);
	}

	public String getDomain() {
		return "Z";
	}

	public Type getValue(Algorithm algorithm) {
 		Vector populationFitnesses = new Vector();
  		CoevolutionAlgorithm ca = (CoevolutionAlgorithm) algorithm;
 		int popID = 1;

 		for(PopulationBasedAlgorithm currentAlgorithm : ca) {
 			OptimisationSolution solution = currentAlgorithm.getBestSolution();
 			CoevolutionEvaluationList competitors = ((CompetitiveCoevolutionIterationStrategy)ca.getCoevolutionIterationStrategy()).getOpponentSelectionStrategy().setCompetitors(popID, ca.getPopulations());
 			//	((CompetitiveCoevolutionProblemAdapter)currentAlgorithm.getOptimisationProblem()).incrementEvaluationround(); //???
 			Blackboard<Enum<?>, Type> blackboard = new Blackboard<Enum<?>, Type>();
 			blackboard.put(EntityType.CANDIDATE_SOLUTION, solution.getPosition());
 			blackboard.put(EntityType.Coevolution.COMPETITOR_LIST, competitors);
 			blackboard.put(EntityType.Coevolution.BOARD, new EntityScoreboard());
 			Fitness val = currentAlgorithm.getOptimisationProblem().getFitness(blackboard, false);
 			//PopulationFitnesses.add(new Int(popID));
 			populationFitnesses.add(new Real(val.getValue()));
 			++popID;
		}
 		return populationFitnesses;
  	}

}
