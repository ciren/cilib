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

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This class represents a list of {@linkplain Competitor}'s with the same
 * populationID
 * @author leo
 *
 */
public class PopulationCompetitorList implements Cloneable {

	private static final long serialVersionUID = 3331877190938887213L;
	List<Competitor> competitors;
	public PopulationCompetitorList(int populationID) {
		competitors = new ArrayList<Competitor>();
	}

	public PopulationCompetitorList(int populationID, List<Competitor> competitors) {
		this.competitors = competitors;
	}

	public PopulationCompetitorList(PopulationCompetitorList other) {
		competitors = new ArrayList<Competitor>();
		for(Competitor c: other.competitors)
			competitors.add(c.getClone());
	}

	/**
	 * Return the {@linkplain Competitor} at the specified index in the list
	 * @param index The index in the list
	 * @return The competitor object
	 */
	public Competitor getCompetitor(int index){
		if(index >= competitors.size())
			throw new RuntimeException("Invalid index");
		return competitors.get(index);
	}
	/**
	 * Remove the {@linkplain Competitor} at the specified index in the list
	 * @param index
	 */
	public void removeCompetitor(int index){
		competitors.remove(index);
	}

	public int size(){
		return competitors.size();
	}
	/**
	 * Add a {@linkplain Competitor} to this list
	 * @param competitor
	 */
	public void addCompetitor(Competitor competitor){
		int populationID = getPopulationID();
		if(populationID != -1 && populationID != competitor.getPopulationID())
			throw new RuntimeException("Invalid merge, populationID's do not match");
		competitors.add(competitor);
	}
	/**
	 * Search through the {@linkplain Competitor} for the lowest fitness and replace it with newCompetitor if
	 * its fitness is better
	 * @param newCompetitor the new competitor to potentially add
	 */
	public void replaceWorst(Competitor newCompetitor){
		//find the worst competitor
		Fitness worstFit = null;
		int worstIndex = 0;
		for(int i = 0; i < competitors.size(); ++i){
			if(worstFit == null || competitors.get(i).getEntityFitness().compareTo(worstFit) < 0){
				worstFit = competitors.get(i).getEntityFitness();
				worstIndex = i;
			}
		}
		if(newCompetitor.getEntityFitness().compareTo(competitors.get(worstIndex).getEntityFitness()) > 0){
			competitors.remove(worstIndex);
			competitors.add(newCompetitor);
		}
	}
	/**
	 * Add a List of {@linkplain Competitor}'s to this list
	 * @param competitors
	 */
	public void addCompetitors(List<Competitor> competitors){
		int populationID = getPopulationID();
		for(Competitor competitor: competitors)
		{
			if(populationID != -1 && populationID != competitor.getPopulationID())
				throw new RuntimeException("Invalid merge, populationID's do not match");
			this.competitors.add(competitor);
		}
	}
	/**
	 * Change the population ID of this list
	 *
	 * @param oldPopulationID The original populationID
	 * @param newPopulationID The new populationID
	 */
	public void changePopulationID(int oldPopulationID, int newPopulationID){
		List<Competitor> tempCompetitors = new ArrayList<Competitor>();
		for(Competitor c: competitors){
			tempCompetitors.add(new Competitor(c.getEntityData(), c.getPopulationID()));
		}
		competitors.clear();
		competitors = tempCompetitors;
	}
	/**
	 * Merge this list with the specified list
	 * @param other
	 */
	public void merge(PopulationCompetitorList other){
		addCompetitors(other.competitors);
	}
	public void clear(){
		competitors.clear();
	}
	/**
	 * {@inheritDoc}
	 */
	public PopulationCompetitorList getClone() {
		return new PopulationCompetitorList(this);
	}
	/**
	 * get the populationID of the {@linkplain Competitor}'s in this object
	 * @return
	 */
	public int getPopulationID() {
		if(competitors.size() > 0)
			return competitors.get(0).getPopulationID();
		else
			return -1;
	}

}
