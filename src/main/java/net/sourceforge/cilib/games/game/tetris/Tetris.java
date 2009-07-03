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
package net.sourceforge.cilib.games.game.tetris;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.RealTimeGame;
import net.sourceforge.cilib.games.game.StateGame;
import net.sourceforge.cilib.games.game.UpdateGame;
import net.sourceforge.cilib.games.game.tetris.shape.AbstractShape;
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.ScoreGameResult;
import net.sourceforge.cilib.games.states.GameState;

/**
 * This is an implimentation of the game of Tetris.
 * @author leo
 */
public class Tetris extends Game<TetrisGameState> implements UpdateGame, StateGame, RealTimeGame {
	private static final long serialVersionUID = -3843619009373056306L;
	int maxLines;
	public Tetris() {
		setCurrentGameState(new TetrisGameState());
		maxLines = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Tetris(Tetris other) {
		super(other);
		maxLines = other.maxLines;
	}

	/**
	 * {@inheritDoc}
	 */
	public Tetris(Tetris other, TetrisGameState newState) {
		super(other, newState);
		maxLines = other.maxLines;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAgent(Agent player){
		if(players.size() > 0)
			throw new RuntimeException("Tetris is a single player game");
		super.setAgent(player);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAgent(Agent player, int playerID){
		super.setAgent(player, playerID);
		if(players.size() > 1)
			throw new RuntimeException("Tetris is a single player game");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void display() {
		System.out.println("");
		for(int j = 0; j < getHeight(); ++j){
			String line = "|";
			for(int i = 0; i < getWidth(); ++i){
				int shape = 0;
				GameItem item = null;
				if(getCurrentState().getCurrentShape() != null)
					item = getCurrentState().getCurrentShape().getBlock(i, j);

				if(item == null){
					item = getCurrentState().getItem(i, j);
					shape = 1;
				}else if(getCurrentState().getItem(i, j) != null){
					shape = 2;
				}

				char player = ' ';
				if(item != null){
					player = shape == 0 ? 'S' : (shape == 1 ? 'X' : 'C');//((GameEnum)getPlayer(((PlayerItem)item).getPlayerID()).getAgentToken()).getDescription().charAt(0);
				}
				line += player + "|";
			}
			System.out.println(line);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gameOver() {
		return getCurrentState().getCurrentShape() == null || (maxLines > 0 && getCurrentState().getTotalRowsCleared() >= maxLines);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tetris getClone() {
		return new Tetris(this);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tetris getClone(TetrisGameState newState) {
		return new Tetris(this, newState);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractGameResult getGameResult() {
		return new ScoreGameResult(getCurrentState().getTotalRowsCleared());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeGame() {
		if(getCurrentState().getGridWidth() == 0 || getCurrentState().getGridHeight() == 0)
			throw new RuntimeException("Grid Widht and/or Height cannot be 0. Please specify grid size");
		getCurrentState().clearState();
	}
	/**
	 * {@inheritDoc}
	 */
	public void Update() {
		getCurrentState().mergeCurrentShape();
		int amRows = getCurrentState().clearFullRows();
		getCurrentState().calculateScore(amRows);
		getCurrentState().createNewShape();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<GameState> generateStates(int currentPlater) {
		List<GameState> newStates = new ArrayList<GameState>();
		AbstractShape currentShape = getCurrentState().getCurrentShape();
		if(currentShape != null){
			TetrisGameState newState = getCurrentState().getClone();
            int amRotate = 0;
            AbstractShape copy = newState.getCurrentShape().getClone();
            while (newState.getCurrentShape().rotate(amRotate)){
            	if(newState.setShapeLeftGrid()){
	                boolean canRight = true;
	                while (canRight){
	                	if(newState.FitShapeInGridTopDown()){
	                		newStates.add(newState.getClone());
	                	}
	                	newState.getCurrentShape().setShapeTopGrid();
	                    canRight = newState.moveRight();
	                }
            	}
            	++amRotate;
            	newState.setCurrentShape(copy.getClone());
            }
            if(newStates.size() == 0)
            	newStates.add(getCurrentState().getClone());
		}
		if(newStates.size() == 0){
			throw new RuntimeException("Game is over, this method should not have been called");
		}
		return newStates;
	}
	public int getWidth(){
		return getCurrentState().getGridWidth();
	}
	public int getHeight(){
		return getCurrentState().getGridHeight();
	}
	public void setWidth(int width){
		getCurrentState().setGridWidth(width);
	}
	public void setHeight(int height){
		getCurrentState().setGridHeight(height);
	}

	public void recordRoundStartState() {
	}

	public void setMaxLines(int maxLines) {
		this.maxLines = maxLines;
	}

	public int getMaxLines() {
		return maxLines;
	}
}
