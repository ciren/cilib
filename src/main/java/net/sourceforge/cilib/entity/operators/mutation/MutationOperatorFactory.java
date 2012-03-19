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
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.math.ArithmeticOperator;

/**
 * Factory to return the correct object based on the given symbol / token.
 *
 */
public final class MutationOperatorFactory {

    private enum Operators {
        ADDITION("+", "plus", "add", "additive"),
        MULTIPLICATION("*", "times", "multiplicative");

        private List<String> operationSynnomymns;

        Operators(String... strings) {
            this.operationSynnomymns = new ArrayList<String>();
            for (String s : strings) {
                this.operationSynnomymns.add(s);
            }
        }

        boolean contains(String operatorSymbol) {
            for (String string : operationSynnomymns) {
                if (string.compareToIgnoreCase(operatorSymbol) == 0) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Remove access to the default constructor.
     */
    private MutationOperatorFactory() {
    }


    /**
     * Factory method to create the correct operator implementation object.
     * @param operatorSymbol A symbol or word describing the symbol
     * @return The <tt>MutationOperatorStrategy</tt> associated to the meaning of the operatorSymbol.
     */
    public static ArithmeticOperator getOperatorStrategy(String operatorSymbol) {
        if (Operators.ADDITION.contains(operatorSymbol)) {
            return ArithmeticOperator.ADDITION;
        }

        if (Operators.MULTIPLICATION.contains(operatorSymbol)) {
            return ArithmeticOperator.MULTIPLICATION;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Cannot determine the type of operator strategy! ");
        builder.append("Passed in token was: '" + operatorSymbol + "'.\n");
        builder.append("Please correct the token to be passed into the MutationOperatorFactory.");
        throw new UnsupportedOperationException(builder.toString());
    }

}
