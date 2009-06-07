/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.type;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * This is an implementation of the Domain string parser validator.
 * The validator is based on the following grammar:
 *
 * <p>
 * Grammar:
 * <ul>
 *  <li>Matrix ::= "[" + Matrix + "]" + Dim + Repeat</li>
 *  <li>Matrix ::= Domain</li>
 *     <li>Domain ::= Type + Bounds + Dim + Repeat</li>
 *     <li>Type   ::= 'B' | 'R' | 'Z' | 'T'</li>
 *     <li>Bounds ::= []</li>
 *     <li>Bounds ::= '(' + lower + ',' + upper + ')'</li>
 *     <li>Dim    ::= []</li>
 *     <li>Dim    ::= '^' + dim</li>
 *  <li>Repeat ::= []</li>
 *  <li>Repeat ::= ',' + Matrix</li>
 *     <li>lower  ::= double | int</li>
 *     <li>upper  ::= double | int</li>
 *     <li>dim    ::= int</li>
 * </ul>
 *
 * where <tt>[]</tt> represents an empty string.
 *
 * @author Gary Pampara
 * @deprecated in favor of the JavaCC generated parser.
 */
public class DomainValidator {

    private StreamTokenizer parser;
    private ArrayList<String> expandedDomain;
    private String expandedString = "";
    private String tmpString = "";
    private boolean vectorString = false;


    /**
     * Empty default constructor.
     */
    public DomainValidator() {
    }


    /**
     * Constructer, setting the parser to the specified <tt>StreamTokenizer</tt>.
     * @param validationParser The <tt>StreamTokenizer</tt> to be used.
     */
    public DomainValidator(StreamTokenizer validationParser) {
        parser = validationParser;
        expandedDomain = new ArrayList<String>();

        expandedDomain.add("");
    }


    /**
     * Validate the given domain string.
     *
     * @param domain The domain string to be validated
     * @return <code>true</code> if the domain is valid; <code>false</code> otherwise
     */
    public boolean validate(String domain) {
        Reader domainReader = new CharArrayReader(domain.toCharArray());
        parser = new StreamTokenizer(domainReader);
        boolean result = false;

        try {
            result = parseMatrix();
            addToExpanded(0); // Finally flush out any left over parsing to the expanded string
            //System.out.println("result: " + result);
        }
        catch (Exception e) {
            throw new RuntimeException("Error during parsing of domain string\n" + e);
        }

        return result;
    }


    /**
     *
     * @return
     * @throws IOException
     */
    private boolean parseMatrix() throws IOException {
        parser.nextToken();

        if ((char) parser.ttype == '[') {
            this.vectorString = true;
            //System.out.println("Found [");
            //parseDomain();
            parseMatrix();
            accept(']'); //write("]");

            parser.nextToken();
            parseDimension();
            parseRepeat();
            this.vectorString = false;
        }
        else {
            //System.out.println("asdsada");
            parser.pushBack();
            parseDomain();
        }

        return true;
    }

    /**
     * Parse the domain. Corresponds to the productions rule:
     * <ul>
     *     <li>Domain ::= Type + Bounds + Dim</li>
     * </ul>
     *
     * @return <code>true</code> if the domain is parser correctly.
     * @throws IOException - if an I/O error occurs
     */
    private boolean parseDomain() throws IOException {

        parseType();
        parseBounds();
        parseDimension();
        parseRepeat();

        return true;
    }


    /**
     * Parse the <tt>Type</tt> production rule. Corresponds to:
     *
     * <ul>
     * <li>Type   ::= 'R' | 'Z' | 'B' | 'T'</li>
     * </ul>
     *
     * @throws IOException - if an I/O error occurs
     */
    private void parseType() throws IOException {
        int result = parser.nextToken();

        if (result == StreamTokenizer.TT_WORD) {
            String tmp = parser.sval;
            //System.out.println("Word: " + tmp);

            if (
                    tmp.equals("R") |
                    tmp.equals("Z") |
                    tmp.equals("B") |
                    tmp.equals("T")
                    ) {
                //System.out.println("Found:" + tmp);
                write(tmp); // to expand the domain
                return;
            }
            else
                throw new RuntimeException("Parser error: unknown Type");
        }
    }


