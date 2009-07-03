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
package net.sourceforge.cilib.games.game.scoring;

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.DrawResult;
import net.sourceforge.cilib.games.result.ScoreGameResult;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.problem.MaximisationFitness;

/**
 * @author leo
 * This class assigns a fitness based upon the win/lose/draw result of the game and a value assosiated with each outcome.
 * If the outcome is a {@linkplain ScoreGameResult} then assign the score attained as the fitness
 */
public class WinLoseDrawValueScoringStrategy extends GameScoringStrategy {
    Double winValue;
    Double loseValue;
    Double drawValue;
    public WinLoseDrawValueScoringStrategy() {
        winValue = 1.0;
        loseValue = -2.0;
        drawValue = 0.0;
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
        AbstractGameResult result = game.getGameResult();
        if(result instanceof DrawResult){
            for(int i = 1; i <= game.getPlayerCount(); ++i)
                game.assignPlayerScore(i, new MaximisationFitness(drawValue));

        }
        else if (result instanceof WinGameResult){
            int winnerID = ((WinGameResult)result).getWinnerID();
            game.assignPlayerScore(winnerID, new MaximisationFitness(winValue));
            for(int i = 1; i <= game.getPlayerCount(); ++i)
                if(i != winnerID)
                    game.assignPlayerScore(i, new MaximisationFitness(loseValue));
        }
        else{ //score game result
            for(int i = 1; i <= game.getPlayerCount(); ++i)
                game.assignPlayerScore(i, new MaximisationFitness(((ScoreGameResult)result).getHighScore()));
        }
    }

    public Double getDrawValue() {
        return drawValue;
    }

    public void setDrawValue(Double drawValue) {
        this.drawValue = drawValue;
    }

    public Double getLoseValue() {
        return loseValue;
    }

    public void setLoseValue(Double loseValue) {
        this.loseValue = loseValue;
    }

    public Double getWinValue() {
        return winValue;
    }

    public void setWinValue(Double winValue) {
        this.winValue = winValue;
    }

}
