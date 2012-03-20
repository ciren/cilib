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
    DEVISION {
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
