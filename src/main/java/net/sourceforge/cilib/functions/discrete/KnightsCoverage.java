/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the Knight's Coverage problem.
 *
 * <p>
 * The problem....
 *
 */
public class KnightsCoverage extends DiscreteFunction {

    private static final long serialVersionUID = -8039165934381145252L;
    private final int[] movesX = {1, 2, 2, 1, -1, -2, -2, -1};
    private final int[] movesY = {-2, -1, 1, 2, 2, 1, -1, -2};
    private int boardSize;

    public KnightsCoverage() {
        this.boardSize = 8;
        setDomain("B^" + boardSize * boardSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KnightsCoverage getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer apply(Vector input) {
        int[][] board = new int[boardSize][boardSize];

        // Place the knights (represented by a -1)
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (input.booleanValueOf(i * boardSize + j)) {
                    board[i][j] = -1;
                }
            }
        }

        // Now determine the coverage.
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == -1) {
                    determineCoverage(board, i, j);
                }
            }
        }

        // Sum up for the fitness value
        int fitness = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == -1) {// covered square by knight
                    fitness += 100;
                } else if (board[i][j] == 0) { // not covered by a knight at all
                    fitness += 1000;
                } else {
                    fitness -= 200; // square is covered
                }
            }
        }

        return fitness;
    }

    private void determineCoverage(int[][] board, int i, int j) {
        // Move through all the available moves
        for (int m = 0; m < movesX.length; m++) {
            int moveX = movesX[m] + i;
            int moveY = movesY[m] + j;

            if ((moveX >= 0 && moveX < boardSize) && (moveY >= 0 && moveY < boardSize)) {
                if (board[moveX][moveY] != -1) {
                    board[moveX][moveY]++;
                }
            }
        }
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        setDomain("B^" + boardSize * boardSize);
    }
}
