/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.games.agent.state;


import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.agent.state.traversal.StateTraversalStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.StateGame;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author leo
 * This class represents all agents that function as state evaluators.
 * A state evaluator agent makes a decision by ranking each possible game state and choosing the one with the highest rank
 */


public class StateEvaluationAgent extends Agent {

	private static final long serialVersionUID = 3576769226785412047L;
	//The traversal strategy determines how the state space is traversed
	StateTraversalStrategy traversalStrategy;
	public StateEvaluationAgent() {
		super();
	}

	/**
	 * Copy constructor
	 * @param other
	 */
	public StateEvaluationAgent(StateEvaluationAgent other) {
		super(other);
		traversalStrategy = other.traversalStrategy.getClone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StateEvaluationAgent getClone() {
		return new StateEvaluationAgent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void move(Game<GameState>  game) {
		if(!(game instanceof StateGame))
			throw new RuntimeException("State evaluation agents can only be used on state based games");
		game.setCurrentGameState(traversalStrategy.selectState(game, playerID));
	}

	public StateTraversalStrategy getTraversalStrategy() {
		return traversalStrategy;
	}

	public void setTraversalStrategy(StateTraversalStrategy traversalStrategy) {
		this.traversalStrategy = traversalStrategy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DomainRegistry getAgentDomain() {
		return traversalStrategy.getEvaluator().getEvaluatorDomain();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeAgent(Type agentData) {
		traversalStrategy.getEvaluator().initializeEvaluator(agentData);
	}

}
