/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.coevolution;

import java.util.List;
import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.Int;

/**
 * example of implementation of an OpponentSelectionStrategy
 * @author Julien Duhain
 *
 */
public class SelectAllOpponentSelectionStrategy extends OpponentSelectionStrategy{
	//id of population used when assigning players from own population. if left to -1 then it'l be the population id of the entity
	int ownPopulationID;

	public SelectAllOpponentSelectionStrategy(){
		super();
		ownPopulationID = -1;
	}

	public SelectAllOpponentSelectionStrategy(SelectAllOpponentSelectionStrategy copy){
		ownPopulationID = copy.ownPopulationID;
	}

	public SelectAllOpponentSelectionStrategy getClone() {
		return new SelectAllOpponentSelectionStrategy(this);
	}

	/**
	 * @param pool the pool of potential opponents
	 * @return all entities from all populations in one list
	 */
	public CoevolutionEvaluationList setCompetitors(int populationID, List<PopulationBasedAlgorithm> pool) {
			CoevolutionEvaluationList opponents = new CoevolutionEvaluationList();

			for(PopulationBasedAlgorithm algorithm: pool){
				List<EvaluationEntity> algorithmCompetitors = new ArrayList<EvaluationEntity>();
				for(int i=0; i < algorithm.getPopulationSize(); i++){
					Entity e =algorithm.getTopology().get(i);
					int pID = ((Int)e.getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt();
					if(pID == populationID && ownPopulationID != -1)
						pID = ownPopulationID;
					algorithmCompetitors.add(new EvaluationEntity(e.getCandidateSolution(), pID));
				}
				opponents.addEntityList(algorithmCompetitors);
			}

		return opponents;
	}

	public void setOwnPopulationID(int ownPopulationID) {
		this.ownPopulationID = ownPopulationID;
	}
}
