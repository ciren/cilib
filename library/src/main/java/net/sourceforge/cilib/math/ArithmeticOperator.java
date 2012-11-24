/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

/**
 * Basic arithmetic operations. This class provides the simple operators so that
 * they may be reused in other classes.
 *
 */
public enum ArithmeticOperator {
    ADDITION {
        @Override
        public <T extends Number> double evaluate(T left, T right) {
            return left.doubleValue() + right.doubleValue();
        }
    },
    SUBTRACTION {
        @Override
        public <T extends Number> double evaluate(T left, T right) {
            return left.doubleValue() - right.doubleValue();
        }
    },
    MULTIPLICATION {
        @Override
        public <T extends Number> double evaluate(T left, T right) {
            return left.doubleValue() * right.doubleValue();
        }
    },
    DIVISION {
        @Override
        public <T extends Number> double evaluate(T left, T right) {
            return left.doubleValue() / right.doubleValue();
        }
    };

    /**
     * Evaluate the binary operator, given the left and the right values.
     * @param <T> The {@code Number} type.
     * @param left The left operand.
     * @param right The right operand.
     * @return A double representing the result.
     */
    public abstract <T extends Number> double evaluate(T left, T right);

}
