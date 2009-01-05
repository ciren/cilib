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
package net.sourceforge.cilib.coevolution;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.coevolution.CompetitiveCoevolutionProblemAdapter;
import net.sourceforge.cilib.type.types.Int;



/**
 * @author Julien Duhain
 * 
 */
public class CompetitiveCoevolutionIterationStrategy extends CoevolutionIterationStrategy {

	private static final long serialVersionUID = 1061304146851715740L;
	protected OpponentSelectionStrategy opponentSelectionStrategy;
	protected FitnessSharingStrategy fitnessSharingStrategy;
	
	public CompetitiveCoevolutionIterationStrategy() {
		super();
		opponentSelectionStrategy = new SelectAllOpponentSelectionStrategy();
		fitnessSharingStrategy = new StandardFitnessSharingStrategy();
	}

	@Override
	public CompetitiveCoevolutionIterationStrategy getClone(){
		return new CompetitiveCoevolutionIterationStrategy(this);
	}
	
	public CompetitiveCoevolutionIterationStrategy(CompetitiveCoevolutionIterationStrategy copy){
		opponentSelectionStrategy = copy.opponentSelectionStrategy;
		fitnessSharingStrategy = copy.fitnessSharingStrategy;
	}
	
	@Override
	public void performIteration(CoevolutionAlgorithm ca) {		
		
		 for(PopulationBasedAlgorithm currentAlgorithm : ca.getPopulations()) {
			 //new round of competitions
			 ((CompetitiveCoevolutionProblemAdapter)currentAlgorithm.getOptimisationProblem()).incrementEvaluationround();			

             for(int i=0; i<currentAlgorithm.getPopulationSize(); i++){
				Entity e = currentAlgorithm.getTopology().get(i);
				CoevolutionEvaluationList opponents = opponentSelectionStrategy.setCompetitors(((Int)e.getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt(), ca.getPopulations());
				e.getProperties().put(EntityType.Coevolution.COMPETITOR_LIST, opponents); 
			}
			currentAlgorithm.performIteration();
		}
				 		
	}

	public FitnessSharingStrategy getFitnessSharingStrategy() {
		return fitnessSharingStrategy;
	}

	public void setFitnessSharingStrategy(
			FitnessSharingStrategy fitnessSharingStrategy) {
		this.fitnessSharingStrategy = fitnessSharingStrategy;
	}

	public OpponentSelectionStrategy getOpponentSelectionStrategy() {
		return opponentSelectionStrategy;
	}

	public void setOpponentSelectionStrategy(OpponentSelectionStrategy opponentSelectionStrategy) {
		this.opponentSelectionStrategy = opponentSelectionStrategy;
	}
}
