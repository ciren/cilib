/*
 * Queens.java
 * 
 * Created on Jan 10, 2006
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.type.types.Vector;

/**
 * Implementation of the a function to return the fitness of the Queens problem.
 * The function class only determines the fitness of the solution by determining
 * the number of conflicts and penalises the fitness if there are.
 * 
 * @author Gary Pampara
 */
public class Queens extends DiscreteFunction {
	
	private int boardSize;
	private final double x_moves [] = { 1, 1,  1,  0, -1, -1, -1, 0 };
	//private final double y_moves [] = { 1, 0, -1, -1, -1,  0,  1, 1 };
	
	
	public Queens() {
		this.boardSize = 8;
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
		//double board [][] = new double[boardSize][boardSize];
		
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				boolean isQueen = x.getBit(row*boardSize+col);
				
				if (!isQueen)
					continue;
				else {
					for (int move = 0; move < x_moves.length; move++)
						fitness += determineConflicts(move, row, col, x);
				}
			}
		}
		
		return fitness;
	}
	
	
	private double determineConflicts(int move, int row, int col, Vector x) {
		if ( (row < 0 || row >= boardSize) || (col < 0 || col >= boardSize) ) {
			return 0;
		}
		else {
			return -8;
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
