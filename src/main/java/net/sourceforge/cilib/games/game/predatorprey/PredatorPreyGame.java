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
package net.sourceforge.cilib.games.game.predatorprey;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.games.game.GridGame;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.games.items.PlayerItem;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * @author leo
 *
 */
public class PredatorPreyGame extends GridGame {
	/**
	 *
	 */
	private static final long serialVersionUID = 332203013419474482L;
	int maxIterations;


	public PredatorPreyGame(){
		maxIterations = 20;
		turnBasedGame = false;
		gridHeight = 9;
		gridWidth = 9;
	}

	public PredatorPreyGame(PredatorPreyGame other){
		super(other);
		maxIterations = other.maxIterations;
	}

	private boolean predatorCaughtPrey()
	{
		try
		{
			//if predator and prey players are next to or on the same cell then game over
			if(currentState.getItem(0).getLocation().getDistance(new EuclideanDistanceMeasure(), currentState.getItem(1).getLocation()) <= 1.0)
				return true;

			return false;
		}
		catch(Exception e)
		{
			throw new InitialisationException("Game not initialized, predator and prey items do not exist");
		}
	}

	public void movePlayer(int playerID, int x, int y){
		try{
			Vector moveVector = new Vector(2);
			moveVector.add(new Int(x));
			moveVector.add(new Int(y));
			for(int i = 0; i < currentState.getSize(); ++i){
				if(((PlayerItem)currentState.getItem(i)).getPlayerID() == playerID){
					//move the item by the specified coords
					currentState.getItem(i).getLocation().moveItem(moveVector);
				}
			}
		}
		catch(Exception e){
			throw new InitialisationException("Game not initialized, predator and prey items not found");
		}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.game.Game#gameOver()
	 */
	@Override
	public boolean gameOver() {
		if(currentIteration >= maxIterations)
			return true;

		return predatorCaughtPrey();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.game.Game#getClone()
	 */
	@Override
	public PredatorPreyGame getClone() {
		return new PredatorPreyGame(this);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.game.Game#getGameResult()
	 */
	@Override
	public AbstractGameResult getGameResult() {
		// TODO Auto-generated method stub
		if((currentIteration >= maxIterations) && !predatorCaughtPrey())
			return new WinGameResult(2); //prey won
		else
			return new WinGameResult(1); //predator won
	}

	private void setRandomPositions(){
		try{
			Random rand = new MersenneTwister();
			((GridLocation)(currentState.getItem(0).getLocation())).getPosition().setInt(0, rand.nextInt(getWidth()));
			((GridLocation)(currentState.getItem(0).getLocation())).getPosition().setInt(1, rand.nextInt(getHeight()));
			((GridLocation)(currentState.getItem(1).getLocation())).getPosition().setInt(0, rand.nextInt(getWidth()));
			((GridLocation)(currentState.getItem(1).getLocation())).getPosition().setInt(1, rand.nextInt(getHeight()));
		}
		catch(Exception e){
			throw new InitialisationException("Game not initialized, predator and prey items do not exist");
		}
	}

	@Override
	public void initializeGame() {
		currentState.clearState();
		currentState.addGameItem(new PredatorItem(1, gridWidth, gridHeight));
		currentState.addGameItem(new PreyItem(2, gridWidth, gridHeight));
		setRandomPositions();
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		System.out.println("");
		for(int y = 0; y < gridHeight; ++y){
			String line = "|";
			for(int x = 0; x < gridWidth; ++x){
				if(((GridLocation)(currentState.getItem(0).getLocation())).getPosition().getInt(0) == x
						&& ((GridLocation)(currentState.getItem(0).getLocation())).getPosition().getInt(1) == y)
					line += "P|";
				else if(((GridLocation)(currentState.getItem(1).getLocation())).getPosition().getInt(0) == x
						&& ((GridLocation)(currentState.getItem(1).getLocation())).getPosition().getInt(1) == y){
					line += "Y|";
				}
				else
					line += " |";
			}
			System.out.println(line);
		}
	}
}
