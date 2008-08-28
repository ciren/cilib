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
package net.sourceforge.cilib.coevolution.score;

import java.util.ArrayList;

import net.sourceforge.cilib.coevolution.CoevolutionAlgorithm;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;

/**
 * This class provides a means of keeping a competition
 * history of one specific entity within a {@linkplain CoevolutionAlgorithm}.
 */
public class EntityScoreboard implements Type {
	private static final long serialVersionUID = -2524835257237678625L;
	private ArrayList<EntityScore> scores;
	
	/**
	 * Create a default score board.
	 */
	public EntityScoreboard() {
		this.scores = new ArrayList<EntityScore>();
	}
	
	/**
	 * Create a copy of the provided instance.
	 * @param copy The instance to copy.
	 */
	public EntityScoreboard(EntityScoreboard copy) {
		this.scores = new ArrayList<EntityScore>();
		for (EntityScore score : copy.scores) {
			this.scores.add(score);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EntityScoreboard getClone(){
		return new EntityScoreboard(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
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
	public ArrayList<EntityScore> getScores() {
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
		for (EntityScore score : this.scores) {
			wins += score.getWinCount();
		}	//endFor
		return wins;
	}
	
	public int getWinCount(int round) {
		int wins = 0;
		for (EntityScore score : this.scores) {
			if(score.getRound() == round){
				wins += score.getWinCount();
			}
		}	//endFor
		return wins;
	}
	
	/**
	 * Get the number of times the entity this score board is
	 * used for has lost.
	 * 
	 * @return The number of loses in the score board.
	 */
	public int getLoseCount() {
		int lose = 0;
		for (EntityScore score : this.scores) {
			lose += score.getLoseCount();
		}
		return lose;
	}
	
	/**
	 * Get the number of times the entity this score board is
	 * used for has drawed.
	 * 
	 * @return The number of draws in the score board.
	 */
	public int getDrawCount() {
		int draw = 0;
		for (EntityScore score : this.scores) {
			draw += score.getDrawCount();
		}

		return draw;
	}
	
	/**
	 * Get the number of times the entity assosiated with this
	 * score board has competed.
	 * 
	 * @return The number of sum of the wins, loses and draws.
	 */
	public int getCompeteCount() {
		int count = 0;
		for (EntityScore score : this.scores) {
			count += score.getWinCount() + score.getLoseCount() + score.getDrawCount();
		}
		
		return count;
	}
	
	public int getCompeteCount(int round){
		int count = 0;
		for (EntityScore entityScore : this.scores) {
			if(entityScore.getRound() == round){
				count += entityScore.getPlayCount();
			}
		}
		return count;
	}
	
	public void mergeEntityScore(EntityScore scoreBoard){
		for (EntityScore entityScore : this.scores) {
			if (scoreBoard.getCompetitorGroup() == entityScore.getCompetitorGroup() &&
					scoreBoard.getRound() == entityScore.getRound()) {
						entityScore.mergeScoreBoard(scoreBoard);
						return;
			}
		}		
		this.scores.add(scoreBoard);
	}
	
	public ArrayList<Fitness> getWinScores(int round){
		ArrayList<Fitness> values = new ArrayList<Fitness>();
		for (EntityScore entityScore : this.scores) {
			if(entityScore.getRound() == round){
				values.addAll(entityScore.getRoundsWon());
			}
		}
		return values;
	}
	
	public ArrayList<Fitness> getDrawScores(int round){
		ArrayList<Fitness> values = new ArrayList<Fitness>();
		for (EntityScore entityScore : this.scores) {
			if(entityScore.getRound() == round){
				values.addAll(entityScore.getRoundsDrawn());
			}
		}
		return values;
	}
	
	public ArrayList<Fitness> getLoseScores(int round){
		ArrayList<Fitness> values = new ArrayList<Fitness>();
		for (EntityScore entityScore : this.scores) {
			if(entityScore.getRound() == round){
				values.addAll(entityScore.getRoundsLost());
			}
		}
		return values;
	}
	
	public ArrayList<Fitness> getScores(int round){
		ArrayList<Fitness> values = new ArrayList<Fitness>();
		for (EntityScore entityScore : this.scores) {
			if(entityScore.getRound() == round){
				values.addAll(entityScore.getRoundsWon());
				values.addAll(entityScore.getRoundsDrawn());
				values.addAll(entityScore.getRoundsLost());
			}
		}
		return values;
	}

	public void clearScoreBoard(){
		scores.clear();
	}
	
	public void removeScores(int round){
		for(int i = scores.size() - 1; i >= 0; --i){
			if(scores.get(i).getRound() == round){
				scores.remove(i);
			}
		}
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
		this.scores = new ArrayList<EntityScore>();
	}
}
