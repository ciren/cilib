/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.transform;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

/**
 * Class represents a DataOperator that converts a
 * {@link StandardDataTable StandardDataTable<String>} to a
 * {@link StandardDataTable StandardDataTable<Type>}. Attempts to match
 * tokens (in order) as: {@link Real}, {@link Bit} (booleans) and then defaults
 * to {@link StringType}.
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

    public TypeConversionOperator() {}

    public TypeConversionOperator(TypeConversionOperator rhs) {
        super(rhs);
    }

    @Override
    public TypeConversionOperator getClone() {
        return new TypeConversionOperator(this);
    }

    /**
     * Applies the operator to the given {@link StandardDataTable}:
     * <p>
     * Constructs a new {@link Type} from every String token in every row.
     * All rows defined in the {@link #selectedItems} list are processed.
     * If the list is empty, all rows are processed.
     *
     * @param dataTable a DataTable of type List<String>
     * @return a new {@link StandardDataTable} of type {@link Type}.
     * @throws {@inheritDoc}
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
                        newRow.add(Real.valueOf(Double.parseDouble(object)));
                    } else if (isTrueBoolean(object)) {
                        newRow.add(Bit.valueOf(true));
                    } else if (isFalseBoolean(object)) {
                        newRow.add(Bit.valueOf(false));
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
