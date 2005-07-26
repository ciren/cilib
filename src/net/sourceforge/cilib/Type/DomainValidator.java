/*
 * DomainValidator.java
 * 
 * Created on Jun 13, 2005
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.Type;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * @author gary
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DomainValidator {
	
	private StreamTokenizer parser;
	
	private int vector; // The variable indicates that we are inside an inner vector
	private int dimension; // Count the number of dimensions 
	private ArrayList<String> expandedDomain;
	
	public DomainValidator(StreamTokenizer validationParser) {
		parser = validationParser;
		expandedDomain = new ArrayList<String>();
		
		expandedDomain.add("");
	}

	
	/**
	 * Determine if the current domain is valid. The only way to do so is to parse the string and 
	 * return the string represenation from the required classes.
	 *  
	 * @return <code>true</code> if the validator validates the domain string, <code>false</code> otherwise
	 */
	public boolean isValid() {
		boolean result = false;
		
		try {
			while (parser.nextToken() != StreamTokenizer.TT_EOF) {
				if (parser.ttype == StreamTokenizer.TT_WORD) {
					dimension++;
				
					if (parser.sval.equals("R")) {
						validateReal(parser); // Should this actually build the real or just expand it?
					}
					else if (parser.sval.equals("B")) {
						//System.out.println("B found");
						validateBit(parser);
					}
				}
				else if (parser.ttype == StreamTokenizer.TT_NUMBER) {
				}
				else {
					char current = (char) parser.ttype;
					
					if (current == '[') {
						vector++;
						expandedDomain.add("");
						//System.out.println("inner vector");
						expandedDomain.set(vector, expandedDomain.get(vector) + '[');
					}
					else if (current == ']') {
						String s = expandedDomain.get(vector);
						s += ']';
						expandedDomain.remove(vector);
						vector--;
						
						parser.nextToken();
						if ((char) parser.ttype == '^') {
							parser.nextToken();
							int number = Double.valueOf(parser.nval).intValue();
							
							for (int i = 0; i < number-1; i++)
								expandedDomain.set(vector, expandedDomain.get(vector) + s + ",");
							expandedDomain.set(vector, expandedDomain.get(vector) + s);
						}
					}
					else if (tokenNotValid(current)) {
						break;
					}
					else
						expandedDomain.set(vector, expandedDomain.get(vector) + ','); // Added to solve the missing ',' problem
				}
			}
			
			result = true;
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		
		return result;
	}


	/**
	 * @return
	 */
	private boolean tokenNotValid(char current) {
		//System.out.println(String.valueOf(current).matches("R|B|Z|[0-9]|^|-|,"));
		return String.valueOf(current).matches("R|B|Z|[0-9]|^|-"); 
	}


	/**
	 * Get the validated expanded domain string from the domain
	 * 
	 * @return The validated expanded domain string
	 */
	public String getValidatedString() {
		String validatedString = "";
		
		if (expandedDomain.get(vector) == "") {
			expandedDomain.set(vector, "");
		
			if (isValid()) {
				validatedString = expandedDomain.get(vector).toString();
			}
			else {
				throw new RuntimeException("The Domain String is not valid.... Please correct");
			}
		}

		return validatedString;
	}

	
	/**
	 * 
	 * @param realParser
	 */
	private void validateReal(StreamTokenizer realParser) {
		String result = "";
		
		try {
			realParser.nextToken();
		
			if ( (char) realParser.ttype == '^') { // We have something of the form R^x
				realParser.nextToken();
				int length = Double.valueOf(realParser.nval).intValue();
				
				for (int i = 0; i < length-1; i++)
					result += "R,";
				result += 'R';
				
				expandedDomain.set(vector, expandedDomain.get(vector) + result);
			}
			else { // We have something of the form R(x,y)
				eat(realParser.ttype, '('); realParser.nextToken();
				double lower = realParser.nval; realParser.nextToken();
				eat(realParser.ttype, ','); realParser.nextToken();
				double upper = realParser.nval; realParser.nextToken();
				eat(realParser.ttype, ')'); realParser.nextToken();
				
				if ((char) realParser.ttype == '^') {
					realParser.nextToken();
					int length = Double.valueOf(realParser.nval).intValue();
									
					for (int i = 0; i < length; i++) {
						result += "R(" + String.valueOf(lower) + "," + String.valueOf(upper) + ")";
						
						if (realParser.ttype != StreamTokenizer.TT_EOF && (i+1) != length)
							result += ',';
					}
				}
				else
					realParser.pushBack(); // Rewind to the previous token to avoid errors

				expandedDomain.set(vector, expandedDomain.get(vector) + result);
			}
		}
		catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param parser
	 */
	private void validateBit(StreamTokenizer parser) {
		String result = "";
		
		try {
			parser.nextToken();
		
			if ( (char) parser.ttype == '^') { // We have something of the form B^x
				parser.nextToken();
				int length = Double.valueOf(parser.nval).intValue();
				
				for (int i = 0; i < length-1; i++)
					result += "B,";
				result += 'B';
				
				expandedDomain.set(vector, expandedDomain.get(vector) + result);
			}
			else if ( (char) parser.ttype == ',') {
				parser.pushBack();
				return;
			}
		}
		catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	public void eat(int source, char target) {
		char sourceChar = (char) source;
		
		if (sourceChar == target)
			return;
		else
			throw new RuntimeException("Invalid Domain String data at: " + sourceChar + " expected: " + target);
	}
	
	
	public int getDimension() {
		return dimension;
	}
}