    /**
     * Parse the bounds production rule. Corresponds to:
     *
     * <ul>
     *     <li>Bounds ::= []</li>
     *     <li>Bounds ::= '(' + lower + ',' + upper + ')'</li>
     * </ul>
     *
     * @throws IOException - if an I/O error occurs
     */
    private void parseBounds() throws IOException {
        parser.nextToken();

        if ((char) parser.ttype == ',') {
            parser.pushBack();
            return;
        }

        if ((char) parser.ttype == '^') {
            parser.pushBack();
            return;
        }

        if (parser.ttype == StreamTokenizer.TT_EOF) { // We only have a Type: R
            //System.out.println("End of stream");
            return;
        }
        else {
            accept('('); write("(");
            parser.nextToken();
            double lower = parser.nval; write(Double.valueOf(lower).toString());
            //System.out.println("lower: " + lower);
            parser.nextToken();
            accept(','); write(",");
            parser.nextToken();
            double upper = parser.nval; write(Double.valueOf(upper).toString());
            //System.out.println("upper: " + upper);
            parser.nextToken();
            accept(')'); write(")");

            parser.nextToken();
        //    System.out.println("asd: "+(char) parser.ttype);
            parser.pushBack();
        }

    }


    /**
     * Parse the <tt>Dim</tt> component of the production rules.
     * Corresponding production rule:
     *
     * <ul>
     * <li>Dim    ::= []</li>
     * <li>Dim    ::= '^' + dim</li>
     * </ul>
     *
     * @throws IOException - if an I/O error occurs
     */
    private void parseDimension() throws IOException {
        parser.nextToken();

        //System.out.println("current: " + (char) parser.ttype);

        if ((char) parser.ttype == ',') {
            parser.pushBack();
            return;
        }

        if (parser.ttype == StreamTokenizer.TT_EOF) { // This is not a '^'
            // System.out.println("Domain has no dimensions");
        }
        else {
        //    System.out.println("here");
            accept('^');
            //System.out.println("here2");
            parser.nextToken();
            double dim = parser.nval;
            //System.out.println("Dimension: " + dim);
            addToExpanded(Double.valueOf(dim).intValue());
        }

        //System.out.println("Expanded: " + this.expandedString);
    }


    /**
     * Parse the <tt>Repeat</tt> component of the production rules.
     * Corresponding to the production rule:
     *
     * <ul>
     *  <li>Repeat ::= []</li>
     *  <li>Repeat ::= ',' + Domain</li>
     * </ul>
     *
     * @throws IOException - if an I/O error occurs
     */
    private void parseRepeat() throws IOException {
        parser.nextToken();
        //System.out.println("Current symbol: " + (char) parser.ttype);

        if ((char) parser.ttype == ']') {
            //System.out.println("Found ]");
            parser.pushBack();
            return;
        }

        if (parser.ttype == StreamTokenizer.TT_EOF) {
            //System.out.println("EOF in parseRepeat");
            return;
        }
        else {
            accept(','); write(",");
            addToExpanded(0);
            //parseDomain();
            parseMatrix();
        }
    }


    /**
     * Utility method to eat tokens.
     * @param c The expected token to eat.
     */
    private boolean accept(char c) {
        if (((char) parser.ttype) == c)
            return true;
        else
            throw new RuntimeException("Parser error: " + c + " was expected but received: " + (char) parser.ttype);
    }






    /**
     * Expand the domain string to a representation that the <tt>DomainBuilder</tt>.
     * @param domain The domain string to be expanded.
     * @return The expanded string.
     */
    public String expandString(String domain) {
        this.expandedString = "";
        this.tmpString = "";
        this.validate(domain);

        if (this.expandedString.equals("")) { // the dimension was never called
            this.expandedString = new String(this.tmpString);
        }

        return this.expandedString;
    }


    /**
     * Append the given string to the current string.
     * @param s The string to be added.
     */
    private void write(String s) {
        //System.out.println("processed: " + s);
        tmpString += s;
    }


    /**
     * Add the current accumulated string to the expanded string, copying the
     * string as many times as defined by <tt>dim</tt>.
     * @param dim The number of copies to be made.
     */
    private void addToExpanded(int dim) {
        String tmp = "";

        //System.out.println("\t\ttmpString: \""+ this.tmpString + "\"");
        for (int i = 0; i < dim-1; i++) {
            //this.expandedString += this.tmpString;
            //this.expandedString += ",";
            tmp += this.tmpString;
            tmp += ",";
        }

        //this.expandedString += this.tmpString;
        tmp += this.tmpString;

        if (this.vectorString) {
            tmp = "[" + tmp + "]";
            this.tmpString = tmp;
        }
        else if (!this.vectorString) {
            this.expandedString += tmp;
            this.tmpString = "";
        }

        this.vectorString = false;
    }
}
