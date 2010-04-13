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
 *
 */
public class KnightsTour extends DiscreteFunction {
    private static final long serialVersionUID = -4448832121042323303L;

    private static final int[] MOVE_X = {-2, -1,  1,  2, 2, 1, -1, -2};
    private static final int[] MOVE_Y = {-1, -2, -2, -1, 1, 2, -2, -1};

    private int boardSize;
    private int startRow = 0;
    private int startCol = 0;

    public KnightsTour() {
        this.boardSize = 8;
        setDomain("B^" + 3*boardSize*boardSize);
    }

    @Override
    public KnightsTour getClone() {
        return new KnightsTour();
    }

    /**
     * Determine the fitness of the provided bit string.
     * @param input
     * @return
     */
    @Override
    public Integer evaluate(Vector input) {
        // First, decode the input vector into moves for the knight to make
        int count = 0;
        int [] moves = new int[boardSize*boardSize];
        for (int i = 0; i < input.size(); i += 3) {
            moves[count++] = decode(input.booleanValueOf(i), input.booleanValueOf(i+1), input.booleanValueOf(i+2));
        }

        // Initialize the game board
        boolean[][] visited = new boolean[boardSize][boardSize];

        int fitness = 0;
        int currentX = startCol;
        int currentY = startRow;

        for (int i = 0; i < moves.length; i++) {
            currentX += MOVE_X[moves[i]];
            currentY += MOVE_Y[moves[i]];

            if ((currentX >= 0 && currentX < boardSize) &&
                (currentY >= 0 && currentY < boardSize) &&
                !visited[currentX][currentY]) {
                fitness++;
                visited[currentX][currentY] = true;
            }
            else {
                break;
            }
        }

        return Integer.valueOf(fitness);
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        setDomain("B^" + 3*boardSize*boardSize);
    }

    // Returns the octal value, values 0 - 7
    private int decode(boolean bit, boolean bit0, boolean bit1) {
        int i0 = bit ? 1 : 0;
        int i1 = bit0 ? 1 : 0;
        int i2 = bit1 ? 1 : 0;
        return 4*i0 + 2*i1 + i2;
    }

}
