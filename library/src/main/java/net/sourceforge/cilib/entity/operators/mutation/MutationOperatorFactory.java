/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
