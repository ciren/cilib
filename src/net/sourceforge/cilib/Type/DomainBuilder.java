/*
 * DomainBuilder.java
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

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

import net.sourceforge.cilib.Type.Types.Bit;
import net.sourceforge.cilib.Type.Types.MixedVector;
import net.sourceforge.cilib.Type.Types.Real;
import net.sourceforge.cilib.Type.Types.Vector;

/**
 * @author gary
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DomainBuilder {
	
	private int level;
	private MixedVector representation;
	private MixedVector tmpVector;
	private String domainRepresentation;
	
	/**
	 * Build an instance of the DomainBuilder with nothing initialised
	 *
	 */
	public DomainBuilder() {
		level = 0;
		domainRepresentation = "";
	}
	
	
	/**
	 * Build the representation of the expanded domain string.
	 * @param representation The expanded domain string
	 * @return A container containing the built represenation
	 */
	public Vector buildRepresentation(String representation) {
		boolean update = false;
		
		if (!this.domainRepresentation.equals(representation)) {
			this.domainRepresentation = representation;
			update = true;
		}
		
		tmpVector = new MixedVector();
		MixedVector tempVector = new MixedVector();
		
		Reader reader = new CharArrayReader(representation.toCharArray());
		StreamTokenizer parser = new StreamTokenizer(reader);
		
		try {
			while (parser.nextToken() != StreamTokenizer.TT_EOF) {
				if (parser.ttype == StreamTokenizer.TT_WORD) {		
					if (parser.sval.equals("R")) {
						if (level != 0)
							tempVector.add(buildReal(parser));
						else
							tmpVector.add(buildReal(parser)); // Should this actually build the real or just expand it?
					}
					else if (parser.sval.equals("B")) {
						if (level != 0)
							tempVector.add(buildBit(parser));
						else
							tmpVector.add(buildBit(parser));
					}
				}
				else if (parser.ttype == StreamTokenizer.TT_NUMBER) {
					//System.out.println("Number: " + parser.nval);
				}
				else {
					//System.out.println("Other: " + (char) parser.ttype);
					char current = (char) parser.ttype;
					
					if (current == '[') {
						level++;
					}
					else if (current == ']') {
						level--;
						tmpVector.add(tempVector);
						tempVector = new MixedVector();
					}
				}
			}
		}
		catch (IOException io) {
			io.printStackTrace();
		}

		if (update || this.representation == null)
			this.representation = tmpVector;
		
		return tmpVector;
	}

	
	/**
	 * Build a component of the type Real using the provided <code>StreamTokenizer</code> 
	 * @param realParser The current parser to be used
	 * @return An object of type <code>Real</code> initialised based on the findings within the <code>StreamTokenizer</code>
	 */
	public Real buildReal(StreamTokenizer realParser) {
		Real result = null;
		double lower = 0.0;
		double upper = 0.0;
		
		try {
			realParser.nextToken();
			char current = (char) realParser.ttype;
			//System.out.println("current: " + current);
		
			if (current == '(') { // We have something in the form: R(x,y) 
				eat(realParser.ttype, '('); realParser.nextToken();
				
				//System.out.println("type: " + realParser.ttype);
				
				if (realParser.ttype == StreamTokenizer.TT_NUMBER)
					lower = realParser.nval;
				else if (realParser.ttype == StreamTokenizer.TT_WORD)
					lower = Double.NEGATIVE_INFINITY;
				
				//System.out.println("lower: " + lower);
				
				realParser.nextToken();
				eat(realParser.ttype, ','); realParser.nextToken();
				
				if (realParser.ttype == StreamTokenizer.TT_NUMBER)
					upper = realParser.nval;
				else if (realParser.ttype == StreamTokenizer.TT_WORD) {
				//	System.out.println("Word read");
					upper = Double.POSITIVE_INFINITY;
				}
				
				realParser.nextToken();
				eat(realParser.ttype, ')');
				
				result = new Real(lower, upper);
			}
			else { // We have something in the form: R
				realParser.pushBack(); // Rewind, we have gone to far
				result = new Real();
			}
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		
		return result;
	}
	
	
	public Bit buildBit(StreamTokenizer bitParser) {
		Bit result = null;
		
		result = new Bit();
		
		return result;
	}
	
	
	/**
	 * Ensure that the desired token is in fact available at the correct time  
	 * @param source The token currently being referenced
	 * @param target The target token expected
	 * @throws RuntimeException if there is a problem with the domain string (where the source and the target are not identical)
	 */
	public void eat(int source, char target) {
		char sourceChar = (char) source;
		
		if (sourceChar == target)
			return;
		else
			throw new RuntimeException("Invalid domain string data at: " + sourceChar + " expected: " + target);
	}
	
	public Vector getBuiltRepresenation() {		
		return this.representation;
	}
	
	public Vector getInitialisedRepresentation() {
		return this.buildRepresentation(domainRepresentation);
	}
}
