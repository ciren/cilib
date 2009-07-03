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

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.competitors.CoevolutionCompetitorList;
import net.sourceforge.cilib.coevolution.competitors.Competitor;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
import net.sourceforge.cilib.type.types.Int;

/**
 * Add all the populations personal best vectors to the opponent pool, this can only be used on {@linkplain Algorithm}s that optimise {@linkplain Particle}s
 * @author leo
 *
 */
public class SelectpBestSolutionsPoolSelectionStrategy extends
		OpponentPoolSelectionStrategy {
	private static final long serialVersionUID = 8353244137335633101L;

	public SelectpBestSolutionsPoolSelectionStrategy() {

	}

	public SelectpBestSolutionsPoolSelectionStrategy(SelectpBestSolutionsPoolSelectionStrategy other) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToCompetitorPool(CoevolutionCompetitorList pool,
		List<PopulationBasedAlgorithm> populations) {
		for(PopulationBasedAlgorithm algorithm: populations){
			int populationID = -1;
			List<Competitor> competitors = new ArrayList<Competitor>();
			for(Entity e: algorithm.getTopology()){
				if(populationID == -1){
					populationID = ((Int)e.getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt();
				}
				if(!(e instanceof AbstractParticle))
					break;
				competitors.add(new Competitor(((AbstractParticle)e).getBestPosition(), e.getBestFitness(), populationID));
			}
			if(competitors.size() > 0)
				pool.addCompetitorList(populationID, competitors);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SelectpBestSolutionsPoolSelectionStrategy getClone() {
		return new SelectpBestSolutionsPoolSelectionStrategy(this);
	}
}
