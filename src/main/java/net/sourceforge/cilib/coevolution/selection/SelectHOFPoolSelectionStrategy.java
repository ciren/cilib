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



import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.competitors.CoevolutionCompetitorList;
import net.sourceforge.cilib.coevolution.competitors.Competitor;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

/**
 * Maintains a HOF and adds the HOF {@linkplain Competitor}s to the pool
 * @author leo
 *
 */
public class SelectHOFPoolSelectionStrategy extends
		OpponentPoolSelectionStrategy {
	private static final long serialVersionUID = 4878959204065018821L;

	CoevolutionCompetitorList hOF;
	int addToHOFEpoch; //the epoch at which a new Competitor will be added to the HOF
	int hOFSize; //The max size of the HOF
	int lasIterationAdded[]; //Coutners to ensure Competitors are only added once at the specified epoch
	/**
	 *
	 */
	public SelectHOFPoolSelectionStrategy() {
		hOFSize = 50;
		addToHOFEpoch = 10;
		hOF = new CoevolutionCompetitorList(hOFSize);
		lasIterationAdded = new int[0];
	}
	public SelectHOFPoolSelectionStrategy(SelectHOFPoolSelectionStrategy other) {
		super(other);
		hOFSize = other.hOFSize;
		addToHOFEpoch = other.addToHOFEpoch;
		hOF = other.hOF.getClone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToCompetitorPool(CoevolutionCompetitorList pool,
			List<PopulationBasedAlgorithm> populations) {
		if(lasIterationAdded.length == 0)
			lasIterationAdded = new int[populations.size()];
		for(PopulationBasedAlgorithm algorithm: populations){
			int currIteration = algorithm.getIterations();
			int pID = ((Int)algorithm.getTopology().get(0).getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt();
			if(currIteration != 0 && currIteration != lasIterationAdded[pID - 1] && currIteration % addToHOFEpoch == 0){

				Fitness bestFit = null;
				Type solution = null;
				for(Entity e: algorithm.getTopology()){
					if(bestFit == null || e.getFitness().compareTo(bestFit) > 0){
						bestFit = e.getFitness();
						solution = e.getCandidateSolution();
					}
				}
				hOF.addCompetitor(new Competitor(solution, bestFit, pID));
				lasIterationAdded[pID - 1] = currIteration;
			}
		}
		pool.merge(hOF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SelectHOFPoolSelectionStrategy getClone() {
		return new SelectHOFPoolSelectionStrategy(this);
	}

	public void setAddToHOFEpoch(int addToHOFEpoch) {
		this.addToHOFEpoch = addToHOFEpoch;
	}

	public void setHOFSize(int size) {
		hOFSize = size;
		hOF = new CoevolutionCompetitorList(hOFSize);
	}

}