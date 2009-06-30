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
package net.sourceforge.cilib.games.game;

import java.util.List;
import net.sourceforge.cilib.games.agent.state.StateEvaluationAgent;
import net.sourceforge.cilib.games.states.GameState;

/**
 * @author leo
 * This interface should be extended by games that can be optimized by agents that expand every possible game state and select the best one.
 * This implies the use of {@linkplain StateEvaluationAgent}s
 */
public interface StateGame {
    /**
     * This method generates every possible from the current state for a specific player
     * @param currentPlater the player whos turn it is
     * @return a list of possible game states
     */
    public List<GameState> generateStates(int currentPlater);
}
