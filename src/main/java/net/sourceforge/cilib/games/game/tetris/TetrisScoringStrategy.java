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
package net.sourceforge.cilib.games.game.tetris;

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.scoring.GameScoringStrategy;
import net.sourceforge.cilib.games.measurement.AgentMeasure;
import net.sourceforge.cilib.games.result.ScoreGameResult;
import net.sourceforge.cilib.games.states.GridGameState;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Long;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author leo
 * This is an scoring strategy for the game of tetris. In this strategy certain features are measured and used
 * in the score as well as the number of lines cleared
 */
public class TetrisScoringStrategy extends GameScoringStrategy {

	public TetrisScoringStrategy() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void assignPlayerScores(Game game) {
		int Width = ((GridGameState)game.getCurrentState()).getGridWidth();
		int Height = ((GridGameState)game.getCurrentState()).getGridHeight();

		double amountLines = ((ScoreGameResult)game.getGameResult()).getHighScore();
		Vector measuredData = (Vector)((AgentMeasure)game.getAgentMeasurements().get(0)).getMeasuredData();
		int counter = ((Int)measuredData.get(0)).getInt();
		long holesInv = ((long)(Width * (Height - 1)) * counter) - ((Long)measuredData.get(1)).getLong();
		long rowTransInv = ((long)(Width * Height) * counter) - ((Long)measuredData.get(2)).getLong();
		long colTransInv = ((long)(Width * Height) * counter) - ((Long)measuredData.get(3)).getLong();
		long heightInv = ((long)Height * counter) - ((Long)measuredData.get(4)).getLong();

		double holeVal = ((double)holesInv / ((long)(Width * (Height - 1)) * counter)) * 500.0;
		double rowTransVal = ((double)rowTransInv / ((long)(Width * Height) * counter)) * 500.0;
		double colTransVal = ((double)colTransInv / ((long)(Width * Height) * counter)) * 500.0;
		double heightVal = ((double)heightInv / ((long)(Width * Height) * counter)) * 500.0;
		double Fitness = amountLines + holeVal + rowTransVal + colTransVal + heightVal;
		game.assignPlayerScore(1, new MaximisationFitness(Fitness));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeMeasurements(Game game) {
		game.clearMeasurements();
		game.addMeasurement(new AveTetrisFeaturesMeasure());
	}

}
