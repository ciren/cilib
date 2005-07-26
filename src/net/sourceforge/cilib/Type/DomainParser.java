/*
 * DomainParser.java
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
import java.io.Reader;
import java.io.StreamTokenizer;

import net.sourceforge.cilib.Type.Types.MixedVector;
import net.sourceforge.cilib.Type.Types.Real;
import net.sourceforge.cilib.Type.Types.Type;
import net.sourceforge.cilib.Type.Types.Vector;

/**
 * @author gary
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DomainParser {

	private DomainBuilder builder;
	private DomainValidator validator;
	private String domain;
	private String expandedRepresenation;
	
	private static DomainParser instance;
	
	private DomainParser() {
		builder = new DomainBuilder();
		domain = "";
		expandedRepresenation = null;
	}
	
	public static DomainParser getInstance() {
		if (instance == null)
			instance = new DomainParser();
		
		return instance;
	}
	
	/**
	 * Build the representation and expand the domain string if the expanded domain string does not exist
	 * 
	 * @param domain The domain string to be parsed
	 */
	public void parse(String domain) {
		//System.out.println("received domain: " + domain);
		//Reader reader = new CharArrayReader(domain.toCharArray());
		//StreamTokenizer parser = new StreamTokenizer(reader);
				
		if (!this.domain.equals(domain)) {
			//System.out.println("Expanding domain...");
			this.domain = domain;
			expandedRepresenation = expandDomainString(domain);
		}
		
		//System.out.println("Expanded domain: \n" + expandedRepresenation + "\n");
		//System.out.println("Built represenation....\n");
		//net.sourceforge.cilib.Type.Types.Vector representation = builder.buildRepresentation(expandedRepresenation);
		//assert representation != null;
		
		builder.buildRepresentation(expandedRepresenation);
		//for (int i = 0; i < representation.getDimension(); i++) {
		//	Type t = representation.get(i);
			//System.out.println(t.toString());
		//}
		
		/*for (ListIterator l = representation.listIterator(); l.hasNext(); ) {
			System.out.println(l.next());
		}
		
		System.out.println("size: " + representation.size());*/
		
		/*try {
			while (parser.nextToken() != StreamTokenizer.TT_EOF) {
				if (parser.ttype == StreamTokenizer.TT_WORD) {
					System.out.println("word: " + parser.sval);
					
					if (parser.sval.equals("R")) {
						System.out.println("Found a R!!!");
						buildR(parser); // Should this actually build the real or just expand it?
					}
				}
				else if (parser.ttype == StreamTokenizer.TT_NUMBER) {
					System.out.println("Number: " + parser.nval);
				}
				else {
					System.out.println("Other: " + (char) parser.ttype);
				}
			}
		}
		catch (IOException io) {
			io.printStackTrace();
		}*/
	}
	

	/**
	 * Build the expanded string representation of the domain string.
	 * 
	 * @param domain The string representing the domain to be built
	 * @return A <code>String</code> representing the expanded represenation
	 */
	private String expandDomainString(String domain) {
		Reader reader = new CharArrayReader(domain.toCharArray());
		StreamTokenizer validationParser = new StreamTokenizer(reader);
		
		validator = new DomainValidator(validationParser);
		
		return validator.getValidatedString();
	}

	
	/**
	 * 
	 * @param domain
	 * @return
	 */
	public boolean validate(String domain) {
		Reader reader = new CharArrayReader(domain.toCharArray());
		StreamTokenizer validationParser = new StreamTokenizer(reader);
		
		DomainValidator validator = new DomainValidator(validationParser);
		
		return validator.isValid();
	}
	
	public String getExpandedRepresentation() {
		return this.expandedRepresenation;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDimension() {
		MixedVector v = (net.sourceforge.cilib.Type.Types.MixedVector) builder.getBuiltRepresenation();
		return v.getDimension();
	}
	
	public Type getBuiltRepresentation() {
		return builder.getBuiltRepresenation();
	}

	public Type getInitialisedRepresenation() {
		Type result = null;
		
		try {
			result = (Type) builder.getBuiltRepresenation().clone();
			result.randomize();
		}
		catch (CloneNotSupportedException c) {
			throw new RuntimeException(c);
		}
		
		return result;
	}
	
	/**
	 * @param solution
	 * @return
	 */
	public boolean isInside(Object solution) {
		boolean result = true;
		Vector vector = (Vector) solution;
		
		for (int i = 0; i < vector.getDimension(); i++) {
			Type t = vector.get(i);
			
			if (t instanceof Vector) {
				result = isInside(t);
			}
			else if (t instanceof Real) {
				Real r = (Real) t;
				double value = r.getReal();
				if (value >= r.getLowerBound() && value <= r.getUpperBound()) {
					continue;
				}
				else {
					result = false;
				}
			}
			
			if (!result)
				break;
		}
		
		return result;
	}
}
