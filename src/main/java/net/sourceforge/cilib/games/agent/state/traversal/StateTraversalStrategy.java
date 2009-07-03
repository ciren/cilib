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

import net.sourceforge.cilib.games.agent.state.StateEvaluationAgent;
import net.sourceforge.cilib.games.agent.state.evaluation.StateEvaluator;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
 * This strategy determines how the state space for a game is traversed when the {@linkplain StateEvaluationAgent} needs to make a decision.
 */
public abstract class StateTraversalStrategy implements Cloneable {
	//The evaluator that is used to rank the explored states
	protected StateEvaluator evaluator;
	//The maximim tree depth that the strategy should look into
	protected int maxDepth;
	public StateTraversalStrategy() {
		maxDepth = 1;
	}
	public StateTraversalStrategy(StateTraversalStrategy other){
		evaluator = other.evaluator;
		maxDepth = other.maxDepth;
	}

	/**
	 * Select a {@linkplain GameState} from the given game objects list of possible states for the specified player.
	 * @param game the game at the state when the decision should be made
	 * @param playerID the ID of the player that needs to make a decision
	 * @return The selected {@linkplain GameState}
	 */
	public abstract GameState selectState(Game<GameState> game, int playerID);

	/**
	 * {@inheritDoc}
	 */
	public abstract StateTraversalStrategy getClone();
	public void setEvaluator(StateEvaluator evaluator) {
		this.evaluator = evaluator;
	}
	public StateEvaluator getEvaluator(){
		return evaluator;
	}
	public int getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
