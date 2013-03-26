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
 *
 */
public class KnightsTour implements DiscreteFunction {

    private static final long serialVersionUID = -4448832121042323303L;
    private static final int[] MOVE_X = {-2, -1, 1, 2, 2, 1, -1, -2};
    private static final int[] MOVE_Y = {-1, -2, -2, -1, 1, 2, -2, -1};
    private int boardSize;
    private int startRow = 0;
    private int startCol = 0;

    public KnightsTour() {
        this.boardSize = 8;
//        setDomain("B^" + 3*boardSize*boardSize);
    }

    /**
     * Determine the fitness of the provided bit string.
     *
     * @param input
     * @return the fitness of the bit string
     */
    @Override
    public Integer apply(Vector input) {
        // First, decode the input vector into moves for the knight to make
        int count = 0;
        int[] moves = new int[boardSize * boardSize];
        for (int i = 0; i < input.size(); i += 3) {
            moves[count++] = decode(input.booleanValueOf(i), input.booleanValueOf(i+1), input.booleanValueOf(i+2));
        }

        // Initialise the game board
        boolean[][] visited = new boolean[boardSize][boardSize];

        int fitness = 0;
        int currentX = startCol;
        int currentY = startRow;

        for (int i = 0; i < moves.length; i++) {
            currentX += MOVE_X[moves[i]];
            currentY += MOVE_Y[moves[i]];

            if ((currentX >= 0 && currentX < boardSize)
                    && (currentY >= 0 && currentY < boardSize)
                    && !visited[currentX][currentY]) {
                fitness++;
                visited[currentX][currentY] = true;
            } else {
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
//        setDomain("B^" + 3*boardSize*boardSize);
    }

    // Returns the octal value, values 0 - 7
    private int decode(boolean bit, boolean bit0, boolean bit1) {
        int i0 = bit ? 1 : 0;
        int i1 = bit0 ? 1 : 0;
        int i2 = bit1 ? 1 : 0;
        return 4 * i0 + 2 * i1 + i2;
    }
//    @Override
//    public String getDomain() {
//        return "B^" + 3*boardSize*boardSize;
//    }
}
