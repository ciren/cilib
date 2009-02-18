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
package net.sourceforge.cilib.games.agent.stateagent;

import net.sourceforge.cilib.games.agent.Agent;

/**
 * @author leo
 * This class represents all agents that function as state evaluators.
 * A state evaluator agent makes a decision by ranking each possible game state and choosing the one with the highest rank
 */

//TODO complete this area of the games framework
public abstract class StateEvaluationAgent extends Agent {
	private static final long serialVersionUID = 3576769226785412047L;

	/**
	 * @param playerNo
	 */
	public StateEvaluationAgent() {
		super();

	}

	/**
	 * @param other
	 */
	public StateEvaluationAgent(Agent other) {
		super(other);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.agent.Agent#getClone()
	 */
	@Override
	public Agent getClone() {
		// TODO Auto-generated method stub
		return null;
	}

}
