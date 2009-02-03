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

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Implementation of the Facade pattern to remove the complexity of actually performing
 * the required translations to get the Domain String parsed and processed into a usable
 * represenation.
 * 
 * @author Gary Pampara
 */
public final class DomainParser {

	private DomainBuilder builder;
	private DomainValidator validator;
	
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
		return DomainParserHolder.instance;
	}

	private static class DomainParserHolder {
		public static DomainParser instance = new DomainParser();
	}
	
	/**
	 * Build the representation and expand the domain string if the expanded domain string
	 * does not exist.
	 * 
	 * The domain is first tested for validity before being processed into the constructed
	 * representation required by the rest of CILib.
	 * 
	 * @param domain The domain string to be parsed
	 * @return <code>true</code> if the parsing is successful.
	 * @throws RuntimeException If the domain string is not valid.
	 */
	public boolean parse(String domain) {
			
		if (isValid(domain)) {
			String expandedRepresenation = expandDomainString(domain);
			builder.build(expandedRepresenation);
						
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
	public StructuredType getBuiltRepresentation() {
		return builder.getBuiltRepresenation();
	}

}
