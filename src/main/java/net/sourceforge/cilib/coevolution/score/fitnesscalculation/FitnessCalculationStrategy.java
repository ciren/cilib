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
package net.sourceforge.cilib.coevolution.score.fitnesscalculation;

import net.sourceforge.cilib.coevolution.score.EntityScoreboard;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
 * Defines how fitness values stored in the scoreboard are combined to get a single fitness value
 */
public abstract class FitnessCalculationStrategy implements Cloneable {
	private static final long serialVersionUID = -3708525082727758222L;
	/**
	 * The number of historic games to keep dictates how many previous competitions should be stored
	 */
	int amountHistoricGames;
	public FitnessCalculationStrategy() {
		amountHistoricGames = 0;
	}

	public FitnessCalculationStrategy(FitnessCalculationStrategy other){
		amountHistoricGames = other.amountHistoricGames;
	}

	/**
	 * Calculate the fitness from the scoreboard
	 * @param score the scoreboard
	 * @param currentRound current round of competition
	 * @return new fitness
	 */
	public abstract Fitness calculateFitnessFromScoreBoard(EntityScoreboard score, int currentRound);

	public int getAmountHistoricGames() {
		return amountHistoricGames;
	}

	public void setAmountHistoricGames(int amountHistoricGames) {
		this.amountHistoricGames = amountHistoricGames;
	}
}
