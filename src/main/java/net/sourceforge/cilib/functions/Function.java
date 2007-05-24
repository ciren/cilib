/*
 * Function.java
 *
 * Created on January 11, 2003, 1:36 PM
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

package net.sourceforge.cilib.functions;

import java.io.Serializable;

import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 * All functions should inherit from <code>Function</code>
 * @author Edwin Peer
 * @author Gary Pampara
 */
public abstract class Function implements Serializable {
	private DomainRegistry domainRegistry;
	private DomainRegistry behavioralDomainRegistry;

	/**
	 * 
	 *
	 */
	public Function() {
		domainRegistry = new DomainRegistry();
		behavioralDomainRegistry = new DomainRegistry();
	}

	public Function(Function copy) {
		domainRegistry = copy.domainRegistry.clone();
		behavioralDomainRegistry = copy.behavioralDomainRegistry.clone();
	}

	/**
	 * @return
	 */
	public int getDimension() {
		return this.getDomainRegistry().getDimension();
	}

	/**
	 * Accessor for the domain of the function. See {@link net.sourceforge.cilib.Domain.Component}.
	 * @returns The function domain.
	 */
	public DomainRegistry getDomainRegistry() {
		return domainRegistry;
	}

	/**
	 * Method added for CiClops compatibility.... not used and do not remove
	 * @param d
	 */
	public void setDomainRegistry(DomainRegistry d) {
		throw new UnsupportedOperationException("You are not allowed to set the DomainRegistry!");
	}

	/**
	 * @return Returns the behaviouralDomainRegistry.
	 */
	public DomainRegistry getBehavioralDomainRegistry() {
		return behavioralDomainRegistry;
	}

	/**
	 * @param behaviouralDomainRegistry The behaviouralDomainRegistry to set.
	 */
	public void setBehaviouralDomainRegistry(DomainRegistry behavioralDomainRegistry) {
		throw new UnsupportedOperationException("You are not allowed to set the BehaviroalDomainRegistry!");
	}

	/**
	 * @return
	 */
	public String getDomain() {
		return domainRegistry.getDomainString();
	}

	/**
	 * Sets the domain of the function. See {@link net.sourceforge.cilib.Domain.Component}.
	 * @param representation the string representation for the function domain.
	 */
	public void setDomain(String representation) {
		DomainParser parser = DomainParser.getInstance();
		parser.parse(representation);

		domainRegistry.setDomainString(representation);
		domainRegistry.setExpandedRepresentation(parser.expandDomainString(representation));
		domainRegistry.setBuiltRepresenation(parser.getBuiltRepresentation());
	}

	/**
	 * @param behavioralDomain
	 */
	public void setBehavioralDomain(String behavioralDomain) {
		DomainParser parser = DomainParser.getInstance();
		parser.parse(behavioralDomain);

		behavioralDomainRegistry.setDomainString(behavioralDomain);
		behavioralDomainRegistry.setExpandedRepresentation(parser.expandDomainString(behavioralDomain));
		behavioralDomainRegistry.setBuiltRepresenation(parser.getBuiltRepresentation());
	}

	/**
	 * Accessor for the function minimum. This is the minimum value of the function in the given
	 * domain.
	 * @return The minimum function value.
	 */
	public abstract Object getMinimum();

	/**
	 * Accessor for the function maximum. This is the maximum value of the function in the given
	 * domain.
	 * @return The maximum of the function.
	 */
	public abstract Object getMaximum();

	/**
	 * Each function must provide an implementation which returns the function value at the given
	 * position. The length of the position array should be the same as the function dimension.
	 * @param x the position
	 */
	public abstract Double evaluate(Object x);
}
