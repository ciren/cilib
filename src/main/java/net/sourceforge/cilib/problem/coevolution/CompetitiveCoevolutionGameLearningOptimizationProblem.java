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


import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.CompetitiveCoevolutionIterationStrategy;
import net.sourceforge.cilib.coevolution.competitors.CoevolutionCompetitorList;
import net.sourceforge.cilib.coevolution.competitors.Competitor;
import net.sourceforge.cilib.coevolution.score.EntityScore;
import net.sourceforge.cilib.coevolution.score.EntityScoreboard;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.calculator.PropertyBasedFitnessCalculator;

/**
 * This class is used by {@linkplain CoevolutionAlgorithm} to optimize a {@linkplain Game}.
 * @author leo
 */
public class CompetitiveCoevolutionGameLearningOptimizationProblem extends
		GameLearningOptimizationProblem implements
		CoevolutionOptimisationProblem {
	private static final long serialVersionUID = 8633162433294415179L;

	public CompetitiveCoevolutionGameLearningOptimizationProblem() {
	}

	/**
	 * Copy constructor
	 * @param copy
	 */
	public CompetitiveCoevolutionGameLearningOptimizationProblem(
			CompetitiveCoevolutionGameLearningOptimizationProblem copy) {
		super(copy);		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fitness evaluateEntity(int populationID, int evaluationRound, Blackboard<Enum<?>, Type> entityData) {
		//need to determine the amount of players required for the game
		//select an opponent from each pool of opponents
		//play!
		game.initializeAgent(populationID, entityData.get(EntityType.CANDIDATE_SOLUTION));
		int competitorGroup = 1;
		EntityScoreboard scoreBoard = (EntityScoreboard)entityData.get(EntityType.Coevolution.BOARD);
		if(fitnessCalculation.getAmountHistoricGames() == 0){ // dont keep a history
			scoreBoard.clearScoreBoard();
		}
		else if(evaluationRound > fitnessCalculation.getAmountHistoricGames()){
			scoreBoard.removeScores(evaluationRound - fitnessCalculation.getAmountHistoricGames());
		}
		CoevolutionAlgorithm ca = (CoevolutionAlgorithm)AbstractAlgorithm.getAlgorithmList().get(0);
		CoevolutionCompetitorList entities = ((CompetitiveCoevolutionIterationStrategy)ca.getCoevolutionIterationStrategy()).selectOpponents(populationID, ca);
		for(int i = 0; i < entities.getNumberOfEntitesPerList(); ++i)
		{
			List<Competitor> competitors = entities.getCompetitorsFromSubList(i); //get the current opponents for this round
			EntityScore gameScores = new EntityScore(evaluationRound, competitorGroup);
			//initialize each opponent in the game. set the contents of the entitiy to the contents of the agent it represents
			for(Competitor e: competitors)
			{
				game.initializeAgent(e.getPopulationID(), e.getEntityData());
			}
			playGame(populationID, gameScores); //play the game a number of times. save the scores in gameScores
			scoreBoard.mergeEntityScore(gameScores); //put these scores in the scoreboard
			++competitorGroup;
		}
		//fitness assignment.calculateFitness(scoreBoard, currentEvalutionRound);
		return fitnessCalculation.calculateFitnessFromScoreBoard(scoreBoard, evaluationRound);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Fitness calculateFitness(Type solution) {
		throw new RuntimeException("This method should not be called for this class");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeEntities(PopulationBasedAlgorithm pba,
			int populationID) {

		//if before algorithm.initialize then this
		Entity sp = pba.getInitialisationStrategy().getEntityType().getClone();
		sp.getProperties().put(EntityType.Coevolution.BOARD, new EntityScoreboard());
		sp.getProperties().put(EntityType.Coevolution.POPULATION_ID, new Int(populationID));
		//sp.getProperties().put(EntityType.Coevolution.COMPETITOR_LIST, new CoevolutionCompetitorList());
		/*change the fitness calculator to be of type property based and not vector based
		if the fitness calculator object can be generic to these entity types then this code can be generic, otherwise we are limited to what is
		specified here
		*/
		if(sp instanceof AbstractEntity)
			((AbstractEntity)sp).setFitnessCalculator(new PropertyBasedFitnessCalculator());
		else
			throw new RuntimeException("Invalid entity type, entity must inherit from Abstract Entity to support Fitness Calculator");


		pba.getInitialisationStrategy().setEntityType(sp.getClone());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAmountSubPopulations() {
		return game.getPlayerCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DomainRegistry getSubPopulationDomain(int populationNo) {
		return game.getDomainForPlayer(populationNo);
	}

}
