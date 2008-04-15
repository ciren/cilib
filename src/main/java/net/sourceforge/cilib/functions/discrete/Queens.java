/*
 * Queens.java
 *
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
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the a function to return the fitness of the Queens problem.
 * The function class only determines the fitness of the solution by determining
 * the number of conflicts and penalises the fitness if there are.
 * 
 * @author Gary Pampara
 */
public class Queens extends DiscreteFunction {
	
	private static final long serialVersionUID = 8900436160526532438L;
	private final double [] xMoves = {1, 1,  1,  0, -1, -1, -1, 0};
	private final double [] yMoves = {1, 0, -1, -1, -1,  0,  1, 1};
	private int boardSize;	
	
	public Queens() {
		this.boardSize = 8;
	}
	
	public Queens getClone() {
		return new Queens();
	}

	/**
	 * Get the matrix and determine if there are any conflicts. If there are no
	 * conflicts...
	 * 
	 * For every direction, there should be no conflicts. Move queen until 
	 * you run off board
	 * 
	 * @param x 
	 */
	@Override
	public double evaluate(Vector x) {
		double fitness = 0;
		double [][] board = new double[boardSize][boardSize];
		
		initialiseBoard(board, x);
		
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				boolean isQueen = (board[row][col] == 1.0) ? true : false;
				
				if (!isQueen)
					continue;
				else {
					for (int move = 0; move < xMoves.length; move++)
						fitness += determineConflicts(move, row, col, board);
				}
			}
		}
		
		return fitness;
	}
	
	
	/**
	 * 
	 * @param move
	 * @param row
	 * @param col
	 * @param board
	 * @return
	 */
	private double determineConflicts(int move, int row, int col, double [][] board) {
		double conflicts = 0;
		int newRow = row;
		int newCol = col;
		
		while ((newRow >= 0 && newRow < boardSize) || (newCol >= 0 && newCol < boardSize)) {
			newRow += xMoves[move];
			newCol += yMoves[move];
			
			if (board[newRow][newCol] == 1.0)
				conflicts++;
		}
		
		return conflicts;
	}
	
	/**
	 * 
	 * @param board
	 * @param x
	 */
	private void initialiseBoard(double [][] board, Vector x) {
		int counter = 0;
		
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				board[row][col] = x.getBit(counter++) ? 1.0 : 0.0;
			}
		}
	}

	
	
	/**
	 * @return Returns the boardSize.
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * @param boardSize The boardSize to set.
	 */
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}	

}
