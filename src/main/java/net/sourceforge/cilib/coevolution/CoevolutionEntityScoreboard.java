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

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Type;

/**
 * This class provides a means of keeping a competition
 * history of one specific entity within a {@linkplain CoevolutionAlgorithm}.
 */
public class CoevolutionEntityScoreboard implements Type {
	private static final long serialVersionUID = -2524835257237678625L;
	private ArrayList<CoevolutionEntityScore> scores;
	
	/**
	 * Create a default score board.
	 */
	public CoevolutionEntityScoreboard() {
		this.scores = new ArrayList<CoevolutionEntityScore>();
	}
	
	/**
	 * Create a copy of the provided instance.
	 * @param copy The instance to copy.
	 */
	public CoevolutionEntityScoreboard(CoevolutionEntityScoreboard copy) {
		this.scores = new ArrayList<CoevolutionEntityScore>();
		for (CoevolutionEntityScore score : copy.scores) {
			this.scores.add(score);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CoevolutionEntityScoreboard getClone(){
		return new CoevolutionEntityScoreboard(this);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (this.scores == null ? 0 : this.scores.hashCode());
		return hash;
	}

	/**
	 * Get a list of all the entities that the entity this
	 * score board are used for has compete against with the
	 * assosiated score hisotries.
	 * @return A list of score histories.
	 */
	public ArrayList<CoevolutionEntityScore> getScores() {
		return this.scores;
	}

	/**
	 * Get the number of times the entity this score board is
	 * used for has won.
	 * 
	 * @return The number of wins in the score board.
	 */
	public int getWinCount() {
		int wins = 0;
		for (CoevolutionEntityScore score : this.scores) {
			wins += score.getWinCount();
		}	//endFor
		return wins;
	}
	
	/**
	 * Get the number of times the entity this score board is
	 * used for has won against a specific entity.
	 * 
	 * @param entity The entity that has been competed against.
	 * 
	 * @return The number of times the supplied entity has been won against.
	 */
	public int getWinCountAgainst(Entity entity) {
		for (CoevolutionEntityScore score : this.scores) {
			if (score.getCompetingEntity() == entity) {
				return score.getWinCount();
			}
		}

		return 0;
	}
	
	/**
	 * Get the number of times the entity this score board is
	 * used for has lost.
	 * 
	 * @return The number of loses in the score board.
	 */
	public int getLoseCount() {
		int lose = 0;
		for (CoevolutionEntityScore score : this.scores) {
			lose += score.getLoseCount();
		}
		return lose;
	}
	
	/**
	 * Get the number of times the entity this score board is
	 * used for has lost against a specific entity.
	 * 
	 * @param entity The entity that has been competed against.
	 * 
	 * @return The number of times the supplied entity has been lost against.
	 */
	public int getLoseCountAgainst(Entity entity) {
		for (CoevolutionEntityScore score : this.scores) {
			if (score.getCompetingEntity() == entity) {
				return score.getLoseCount();
			}
		}
		
		return 0;
	}
	
	/**
	 * Get the number of times the entity this score board is
	 * used for has drawed.
	 * 
	 * @return The number of draws in the score board.
	 */
	public int getDrawCount() {
		int draw = 0;
		for (CoevolutionEntityScore score : this.scores) {
			draw += score.getDrawCount();
		}

		return draw;
	}
	
	/**
	 * Get the number of times the entity this score board is
	 * used for has drawed against a specific entity.
	 * 
	 * @param entity The entity that has been competed against.
	 * 
	 * @return The number of times the supplied entity has been drawed against.
	 */
	public int getDrawCountAgainst(Entity entity) {
		for (CoevolutionEntityScore score : this.scores) {
			if (score.getCompetingEntity() == entity) {
				return score.getDrawCount();
			}
		}
		
		return 0;
	}
	
	/**
	 * Get the number of times the entity assosiated with this
	 * score board has competed.
	 * 
	 * @return The number of sum of the wins, loses and draws.
	 */
	public int getCompeteCount() {
		int count = 0;
		for (CoevolutionEntityScore score : this.scores) {
			count += score.getWinCount() + score.getLoseCount() + score.getDrawCount();
		}
		
		return count;
	}
	
	/**
	 * Get the number of times the entity assosiated with this
	 * score board has competed against a specific entity.
	 * 
	 * @param entity The entity of which the number of competes are desired.
	 * 
	 * @return The number of competes against the specific entity.
	 */
	public int getCompeteCountAgainst(Entity entity) {
		for (CoevolutionEntityScore score : this.scores) {
			if (score.getCompetingEntity() == entity) {
				return score.getWinCount() + score.getLoseCount() + score.getDrawCount();
			}
		}
		
		return 0;
	}
	
	/**
	 * Indicate that the entity assosicated with this score
	 * board has won against the supplied entity.
	 * 
	 * @param entity The entity that was won.
	 * 
	 * @param round The round in which the competition took place.
	 */
	public void win(Entity entity, int round) {
		for (CoevolutionEntityScore score : this.scores) {
			if (score.getCompetingEntity() == entity) {
				score.win(round);
				return;
			}
		}
		
		//if not already in list
		CoevolutionEntityScore score = new CoevolutionEntityScore(entity);
		score.win(round);
		this.scores.add(score);
	}
	
	/**
	 * Indicate that the entity assosicated with this score
	 * board has lost against the supplied entity.
	 * 
	 * @param entity The entity that won.
	 * 
	 * @param round The round in which the competition took place.
	 */
	public void lose(Entity entity, int round) {
		for (CoevolutionEntityScore score : this.scores) {
			if (score.getCompetingEntity() == entity) {
				score.lose(round);
				return;
			}
		}
		
		//if not already in list
		CoevolutionEntityScore score = new CoevolutionEntityScore(entity);
		score.lose(round);
		this.scores.add(score);
	}
	
	/**
	 * Indicate that the entity assosicated with this score
	 * board has drawed against the supplied entity.
	 * 
	 * @param entity The entity that was drawed against.
	 * 
	 * @param round The round in which the competition took place.
	 */
	public void draw(Entity entity, int round) {
		for (CoevolutionEntityScore score : this.scores) {
			if (score.getCompetingEntity() == entity) {
				score.draw(round);
				return;
			}
		}
		
		//if not already in list
		CoevolutionEntityScore score = new CoevolutionEntityScore(entity);
		score.draw(round);
		this.scores.add(score);
	}

	public int getDimension() {
		throw new UnsupportedOperationException("Implementation not provided");
	}

	public String getRepresentation() {
		throw new UnsupportedOperationException("Implementation not provided");
	}

	public boolean isInsideBounds() {
		throw new UnsupportedOperationException("Implementation not provided");
	}

	public void randomise() {
		throw new UnsupportedOperationException("Implementation not provided");
	}

	public void reset() {
		this.scores = new ArrayList<CoevolutionEntityScore>();
	}
}
