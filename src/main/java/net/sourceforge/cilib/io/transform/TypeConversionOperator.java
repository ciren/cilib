/*
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
package net.sourceforge.cilib.io.transform;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

/**
 * Class represents a DataOperator that converts a {@link StandardDataTable<String> StandardDataTable<String>}
 * to a {@link StandardDataTable<Type> StandardDataTable<Type>}. Attempts to match
 * tokens (in order) as: Real, Bit (booleans) and then defaults to StringType.
 * @author andrich
 */
public class TypeConversionOperator extends SelectiveDataOperator {

    /// IMPORTANT NOTE:
    /// Taken from Java 1.5 API docs, regular expression that matches any double,
    /// avoids the NumberFormatException
    private final String Digits = "(\\p{Digit}+)";
    private final String HexDigits = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
    private final String Exp = "[eE][+-]?" + Digits;
    private final String doubleRegularExpresion =
            ("[\\x00-\\x20]*" + // Optional leading "whitespace"
            "[+-]?(" + // Optional sign character
            "NaN|" + // "NaN" string
            "Infinity|" + // "Infinity" string

            // A decimal floating-point string representing a finite positive
            // number without a leading sign has at most five basic pieces:
            // Digits . Digits ExponentPart FloatTypeSuffix
            //
            // Since this method allows integer-only strings as input
            // in addition to strings of floating-point literals, the
            // two sub-patterns below are simplifications of the grammar
            // productions from the Java Language Specification, 2nd
            // edition, section 3.10.2.

            // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
            "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +
            // . Digits ExponentPart_opt FloatTypeSuffix_opt
            "(\\.(" + Digits + ")(" + Exp + ")?)|" +
            // Hexadecimal strings
            "((" +
            // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "(\\.)?)|" +
            // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
            ")[pP][+-]?" + Digits + "))" +
            "[fFdD]?))" +
            "[\\x00-\\x20]*");// Optional trailing "whitespace"

    /**
     * Applies the operator to the given DataTable: Constructs a new {@link Type Type}
     * from every String token in every row. All rows defined
     * in the {@link #selectedItems selectedItems} list are processed. If the list is
     * empty, all rows are processed.
     * @param dataTable a DataTable of type List<String>
     * @return a new {@link StandardDataTable StandardDataTable} of type {@link Type Type}.
     * @throws {@inheritDoc }
     */
    @Override
    public DataTable operate(DataTable dataTable) throws CIlibIOException {

        StandardDataTable<Type> resultTable = new StandardDataTable<Type>();

        int size = dataTable.getNumRows();
        for (int i = 0; i < size; i++) {
            if (selectedItems.isEmpty() || selectedItems.contains(i)) {
                List row = (List) dataTable.getRow(i);
                List<Type> newRow = new ArrayList<Type>(row.size());
                for (Object token : row) {
                    String object = token.toString();
                    if (isDouble(object)) {
                        newRow.add(new Real(Double.parseDouble(object)));
                    } else if (isTrueBoolean(object)) {
                        newRow.add(new Bit(true));
                    } else if (isFalseBoolean(object)) {
                        newRow.add(new Bit(false));
                    } else {
                        newRow.add(new StringType(object));
                    }
                }
                resultTable.addRow(newRow);
            }
        }
        return resultTable;
    }

    /**
     * Tests the token for a Double string.
     * @param token a string token.
     * @return true if string matches Double regular expression.
     */
    private boolean isDouble(String token) {
        return token.matches(doubleRegularExpresion);
    }

    /**
     * Tests the token for a Boolean string.
     * @param token a string token.
     * @return true if string equals (ignoring case) "t" or "True"
     */
    private boolean isTrueBoolean(String token) {
        if ((token.compareToIgnoreCase("t") == 0) | (token.compareToIgnoreCase("true") == 0)) {
            return true;
        }
        return false;
    }

    /**
     * Tests the token for a Boolean string.
     * @param token a string token.
     * @return true if string equals (ignoring case) "f" or "False"
     */
    private boolean isFalseBoolean(String token) {
        if ((token.compareToIgnoreCase("f") == 0) || (token.compareToIgnoreCase("false") == 0)) {
            return true;
        }
        return false;
    }
}
