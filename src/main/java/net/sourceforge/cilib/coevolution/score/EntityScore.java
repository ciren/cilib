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
package net.sourceforge.cilib.coevolution.score;

import java.util.ArrayList;
import net.sourceforge.cilib.problem.Fitness;


/**
 * This class is used to store history of competitions
 * the entity stored in this class against one other
 * specific entity.
 *
 * @author cornelius gouws
 * @author leo
 * @see EntityScoreboard
 */
public class EntityScore {
	/**
	 * The current round and competitor group. A competitor group is a unique nubmer assigned to a group of opponents this entitry
	 * competed against
	 */
	private int round;
	private int competitorGroup;

	/**
	 * Fitness values for rounds that this entity won
	 */
	private ArrayList<Fitness> roundsWon;
	/**
	 * Fitness values for rounds this entity lost
	 */
	private ArrayList<Fitness> roundsLost;
	/**
	 * Fitness values for rounds this entity drew
	 */
	private ArrayList<Fitness> roundsDrawn;

	/**
	 * Create a CoevolutionEntityScore for the given entity.
	 * @param entity The entity that should be kept score of.
	 */
	public EntityScore(int round, int competitorGroup) {

		this.round = round;
		this.competitorGroup = competitorGroup;

		this.roundsWon = new ArrayList<Fitness>();
		this.roundsLost = new ArrayList<Fitness>();
		this.roundsDrawn = new ArrayList<Fitness>();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if ((obj == null) || (this.getClass() != obj.getClass()))
			return false;

		EntityScore other = (EntityScore) obj;
		return (this.roundsDrawn.equals(other.roundsDrawn)) &&
			(this.roundsLost.equals(other.roundsLost)) &&
			(this.roundsWon.equals(other.roundsWon)) &&
			(this.round == other.round) &&
			(this.competitorGroup == other.competitorGroup);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + round;//(this.competitors == null ? 0 : this.competitors.hashCode());
		hash = 31 * hash + competitorGroup;
		hash = 31 * hash + (this.roundsDrawn == null ? 0 : this.roundsDrawn.hashCode());
		hash = 31 * hash + (this.roundsLost == null ? 0 : this.roundsLost.hashCode());
		hash = 31 * hash + (this.roundsWon == null ? 0 : this.roundsWon.hashCode());
		return hash;
	}

	/**
	 * Indicate that the entity that is contained in this
	 * CoevolutionEntityScore won.
	 * @param score The Fitness achieved
	 */
	public void win(Fitness score) {
		this.roundsWon.add(score);
	}

	/**
	 * Indicate that the entity that is contained in this
	 * EntityScore has been defeated.
	 * @param score the Fitness achieved
	 */
	public void lose(Fitness score) {
		this.roundsLost.add(score);
	}

	/**
	 * Indicate that the entity that is contained in this
	 * EntityScore has drawn.
	 * @param round The round that the entity drawed.
	 */
	public void draw(Fitness score) {
		this.roundsDrawn.add(score);
	}

	/**
	 * Get the number of times this entity won
	 * @return The number of times this entiy won.
	 */
	public int getWinCount() {
		return this.roundsWon.size();
	}

	/**
	 * Get the number of times this entity lost
	 * @return The number of times this entity lost
	 */
	public int getLoseCount() {
		return this.roundsLost.size();
	}

	/**
	 * Get the number of times this entity drew.
	 * @return The number of times this entity has drawn.
	 */
	public int getDrawCount() {
		return this.roundsDrawn.size();
	}
	/**
	 * Get the number of games played
	 * @return The trtal number of games played
	 */
	public int getPlayCount(){
		return getWinCount() + getLoseCount() + getDrawCount();
	}
	/**
	 * Merge this scoreboard with another one
	 * @param other the other scoreboard
	 */
	public void mergeScoreBoard(EntityScore other){
		if(!(round == other.round && competitorGroup == other.competitorGroup))
			throw new RuntimeException("Unable to merge score boards, competitors do not match");
		roundsWon.addAll(other.roundsWon);
		roundsDrawn.addAll(other.roundsDrawn);
		roundsLost.addAll(other.roundsLost);
	}

	/**
	 * Get a list of all the rounds the entity has drawn.
	 * @return The rounds the entity drawn.
	 */
	public ArrayList<Fitness> getRoundsDrawn() {
		return roundsDrawn;
	}

	/**
	 * Get a list of all the rounds the entity has lost.
	 * @return The rounds the entity lost.
	 */
	public ArrayList<Fitness> getRoundsLost() {
		return roundsLost;
	}

	/**
	 * Get a list of all the rounds the entity has won.
	 * @return The rounds the entity has won.
	 */
	public ArrayList<Fitness> getRoundsWon() {
		return roundsWon;
	}

	public int getRound() {
		return round;
	}

	public int getCompetitorGroup() {
		return competitorGroup;
	}
}
