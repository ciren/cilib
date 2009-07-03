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
package net.sourceforge.cilib.games.game.predatorprey;

import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.scoring.GameScoringStrategy;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.problem.MaximisationFitness;

/**
 * @author leo
 * This is a {@linkplain GameScoringStrategy} that will score a game of Predator Prey. It is score of 1 if the agent has one, and an additional value of 0.05 for every iteration of the game
 * the agent survived in the case of a Prey agent
 */
public class PredatorPreyGameScoringStrategy extends GameScoringStrategy {

    /**
     *
     */
    public PredatorPreyGameScoringStrategy() {

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeMeasurements(Game game) {
        // this scoring strategy does not require agent measurements
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void assignPlayerScores(Game game) {
        WinGameResult result = (WinGameResult)game.getGameResult();
        for(int i = 1; i <= 2; ++i){
            double score = 0;
            Agent p = game.getPlayer(i);
            if(result.getWinnerID() == i){
                score += 1;
            }
            if(p.getAgentToken().equals(GameToken.PredatorPrey.PREY)){
                score += game.getCurrentIteration() * 0.05;
            }

            game.assignPlayerScore(i, new MaximisationFitness(score));
        }
    }

}
