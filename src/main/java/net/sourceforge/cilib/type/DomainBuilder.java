/*
 * DomainBuilder.java
 * 
 * Created on Jun 13, 2005
 *
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.type;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StreamTokenizer;

import net.sourceforge.cilib.type.creator.TypeCreator;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is an implementation of the Domain string type generator.
 * 
 * <p>
 * Grammar:
 * <ul>
 * <li>Matrix   ::= "[" + Domain + "]" + Repeated</li>
 * <li>Matrix   ::= Domain</li>
 * <li>Domain   ::= Type + Bounds + Repeated</li>
 * <li>Bounds   ::= []</li>
 * <li>Bounds   ::= '(' + lower + ',' + upper + ')'</li>
 * <li>Repeated ::= []</li>
 * <li>Repeated ::= ',' + Matrix</li>
 * <li>Type     ::= 'B' | 'R' | 'Z' | 'T'</li>
 * <li>lower    ::= double | int</li>
 * <li>upper    ::= double | int</li>
 * </ul>
 * 
 * @author Gary Pampara
 */
public class DomainBuilder {
	
	private Vector representation;
	private StreamTokenizer parser;
	private double lower;
	private double upper;
	private Object objectType;
	
	/**
	 * Build an instance of the DomainBuilder with nothing initialised,
	 * except for the default varialbes to default values. 
	 *
	 */
	public DomainBuilder() {
		lower = -Double.MAX_VALUE;
		upper = Double.MAX_VALUE;
	}
	
	
	/**
	 * Build the representation of the expanded domain string.
	 * @param expandedDomain The expanded domain string
	 * @return A container containing the built represenation
	 */
	public Vector build(String expandedDomain) {
		
		representation = new Vector();
		parser = new StreamTokenizer(new CharArrayReader(expandedDomain.toCharArray()));
		
		try {
			buildMatrix();
			//buildDomain();
		}
		catch (IOException e) {
			throw new RuntimeException("IOException occoured during building of the domain\n" + e);
		}
		
		return representation;
	}

	
	/**
	 * Get the built / constructed representation as a <tt>Type</tt> object.
	 * 
	 * @return The constructed <tt>Type</tt> object representing the domain string.
	 */
	public Type getBuiltRepresenation() {
		return this.representation;
	}
	
	
	/**
	 * 
	 * @throws IOException
	 */
	private void buildMatrix() throws IOException {
		parser.nextToken();
		
		if ((char) parser.ttype == '[') {
			final Vector tmp = this.representation;
			this.representation = new Vector();
			buildDomain();
			
			parser.nextToken();
			accept(']');
			
			// We know that we have a vector already :)
			tmp.add(representation);
			this.representation = tmp;
			
			buildRepeat();
		}
		else {
			parser.pushBack();
			buildDomain();
		}
	}
	
	
	/**
	 * Start the parser off. This method corresponds to the rule:
	 * 
	 * Domain   ::= Type + Bounds + Repeated
	 * 
	 * @throws IOException - if an I/O Error occours
	 */
	private void buildDomain() throws IOException {
		buildType();
		buildBounds();
		buildTypeObject(); // This method is not part of the grammar, however it is the logical place to actually build the type and add it to the Vector
		buildRepeat();
	}
	
	
	/**
	 * Parse the type of the domain string.
	 * 
	 * Corresponds to production rule:
	 * <ul>
	 * <li>Type ::= 'R' | 'Z' | 'B' | 'T'</li>
	 * </ul>
	 *  
	 * @throws IOException - if an I/O Error occours
	 */
	private void buildType() throws IOException {
		parser.nextToken();
		
		if (parser.sval.equals("R") |
				parser.sval.equals("Z") |
				parser.sval.equals("B") |
				parser.sval.equals("T")
				)
		{
			this.objectType = parser.sval;
		}
		
	}
	
	
	/**
	 * Build the bounds of the type if needed, else return.
	 * 
	 * This method corresponds to the production rules:
	 * 
	 * <ul>
	 * <li>Bounds   ::= []</li>
	 * <li>Bounds   ::= '(' + lower + ',' + upper + ')'</li>
	 * </ul> 
	 * 
	 * @throws IOException - if an I/O Error occours
	 */
	private void buildBounds() throws IOException {
		parser.nextToken();
		//System.out.println("Building token: " + parser.ttype);
		
		if ((char) parser.ttype == ',') {
			parser.pushBack();
			return;
		}
		
		if (parser.ttype == StreamTokenizer.TT_EOF) { // We only have a Type eg: R
			//System.out.println("End of stream");
			return;
		}
		else {
			accept('(');
			parser.nextToken();
			this.lower = parser.nval;
			//System.out.println("lower: " + lower);
			parser.nextToken();
			accept(',');
			parser.nextToken();
			this.upper = parser.nval;
			//System.out.println("upper: " + upper);
			parser.nextToken();
			accept(')');
		}
	}
		
	
	/**
	 * Actually build the <tt>Type</tt> object and add it to the collection of built
	 * objects ready for use by CiLib or CiClops
	 * 
	 * @throws IOException - if an I/O Error occours
	 */
	private void buildTypeObject() throws IOException {
		//System.out.println("Building object: " + objectType + "(" + lower + "," + upper +")");
		//System.out.println("size before: " + this.representation.size());
		
		String type = (String) objectType;
		Class<?> c = null;
		TypeCreator domain = null;
		
		try {
			c = Class.forName("net.sourceforge.cilib.type.creator." + type);
			//System.out.println("constructing class: " + c.getName());	
			domain = (TypeCreator) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Type builtType = null;		
		
		if (lower == -Double.MAX_VALUE || upper == Double.MAX_VALUE) { // No Bounds
			builtType = domain.create();
		}
		else { // Use Bounds
			builtType = domain.create(lower,upper);
			
			// Reset the variables :)
			this.lower = -Double.MAX_VALUE;
			this.upper = Double.MAX_VALUE;
		}
		
		this.representation.add(builtType);
		
		//System.out.println("size after: " + this.representation.size());
	}
	
	
	/**
	 * Method corresponds to the production rules:
	 *
	 * <ul>
	 * <li>Repeated ::= []</li>
	 * <li>Repeated ::= ',' + Domain</li>
	 * </ul>
	 * 
	 * @throws IOException - if an I/O Error occours
	 */
	private void buildRepeat() throws IOException {
		parser.nextToken();
		
		if ((char) parser.ttype == ']') {
			parser.pushBack();
			return;
		}
		
		if (parser.ttype == StreamTokenizer.TT_EOF) { // End of stream
			//System.out.println("End of expandedString");
		}
		else {
			accept(',');
			//this.buildDomain();
			this.buildMatrix();
		}
	}
	
	
	/**
	 * Utility method to remove tokens from the input secquence.
	 * 
	 * @param c The target token expected.
	 */
	private void accept(char c) {
		if (((char) parser.ttype) == c)
			return;
		else
			throw new RuntimeException("Parser error: " + c + " was expected but received: " + (char) parser.ttype);
	}
	
}
