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

    /**
     * Create a new instance of this function. The default board size is 8.
     */
    public Queens() {
        this.boardSize = 8;
        setDomain("B^" + boardSize*boardSize);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public Queens(Queens copy) {
        this.boardSize = copy.boardSize;
        setDomain("B^" + boardSize*boardSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Queens getClone() {
        return new Queens(this);
    }

    /**
     * Get the matrix and determine if there are any conflicts. If there are no
     * conflicts, then a suitable solution has been found.
     *
     */
    @Override
    public Integer evaluate(Vector input) {
        int fitness = 0;
        boolean[][] board = new boolean[boardSize][boardSize];

        initialiseBoard(board, input);

        if (numberOfQueens(board) != boardSize)
            return 30000; // Should this not be a big number from somewhere else? Integer.MAX_VALUE?

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                boolean isQueen = board[row][col];

                if (isQueen) {
                    for (int move = 0; move < xMoves.length; move++)
                        fitness += determineConflicts(move, row, col, board);
                }
            }
        }

        return fitness;
    }


    /**
     * Determine the number of conflicts, based on the current direction that
     * the queen is headed in.
     * @param move The integer determining the move of the queen.
     * @param row The current {@code row}.
     * @param col The current {@code column}.
     * @param board The current game state.
     * @return The value of the number of conflicts.
     */
    private double determineConflicts(int move, int row, int col, boolean[][] board) {
        double conflicts = 0;
        int newRow = row;
        int newCol = col;

        newRow += xMoves[move];
        newCol += yMoves[move];

        while (insideBoard(newRow, newCol)) {
            if (board[newRow][newCol])
                conflicts++;

            newRow += xMoves[move];
            newCol += yMoves[move];
        }

        return conflicts;
    }

    /**
     * Initialize the current game state with the provided {@code Vector}.
     * @param board The board to initialize.
     * @param x The {@code Vector} to base the initialization on.
     */
    private void initialiseBoard(boolean[][] board, Vector x) {
        int counter = 0;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = x.getBit(counter++);
            }
        }
    }

    /**
     * Get the size of the currently set board.
     * @return Returns the boardSize.
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Set the size of the board.
     * @param boardSize The boardSize to set.
     */
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        this.setDomain("B^" + boardSize*boardSize);
    }

    /**
     * Determine if the current {@code row} and {@code col} values are within
     * the biounds of the chess board.
     * @param row The row value in the range: {@code [0..boardSize-1]}
     * @param col The column value in the range:  {@code [0..boardSize-1]}
     * @return {@code true} if the position is inside the board, {@code false} otherwise.
     */
    private boolean insideBoard(int row, int col) {
        return (row >= 0 && row < boardSize) && (col >= 0 && col < boardSize);
    }

    /**
     * Obtain the number of queen's that are currently on the chess board.
     * @param board The board to inspect.
     * @return The number of queens.
     */
    private int numberOfQueens(boolean[][] board) {
        int count = 0;
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j])
                    count++;
            }

        return count;
    }

}
