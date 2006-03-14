/*
 * DomainParser.java
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

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * Implementation of the Facade pattern to remove the complexity of actually performing
 * the required translations to get the Domain String parsed and processed into a usable
 * represenation.
 * 
 * @author Gary Pampara
 */
public class DomainParser {

	private DomainBuilder builder;
	private DomainValidator validator;
	
	private static DomainParser instance;
	
	
	/**
	 * Private constructor used in the singleton design pattern.
	 * 
	 * Look at {@see DomainParser#getInstance()}
	 */
	private DomainParser() {
		builder = new DomainBuilder();
		validator = new DomainValidator();
	}
	
	
	/**
	 * Get the instance of the Singleton object. This object is the <tt>DomainParser</tt>
	 * 
	 * @return The instance of the <code>DomainParser</code>
	 */
	public static DomainParser getInstance() {
		if (instance == null)
			instance = new DomainParser();
		
		return instance;
	}
	
	/**
	 * Build the representation and expand the domain string if the expanded domain string
	 * does not exist.
	 * 
	 * The domain is first tested for validity before being processed into the constructed
	 * representation required by the rest of CILib.
	 * 
	 * @param domain The domain string to be parsed
	 */
	public boolean parse(String domain) {
			
		if (isValid(domain)) {
			String expandedRepresenation = expandDomainString(domain);
		
			builder.build(expandedRepresenation);
			//System.out.println(builder.getBuiltRepresenation());
			
			return true;
		}
		else
			throw new RuntimeException("Domain string is not valid, please correct domain string");
	}
	

	/**
	 * Build the expanded string representation of the domain string.
	 * 
	 * @param domain The string representing the domain to be built
	 * @return A <code>String</code> representing the expanded represenation
	 */
	public String expandDomainString(String domain) {
		String result = validator.expandString(domain);
		//System.out.println("result domain: " + result);
	
		return result;
	}

	
	/**
	 * Test the domain string to see if it is valid. The domain string is passed
	 * onto a {@see DomainValidator} object which perfroms the actual validation.
	 * 
	 * @param domain The domain string to be validated
	 * 
	 * @return <tt>true</tt> - if the domain string is valid;
	 *         <tt>false</tt> - if the domain string is not valid
	 */
	public boolean isValid(String domain) {
		return validator.validate(domain);
	}
	
	
	/**
	 * Accessor method to get the constructed representation. The constructed
	 * representation is constructed by {@see DomainBuilder}.
	 * 
	 * @return The constructed domain string as a <tt>Type</tt>.
	 */
	public Type getBuiltRepresentation() {
		return builder.getBuiltRepresenation();
	}



	/**
	 * Test if the solution provided is within the search space of the problem. If
	 * the provided solution is not in the search space, then the solution is not
	 * inside and hence is invalid.
	 *  
	 * @param solution The provided solution to test
	 * 
	 * @return <code>true</code> if the solution is contained within the search space of the problem; 
	 *         <code>false</code> if the solution is not contained within the search space of the problem
	 */
	public boolean isInside(Object solution) {
		boolean result = true;
		
		if (solution instanceof Vector) {
			Vector vector = (Vector) solution;
		
			for (int i = 0; i < vector.getDimension(); i++) {
				Type t = vector.get(i);
			
				if (t instanceof Vector) {
					result = isInside(t);
				}
				else if (t instanceof Numeric) {
					Numeric r = (Numeric) t;
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
		}
		
		return result;
	}
}
