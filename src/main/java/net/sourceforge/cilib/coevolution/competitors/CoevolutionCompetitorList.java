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
package net.sourceforge.cilib.coevolution.competitors;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.type.types.Type;

/**
 * This class maintains a list of competitors per population for a coevolution problem.
 * It could also be used to represent a HOF.
 * @author leo
 *
 */
public class CoevolutionCompetitorList implements Type{
	private static final long serialVersionUID = -2894509571214982344L;
	List<PopulationCompetitorList> competitorLists;
	int numberOfEntitiesPerList; //set to indicate that all sublists should have this size.
	/**
	 *
	 */
	public CoevolutionCompetitorList() {
		competitorLists = new ArrayList<PopulationCompetitorList>();
		numberOfEntitiesPerList = -1;
	}

	public CoevolutionCompetitorList(int numberOfEntitiesPerList) {
		competitorLists = new ArrayList<PopulationCompetitorList>();
		this.numberOfEntitiesPerList = numberOfEntitiesPerList;
	}

	public CoevolutionCompetitorList(CoevolutionCompetitorList other) {
		competitorLists = new ArrayList<PopulationCompetitorList>();
		numberOfEntitiesPerList = other.numberOfEntitiesPerList;
		for(PopulationCompetitorList popList: other.competitorLists){
			competitorLists.add(popList.getClone());
		}
	}
	/**
	 * Add a {@linkplain PopulationCompetitorList} to this {@linkplain CoevolutionCompetitorList}
	 * @param list The competitors
	 */
	public void addCompetitorList(PopulationCompetitorList list){

		for(PopulationCompetitorList popList: competitorLists){
			if(popList.getPopulationID() == list.getPopulationID()){
				if(numberOfEntitiesPerList != -1 && (popList.size() + list.size()) > numberOfEntitiesPerList)
					throw new RuntimeException("Entity list is not the right size, each subpopulation should have the same length");
				popList.merge(list);
				return;
			}
		}
		if(numberOfEntitiesPerList != -1 && list.size() != numberOfEntitiesPerList)
			throw new RuntimeException("Entity list is not the right size, each subpopulation should have the same length");
		competitorLists.add(list);
	}
	/**
	 * Add a list of {@linkplain Competitor}'s to specific {@linkplain PopulationCompetitorList}
	 * @param populationID The ID of the population these competitors are from
	 * @param list The list of competitors
	 */

	public void addCompetitorList(int populationID, List<Competitor> list){

		for(PopulationCompetitorList popList: competitorLists){
			if(popList.getPopulationID() == populationID){
				if(numberOfEntitiesPerList != -1 && (popList.size() + list.size()) > numberOfEntitiesPerList)
					throw new RuntimeException("Entity list is not the right size, each subpopulation should have the same length");

				popList.addCompetitors(list);
				return;
			}
		}
		if(numberOfEntitiesPerList != -1 && list.size() != numberOfEntitiesPerList)
			throw new RuntimeException("Entity list is not the right size, each subpopulation should have the same length");

		competitorLists.add(new PopulationCompetitorList(populationID, list));
	}
	/**
	 * Merge another {@linkplain CoevolutionCompetitorList} into this one
	 * @param other The list to merge
	 */
	public void merge(CoevolutionCompetitorList other){
		for(PopulationCompetitorList list: other.competitorLists){
			addCompetitorList(list);
		}
	}
	/**
	 * Add a competitor to the list of population competitors. First see if it will fit in a
	 * {@linkplain PopulationCompetitorList} based on its populationID, if not create a new
	 * {@linkplain PopulationCompetitorList}
	 * @param competitor The {@linkplain Competitor} to add
	 */
	public void addCompetitor(Competitor competitor){
		for(PopulationCompetitorList popList: competitorLists){
			if(popList.getPopulationID() == competitor.getPopulationID()){
				if(numberOfEntitiesPerList != -1 && popList.size() == numberOfEntitiesPerList)
					popList.replaceWorst(competitor);
				else
					popList.addCompetitor(competitor);
				return;
			}
		}
		PopulationCompetitorList list = new PopulationCompetitorList(competitor.getPopulationID());
		list.addCompetitor(competitor);
		competitorLists.add(list);
	}

	/**
	 * get one entry from each list at index
	 * @param index the specified index which relates to a population ID
	 * @return the requested list
	 */
	public List<Competitor> getCompetitorsFromSubList(int index){
		List<Competitor> list = new ArrayList<Competitor>();
		for(PopulationCompetitorList popList: competitorLists){
			if(index >= popList.size())
				throw new RuntimeException("index larger than list");
			list.add(popList.getCompetitor(index));
		}
		return list;
	}
	/**
	 * Remove the {@linkplain PopulationCompetitorList} with the specified index
	 * @param index
	 */
	public void removeSubList(int index){
		competitorLists.remove(index);
	}

	public int getNumberOfEntitesPerList(){
		return numberOfEntitiesPerList;
	}

	public void setNumberofEntitiesPerList(){
		if(competitorLists.size() > 0)
			numberOfEntitiesPerList = competitorLists.get(0).size();
	}

	public int getNumberOfLists(){
		return competitorLists.size();
	}

	public int getPopulationID(int index){
		return competitorLists.get(index).getPopulationID();
	}
	public int getNumberOfCompetitors(int index){
		return competitorLists.get(index).size();
	}
	public Competitor getCompetitor(int listIndex, int populationIndex){
		return competitorLists.get(listIndex).getCompetitor(populationIndex);
	}
	public void removeCompetitor(int listIndex, int populationIndex){
		competitorLists.get(listIndex).removeCompetitor(populationIndex);
	}
	/**
	 * Change the populationID for a {@linkplain PopulationCompetitorList}, if the new population already exists, update the
	 * population ID's of the {@linkplain Competitor}'s and merge them with the existing {@linkplain PopulationCompetitorList},
	 * @param index the index in the array to update
	 * @param oldPopulationID the old populationID
	 * @param newPopulationID the new populationID
	 */
	public void updatePopulationID(int index, int oldPopulationID, int newPopulationID){
		boolean found = false;
		for(PopulationCompetitorList popList: competitorLists){
			if(popList.getPopulationID() == newPopulationID){
				competitorLists.get(index).changePopulationID(oldPopulationID, newPopulationID);
				popList.merge(competitorLists.get(index));
				found = true;
				break;
			}
		}
		if(found){
			competitorLists.remove(index);
		}
		else{
			competitorLists.get(index).changePopulationID(oldPopulationID, newPopulationID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CoevolutionCompetitorList getClone() {
		return new CoevolutionCompetitorList(this); //does this make sense?
	}
}
