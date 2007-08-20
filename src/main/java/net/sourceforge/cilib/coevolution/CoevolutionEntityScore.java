/*
 * CoevolutionEntityScore.java
 * 
 * Created on May 16, 2007
 *
 * Copyright (C) 2003 - 2006 
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

import net.sourceforge.cilib.entity.Entity;

/**
 * This class is used to store history of compotitions
 * the entity stored in this class against one other
 * specific entity.
 * 
 * @author cornelius gouws
 * @see CoevolutionEntityScoreboard
 */
public class CoevolutionEntityScore {
	/**
	 * The entity that the score is kept for
	 */
	private Entity entity;//I
	/**
	 * The rounds that the entity the score is kept for has been won
	 */
	private ArrayList<Integer> roundsWon;
	/**
	 * The rounds that the entity the score is kept for has been lost against
	 */
	private ArrayList<Integer> roundsLost;
	/**
	 * The rounds that the entity the score is kept for has been drawed against
	 */
	private ArrayList<Integer> roundsDrawed;
	
	/**
	 * The default constructor should not be used.
	 */
	private CoevolutionEntityScore() {
		//private to avoid use
		//TODO throw exception
	}
	
	/**
	 * Create a CoevolutionEntityScore for the given entity.
	 * @param entity The entity that should be kept score of.
	 */
	public CoevolutionEntityScore(Entity entity) {
		this.entity = entity;
		
		this.roundsWon = new ArrayList<Integer>();
		this.roundsLost = new ArrayList<Integer>();
		this.roundsDrawed = new ArrayList<Integer>();
	}

	/**
	 * Get the entity that is kept score of.
	 * @return The entity that is kept score of.
	 */
	public Entity getCompetingEntity() {
		return entity;
	}
	
	/**
	 * Indicate that the entity that is contained in this
	 * CoevolutionEntityScore has been won. 
	 * @param round The round that the entity has been won.
	 */
	public void win(int round) {
		this.roundsWon.add(round);
	}
	
	/**
	 * Indicate that the entity that is contained in this
	 * CoevolutionEntityScore won. 
	 * @param round The round that the entity won.
	 */
	public void lose(int round) {
		this.roundsLost.add(round);
	}
	
	/**
	 * Indicate that the entity that is contained in this
	 * CoevolutionEntityScore has drawed. 
	 * @param round The round that the entity drawed.
	 */
	public void draw(int round) {
		this.roundsDrawed.add(round);
	}
	
	/**
	 * Get the number of times this entity has been won.
	 * @return The number of times this entiy has been won.
	 */
	public int getWinCount() {
		return this.roundsWon.size();
	}
	
	/**
	 * Get the number of times this entity won.
	 * @return The number of times this entity won.
	 */
	public int getLoseCount() {
		return this.roundsLost.size();
	}
	
	/**
	 * Get the number of times this entity drawed.
	 * @return The number of times this entity has drawed.
	 */
	public int getDrawCount() {
		return this.roundsDrawed.size();
	}

	/**
	 * Get a list of all the rounds the entity has drawed.
	 * @return The rounds the entity drawed. 
	 */
	public ArrayList<Integer> getRoundsDrawed() {
		return roundsDrawed;
	}

	/**
	 * Get a list of all the rounds the entity has won.
	 * @return The rounds the entity won.
	 */
	public ArrayList<Integer> getRoundsLost() {
		return roundsLost;
	}

	/**
	 * Get a list of all the rounds the entity has been won.
	 * @return The rounds the entity has been won.
	 */
	public ArrayList<Integer> getRoundsWon() {
		return roundsWon;
	}
}
