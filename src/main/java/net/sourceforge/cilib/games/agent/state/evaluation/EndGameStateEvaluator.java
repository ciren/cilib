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

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author leo
 * This is a {@linkplain StateEvaluator} that simply scores the state that results in a win for the decision player with a value of 1, and everything else with a value of -1.
 * This evaluator can only evaluate game states where the game is completed.
 */
public class EndGameStateEvaluator implements StateEvaluator {
	public EndGameStateEvaluator() {

	}
	public EndGameStateEvaluator(EndGameStateEvaluator other) {

	}
	/**
	 * {@inheritDoc}
	 */
	public double evaluateState(Game<GameState>  state, int decisionPlayerID) {
		if(!state.gameOver())
			throw new RuntimeException("This evaluator can only evaluate end game states");
		AbstractGameResult result = state.getGameResult();
		if(result instanceof WinGameResult){
			if(((WinGameResult)result).getWinnerID() == decisionPlayerID)
				return 1;
			else
				return -1;
		}
		else
			return 0; //draw
	}

	/**
	 * {@inheritDoc}
	 */
	public DomainRegistry getEvaluatorDomain() {
		throw new RuntimeException("This evaluator cannot be optomised");
	}

	/**
	 * {@inheritDoc}
	 */
	public void initializeEvaluator(Type evaluatorData) {
		throw new RuntimeException("This evaluator cannot be optomised");

	}

}
