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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.OpponentSelectionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Int;

public class SelectNCurrentPBestOpponentSelectionStrategy extends OpponentSelectionStrategy {
	
	protected int numberOfOpponents;
	protected RandomNumber random;
	protected boolean pickFromOwnPopulation;
	//population id for players selected from own population cannot be the same as the player being evaluated, otherwise it will ovverwrite that player in the problem in the case of game optimization
	protected int ownPopulationID;
	
	
	public int getNumberOfOpponents() {
		return numberOfOpponents;
	}

	public void setNumberOfOpponents(int numberOfOpponents) {
		this.numberOfOpponents = numberOfOpponents;
	}

	public SelectNCurrentPBestOpponentSelectionStrategy(){
		numberOfOpponents = 5;
		random = new RandomNumber();
		pickFromOwnPopulation = false;
		ownPopulationID = -1;
	}
	
	public SelectNCurrentPBestOpponentSelectionStrategy(SelectNCurrentPBestOpponentSelectionStrategy copy){
		this.numberOfOpponents = copy.numberOfOpponents;
		this.pickFromOwnPopulation = copy.pickFromOwnPopulation;
		this.ownPopulationID = copy.ownPopulationID;
		this.random = copy.random;
	}
	
	@Override
	public OpponentSelectionStrategy getClone() {
		return new SelectNCurrentPBestOpponentSelectionStrategy(this);
	}

	@Override
	public CoevolutionEvaluationList setCompetitors(int populationID, List<PopulationBasedAlgorithm> pool) {
		CoevolutionEvaluationList opponents = new CoevolutionEvaluationList();
	
		for(PopulationBasedAlgorithm algorithm: pool){
			
			//get first entity to perform some checks
			Entity e = algorithm.getTopology().get(0);
			
			if(pickFromOwnPopulation || ((Int)e.getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt() != populationID){ //select opponents from this pop
				
				int pID = ((Int)e.getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt();
				
				if(pickFromOwnPopulation && pID == populationID && ownPopulationID != -1) //if picking from own and I need to substitute popId with another one then do it
					pID = ownPopulationID;
				
				List<EvaluationEntity> potentialOpponents = new ArrayList<EvaluationEntity>();
				
				for(int i=0; i< algorithm.getPopulationSize(); i++){
					e = algorithm.getTopology().get(i);
					//not picking from my pop and this pop is my pop then break;
					potentialOpponents.add(new EvaluationEntity(e.getCandidateSolution(), pID));		
					if(e instanceof StandardParticle){
						potentialOpponents.add(new EvaluationEntity(((StandardParticle)e).getBestPosition(), pID));
					}
				}
				
				List<EvaluationEntity> selectedOpponents = new ArrayList<EvaluationEntity>();
				for(int i=0; i<numberOfOpponents;i++){
					int selected = (int)random.getUniform(0, potentialOpponents.size());
					selectedOpponents.add(potentialOpponents.get(selected));
					potentialOpponents.remove(selected);
				}
				if(selectedOpponents.size() > 0)
					opponents.addEntityList(selectedOpponents);
			}
		}

		return opponents;
	}

	public boolean isPickFromOwnPopulation() {
		return pickFromOwnPopulation;
	}

	public void setPickFromOwnPopulation(boolean pickFromOwnPopulation) {
		this.pickFromOwnPopulation = pickFromOwnPopulation;
	}

	public void setOwnPopulationPlayerID(int ownPopulationID) {
		this.ownPopulationID = ownPopulationID;
	}

}

