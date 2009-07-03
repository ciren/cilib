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
import net.sourceforge.cilib.util.Cloneable;


/**
 * This class determins how opponents are selected from the opponent pool. A List of {@linkplain OpponentPoolSelectionStrategy}'s is maintained by
 * this class to dictate how the opponent pool is selected.
 * @author Julien Duhain
 * @author leo
 */
public abstract class OpponentSelectionStrategy implements Cloneable{
	List<OpponentPoolSelectionStrategy> poolSelection;
	protected int ownPopulationID; //if the entity can select from its own population you can use a different population ID for those Competitors
	protected boolean selectFromOwnPopulation; //flag to determine if an entity can select opponents from its own population
	public OpponentSelectionStrategy()
	{
		poolSelection = new ArrayList<OpponentPoolSelectionStrategy>();
		poolSelection.add(new SelectAllSolutionsPoolSelectionStrategy());
		ownPopulationID = -1;
		selectFromOwnPopulation = false;
	}

	public OpponentSelectionStrategy(OpponentSelectionStrategy copy) {
		ownPopulationID = copy.ownPopulationID;
		poolSelection = new ArrayList<OpponentPoolSelectionStrategy>();
		for(OpponentPoolSelectionStrategy selection: copy.poolSelection){
			poolSelection.add(selection.getClone());
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract OpponentSelectionStrategy getClone();
	/**
	 * Select opponents from the pool
	 * @param pool the pool of potential opponents
	 * @return the selected opponents
	 */
	public abstract CoevolutionCompetitorList selectCompetitors(CoevolutionCompetitorList pool);
	/**
	 * Creates the opponent pool with an arbitrary number of {@linkplain OpponentPoolSelectionStrategy}s and
	 * select opponents from it.
	 * @param populationID id of current pop
	 * @param populations the sub populations of the competitive coevolution algorithm
	 * @return list of selected opponents
	 */
	public CoevolutionCompetitorList setCompetitors(int currentPopulationID, List<PopulationBasedAlgorithm> populations){
		CoevolutionCompetitorList competitorPool = new CoevolutionCompetitorList();
		//get the competition pool
		for(OpponentPoolSelectionStrategy selection: poolSelection){
			selection.addToCompetitorPool(competitorPool, populations);
		}
		//pre-process the pool, remove own population or substitute the populationID depending on the member values.
		for(int i = 0; i < competitorPool.getNumberOfLists(); ++i){
			if(currentPopulationID == competitorPool.getPopulationID(i)){
				if(!selectFromOwnPopulation){
					competitorPool.removeSubList(i);
				}else if(ownPopulationID != -1){
					competitorPool.updatePopulationID(i, currentPopulationID, ownPopulationID);
				}
			}
		}
		//Select opponents from the pool.
		return selectCompetitors(competitorPool);
	}

	public void setOwnPopulationID(int ownPopulationID) {
		this.ownPopulationID = ownPopulationID;
	}

	public void setSelectFromOwnPopulation(boolean selectFromOwnPopulation) {
		this.selectFromOwnPopulation = selectFromOwnPopulation;
	}

	public void addPoolSelectionStrategy(OpponentPoolSelectionStrategy strategy){
		poolSelection.add(strategy);
	}

	public void clearPoolSelectionStrategies(){
		poolSelection.clear();
	}
}
