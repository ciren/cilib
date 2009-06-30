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
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to return the correct object based on the given symbol / token.
 *
 * @author Gary Pampara
 */
public final class MutationOperatorFactory {

    private enum Operators {
        ADDITION("+", "plus", "add", "additive"),
        MULTIPLICATION("*", "times", "multiplicative");

        private List<String> operationSynnomymns;

        private Operators(String... strings) {
            this.operationSynnomymns = new ArrayList<String>();
            for (String s : strings)
                this.operationSynnomymns.add(s);
        }

        public boolean contains(String operatorSymbol) {
            for (String string : operationSynnomymns)
                if (string.compareToIgnoreCase(operatorSymbol) == 0)
                    return true;

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
    public static MutationOperatorStrategy getOperatorStrategy(String operatorSymbol) {
        if (Operators.ADDITION.contains(operatorSymbol))
            return new AdditionMutationOperatorStrategy();

        if (Operators.MULTIPLICATION.contains(operatorSymbol))
            return new MultiplicationOperatorStrategy();

        StringBuilder builder = new StringBuilder();
        builder.append("Cannot determine the type of operator strategy! ");
        builder.append("Passed in token was: '" + operatorSymbol + "'.\n");
        builder.append("Please correct the token to be passed into the MutationOperatorFactory.");
        throw new UnsupportedOperationException(builder.toString());
    }

}
