/*
 * SelectNOpponentSelectionStrategy.java
 * 
 * Created on 2007/04/27
 *
 * Copyright (C) 2003, 2007 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.coevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.Int;


/**
 * Select N opponents out of the other populations.
 * If pickFromOwnPopulation is set to true, opponents can also be selected from the
 * population where the entity belongs 
 * @author Julien Duhain
 *
 */
public class SelectNOpponentSelectionStrategy extends OpponentSelectionStrategy {

	protected int numberOfOpponents = 5;
	protected RandomNumber random = new RandomNumber();
	protected boolean pickFromOwnPopulation = false;

	public SelectNOpponentSelectionStrategy() {
		
	}
	
	public SelectNOpponentSelectionStrategy(SelectNOpponentSelectionStrategy snoss){
		this.numberOfOpponents = snoss.numberOfOpponents;
		this.pickFromOwnPopulation = snoss.pickFromOwnPopulation;
	}
	
	@Override
	public OpponentSelectionStrategy clone() {
		return new SelectNOpponentSelectionStrategy(this);
	}

	@Override
	public List<Entity> setCompetitors(List<PopulationBasedAlgorithm> pool) {
		List<Entity> opponents = new ArrayList<Entity>();
		List<Entity> potentialOpponents = new ArrayList<Entity>();
		
		for(ListIterator it=pool.listIterator(); it.hasNext(); ){
			PopulationBasedAlgorithm currentAlgorithm = (PopulationBasedAlgorithm)it.next();
			for(int i=0; i<currentAlgorithm.getPopulationSize(); i++){
				Entity e = currentAlgorithm.getTopology().get(i);
				if(pickFromOwnPopulation && e.getProperties().get("populationID") != new Int(i))
					potentialOpponents.add(e);
			}
				
		}
		
		int upper = potentialOpponents.size();
		
		for(int i=0; i<numberOfOpponents;i++){
			int selected = (int)random.getUniform(0,upper);
			opponents.add(potentialOpponents.get(selected));
		}
		return opponents;
	}

	public boolean isPickFromOwnPopulation() {
		return pickFromOwnPopulation;
	}

	public void setPickFromOwnPopulation(boolean pickFromOwnPopulation) {
		this.pickFromOwnPopulation = pickFromOwnPopulation;
	}
	
	public int getNumberOfOpponents() {
		return numberOfOpponents;
	}

	public void setNumberOfOpponents(int numberOfOpponents) {
		this.numberOfOpponents = numberOfOpponents;
	}

}
