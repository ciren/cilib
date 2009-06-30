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
package net.sourceforge.cilib.games.agent.state.evaluation;

import net.sourceforge.cilib.games.agent.state.StateEvaluationAgent;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author leo
 * This interface defines the methods required to evaluate a given state for a {@linkplain StateEvaluationAgent}. It is used to determine
 * which state to select when making a decision.
 */
public interface StateEvaluator {
	/**
	 * Evaluate a given {@linkplain GameState} for a given player ID.
	 * @param state the {@linkplain Game} object that is at a specific state that needs to be evaluated
	 * @param decisionPlayerID The player that has to make a decision
	 * @return the rating of the state.
	 */
	double evaluateState(Game<GameState> state, int decisionPlayerID);
	/**
	 * Some state evaluators can be optimised, and therefore the evaluator should be initialized with evaluator specific data.
	 * @param evaluatorData the data that determines how the evaluator should funciton
	 */
	void initializeEvaluator(Type evaluatorData);
	/**
	 * If the evaluator can be optimized then the evaluator data should be in a specific domain, this function should return that domain
	 * @return The domain of the evaluator
	 */
	DomainRegistry getEvaluatorDomain();
}
