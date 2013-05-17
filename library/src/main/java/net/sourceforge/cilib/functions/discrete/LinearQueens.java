/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;

/**
 * Decorator of the N-Queens problem to linearly penalise infeasible solutions.
 *
 * The function determines the fitness of the solution by first linearly
 * penalising solutions with an incorrect number of queens. If a solution has
 * the correct number of queens, the delegate {@link Queens} problem is used to
 * calculate the fitness by determining the number of conflicts.
 */
public class LinearQueens implements DiscreteFunction {
    private ControlParameter penalty;
    private Queens delegate;

    /**
     * Create a new instance of this function decorator. The delegate's default
     * board size is 8.
     */
    public LinearQueens() {
        this.penalty = ConstantControlParameter.of(100);
        this.delegate = new Queens();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy  the instance to copy.
     */
    public LinearQueens(LinearQueens copy) {
        this.penalty = copy.penalty.getClone();
        this.delegate = copy.delegate;
    }

    /**
     * Get the matrix and first determine if there are insufficient or excess
     * queens. If the amount of queens is correct, pass the input {@code Vector}
     * to the delegate.
     *
     * @param input     the {@code input} {@linkplain Vector}
     * @return          the fitness of the input {@code Vector}
     */
    @Override
    public Integer apply(Vector input) {
        boolean[][] board = delegate.initialiseBoard(input);
        int numQueens = delegate.numberOfQueens(board);
        int boardSize = delegate.getBoardSize();

        if (numQueens != boardSize) {
            return (int)penalty.getParameter() * Math.abs(numQueens - boardSize);
        }

        return delegate.apply(input);
    }

    /**
     * Set the size of the board using the delegate's setter.
     * @param boardSize the {@code boardSize} to set
     */
    public void setBoardSize(int boardSize) {
        delegate.setBoardSize(boardSize);
    }

    /**
     * Set the {@code penalty} {@linkplain ControlParameter} that determines
     * the penalty factor given to solutions with insufficient or excess queens.
     *
     * By default this is set to {@code 100}.
     *
     * @param penalty   the {@code penalty} {@linkplain ControlParameter}
     */
    public void setPenalty(ControlParameter penalty) {
        this.penalty = penalty;
    }
}
