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

import java.util.ArrayList;

import net.sourceforge.cilib.coevolution.score.EntityScoreboard;
import net.sourceforge.cilib.problem.Fitness;

/**
 * This Fitness calulation strategy simply gets the average score attained regardless of win/lose or draw as the Fitness value.
 * @author leo
 *
 */
public class AveFitnessCalculationStrategy extends FitnessCalculationStrategy {
	private static final long serialVersionUID = 7573830125196829386L;

	public AveFitnessCalculationStrategy() {
	}


	public AveFitnessCalculationStrategy(FitnessCalculationStrategy other) {
		super(other);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fitness calculateFitnessFromScoreBoard(EntityScoreboard score,
			int currentRound) {
		ArrayList<Fitness> values = new ArrayList<Fitness>();
		values.addAll(score.getScores(currentRound));
		//get the ave
		double ave = 0.0;
		for(Fitness val: values){
			ave += val.getValue().doubleValue();
		}
		ave /= values.size();
		//set the value to the new fitness
		return values.get(0).newInstance(new Double(ave));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getClone() {
		// TODO Auto-generated method stub
		return new AveFitnessCalculationStrategy(this);
	}

}
