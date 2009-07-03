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
package net.sourceforge.cilib.coevolution.selection;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.competitors.CoevolutionCompetitorList;
import net.sourceforge.cilib.coevolution.competitors.Competitor;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.Int;

/**
 * Adds all the {@linkplain Entity}s current positions to the pool of potentail opponents
 * @author leo
 *
 */
public class SelectAllSolutionsPoolSelectionStrategy extends
		OpponentPoolSelectionStrategy {
	private static final long serialVersionUID = 6891339409251879876L;

	public SelectAllSolutionsPoolSelectionStrategy() {
	}

	public SelectAllSolutionsPoolSelectionStrategy(SelectAllSolutionsPoolSelectionStrategy other) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToCompetitorPool(CoevolutionCompetitorList pool, List<PopulationBasedAlgorithm> populations){
		for(PopulationBasedAlgorithm algorithm: populations){
				int populationID = -1;
				List<Competitor> competitors = new ArrayList<Competitor>();
				for(Entity e: algorithm.getTopology()){
					if(populationID == -1)
						populationID = ((Int)e.getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt();
					competitors.add(new Competitor(e.getCandidateSolution(), e.getFitness(), populationID));
				}
				pool.addCompetitorList(populationID, competitors);
			}
		}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SelectAllSolutionsPoolSelectionStrategy getClone() {
		return new SelectAllSolutionsPoolSelectionStrategy(this);
	}
}
