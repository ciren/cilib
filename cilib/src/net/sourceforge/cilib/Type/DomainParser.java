/*
 * DomainParser.java
 * 
 * Created on Oct 18, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
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
package net.sourceforge.cilib.Type;



/**
 * @author espeer
 *
 */
public class DomainParser {
	
	public DomainParser(DomainBuilder builder) {
		this.builder = builder;
	}
	
	public void build(String domain) {
		this.domain = domain;
		index = -1;
		next();
		whitespace();
		domain();
		whitespace();
		if (sym != 65535) {
			System.out.println(sym + " (" + (int) sym + ")");
			error("Expected end of stream");
		}
	}
	
	private void error(String message) {
		throw new IllegalArgumentException(message);
	}
	
	private void next() {
		if (++index < domain.length()) {
			sym = (char) domain.charAt(index);
		}
		else {
			sym = (char) -1;
		}
	}
	
	private void whitespace() {
		while (Character.isWhitespace(sym)) {
			next();
		}
	}
	
	private void domain() {
		int start = index;
		switch (sym) {
			case '[': prefixVector(); break;
			case 'B': bit(); break;
			case 'R': real(); break;
			case 'Z': integer(); break;
			default: error("Expected a valid domain component");
		}
		while (sym == '^') {
			String last = domain.substring(start, index);
			postfixVector(last);			
		}
	}
	
	private void prefixVector() {
		if (sym == '[') {
			next();
		}
		else {
			error ("Expected '[");
		}
		builder.beginBuildPrefixVector();
		while (sym != ']') {
			whitespace();
			domain();
			whitespace();
			if (sym != ']' && sym != ',') {
				error("Expected ','");
			}
			if (sym == ',') {
				next();
			}
		}
		next();
		builder.endBuildPrefixVector();
	}
	
	private void postfixVector(String last) {
		if (sym == '^') {
			next();
		}
		int dimension = parseInteger();
		int slack = 0;
		if (sym == '~') {
			next();
			if (sym == 'N') {
					next();
					slack = Integer.MAX_VALUE;
			}
			else {
				slack = parseInteger();
			}
		}
		builder.buildPostfixVector("[" + last + "]", dimension, slack);
	}

	private void bit() {
		if (sym == 'B') {
			next();
		}
		else {
			error("Expected 'B'");
		}
		builder.buildBit();
	}

	
	private void real() {
		if (sym == 'R') {
			next();
		}
		else {
			error("Expected 'R'");
		}
		
		whitespace();

		double lower = Double.NEGATIVE_INFINITY;
		double upper = Double.POSITIVE_INFINITY;
		
		if (sym == '(') {		
			next();
			whitespace();
			if (sym != ',') {
				lower = parseReal();
			}
			whitespace();
			if (sym == ',') {
				next();
			}
			else {
				error ("Expected ','");
			}
			whitespace();
			if (sym != ')') {
				upper = parseReal();
			}
			whitespace();
			if (sym == ')') {
				next();
			}
			else {
				error("Expected ')'");
			}
		}

		builder.buildReal(lower, upper);
	}


	private void integer() {
		if (sym == 'Z') {
			next();
		}
		else {
			error("Expected 'Z'");
		}
		
		int lower = Integer.MIN_VALUE;
		int upper = Integer.MAX_VALUE;
		
		whitespace();
		
		if (sym == '(') {		
			next();
			whitespace();
			if (sym != ',') {
				lower = parseInteger();
			}
			whitespace();
			if (sym == ',') {
				next();
			}
			else {
				error ("Expected ','");
			}
			whitespace();
			if (sym != ')') {
				upper = parseInteger();
			}
			whitespace();
			if (sym == ')') {
				next();
			}
			else {
				error("Expected ')'");
			}
		}
		
		builder.buildInt(lower, upper);
	}
	
	private int parseInteger() {
		StringBuffer tmp = new StringBuffer();
		if (sym == '+' || sym == '-') {
			tmp.append(sym);
			next();
		}
		while (Character.isDigit(sym)) {
			tmp.append(sym);
			next();
		}
		return Integer.parseInt(tmp.toString());
	}
	
	private double parseReal() {
		StringBuffer tmp = new StringBuffer();
		if (sym == '+' || sym == '-') {
			tmp.append(sym);
			next();
		}
		while (Character.isDigit(sym) || sym == 'E' || sym == 'e' || sym == '.') {
			tmp.append(sym);
			next();
		}
		return Double.parseDouble(tmp.toString());
	}
	
	private char sym;
	private int index;
	
	private String domain;
	private DomainBuilder builder;
	
}
