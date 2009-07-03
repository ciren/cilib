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
package net.sourceforge.cilib.games.agent.state.traversal;

import java.util.List;

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.StateGame;
import net.sourceforge.cilib.games.states.GameState;
//import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;

/**
 * @author leo
 * An implimentation of the min max state traversal strategy with the alpha beta pruning optimization.
 */
public class MinMaxAlphaBetaTraversalStrategy extends StateTraversalStrategy {
	private int decisionPlayerID;
	private static final long serialVersionUID = 2534588421107032419L;

	public MinMaxAlphaBetaTraversalStrategy() {
		decisionPlayerID = -1;
	}

	/**
	 * @param other
	 */
	public MinMaxAlphaBetaTraversalStrategy(MinMaxAlphaBetaTraversalStrategy other) {
		super(other);
		decisionPlayerID = other.decisionPlayerID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StateTraversalStrategy getClone() {
		return new MinMaxAlphaBetaTraversalStrategy(this);
	}


	/**
	 * return the min or max score for the specified depth of the tree.
	 * @param game the game being played
	 * @param maxPlayer a flag indicating if this is a MAX level. If it is false, it is assumed that it is a MIN level
	 * @param currentPlayerID The played who has to make the decision in the first place
	 * @param currentDepth the current depth of the tree
	 * @param alpha the current alpha value
	 * @param beta the current beta value
	 * @return the MIN or MAX score
	 */
	private double minMax(Game<GameState> game, boolean maxPlayer, int currentPlayerID, int currentDepth, double alpha, double beta){
		if(currentDepth == maxDepth || game.gameOver()){ //if the game is at an end state, or we are at the maximum depth, then use the evaluator to get a score for this state
			double score = evaluator.evaluateState(game, decisionPlayerID);
			return score;
		}
		//if not at a terminating node, generate all the possible states from this one
		Random rand = game.getCurrentState().getRandomizer().getGenerator();
		List<GameState> nextStates = ((StateGame)game).generateStates(currentPlayerID);
		double bestMoveValue = maxPlayer ? -999999 : 999999;
		int amStates = nextStates.size();
		for(int i = 0; i < amStates; ++i){
			//randomize the order in which the states are traversed
			int index = rand.nextInt(nextStates.size());
			//make this state current state of game object
			GameState state = nextStates.get(index);
			nextStates.remove(index);
			state.increaseIteration();
			Game<GameState> newGame = game.getClone(state);
			newGame.setCurrentPlayer(currentPlayerID);
			//get the move value for this state
			double moveValue = minMax(newGame, !maxPlayer, game.getNextPlayerID(currentPlayerID), currentDepth + 1, alpha, beta);

			if(maxPlayer){ //MAX Player
				if(moveValue > bestMoveValue){
					bestMoveValue = moveValue;
					if(moveValue > alpha)
						alpha = moveValue;
				}

				if(beta <= alpha){
					return bestMoveValue;
				}
			}
			else{//MIN Player
				if(moveValue < bestMoveValue){
					bestMoveValue = moveValue;
					if(moveValue < beta)
						beta = moveValue;
				}

				if(beta <= alpha){
					return bestMoveValue;
				}
			}
		}
		return bestMoveValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameState selectState(Game<GameState> game, int playerID) {
		Random rand = game.getCurrentState().getRandomizer().getGenerator();
		decisionPlayerID = playerID;
		//generate all the states from the current state
		List<GameState> startStates = ((StateGame)game).generateStates(playerID);
		double bestScore = -999999;
		GameState bestState = null;
		double alpha = -999999;
		double beta = 999999;
		int amStates = startStates.size();
		for(int i = 0; i < amStates; ++i){
			int index = rand.nextInt(startStates.size());
			GameState state = startStates.get(index);
			startStates.remove(index);
			state.increaseIteration();
			//clone the game with the specified state
			Game<GameState> newGame = game.getClone(state);
			//get the score for the state
			double score = minMax(newGame, false, newGame.getNextPlayerID(playerID), 1, alpha, beta);
			if(score > bestScore){
				bestScore = score;
				bestState = state;
				if(bestScore > alpha)
					alpha = bestScore;
			}
		}
		if(bestState == null) //no valid states could be generated, this can only mean that no decisions are available, and thus, select the current state
			return game.getCurrentState();
		return bestState;
	}

}
