/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
public class KnightsCoverage implements DiscreteFunction {

    private static final long serialVersionUID = -8039165934381145252L;
    private final int[] movesX = {1, 2, 2, 1, -1, -2, -2, -1};
    private final int[] movesY = {-2, -1, 1, 2, 2, 1, -1, -2};
    private int boardSize;

    public KnightsCoverage() {
        this.boardSize = 8;
//        setDomain("B^" + boardSize * boardSize);
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
    }
//    @Override
//    public String getDomain() {
//        return "B^" + boardSize * boardSize;
//    }
}
