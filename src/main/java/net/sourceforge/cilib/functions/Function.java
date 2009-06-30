/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.functions;

import java.io.Serializable;

import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * All functions should inherit from <code>Function</code>.
 * @author Edwin Peer
 * @author Gary Pampara
 */
public abstract class Function implements Cloneable, Serializable {
    private static final long serialVersionUID = -4843291761555348251L;

    private DomainRegistry domainRegistry;
    private DomainRegistry behavioralDomainRegistry;

    /**
     * Create a new instance of {@linkplain Function}.
     */
    public Function() {
        domainRegistry = new StringBasedDomainRegistry();
        behavioralDomainRegistry = new StringBasedDomainRegistry();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public Function(Function copy) {
        domainRegistry = copy.domainRegistry.getClone();
        behavioralDomainRegistry = copy.behavioralDomainRegistry.getClone();
    }

    /**
     * @return The dimension of the function.
     */
    public int getDimension() {
        return this.getDomainRegistry().getDimension();
    }

    /**
     * Accessor for the domain of the function. See {@link net.sourceforge.cilib.Domain.Component}.
     * @return The function domain.
     */
    public DomainRegistry getDomainRegistry() {
        return domainRegistry;
    }

    /**
     * @return Returns the behaviouralDomainRegistry.
     */
    public DomainRegistry getBehavioralDomainRegistry() {
        return behavioralDomainRegistry;
    }

    /**
     * @param behavioralDomainRegistry The behaviouralDomainRegistry to set.
     */
    public void setBehaviouralDomainRegistry(StringBasedDomainRegistry behavioralDomainRegistry) {
        throw new UnsupportedOperationException("You are not allowed to set the BehaviroalDomainRegistry!");
    }

    /**
     * @return The domain {@linkplain String}.
     */
    public String getDomain() {
        return domainRegistry.getDomainString();
    }

    /**
     * Sets the domain of the function.
     * @param representation the string representation for the function domain.
     */
    public void setDomain(String representation) {
        this.domainRegistry.setDomainString(representation);
    }

    /**
     * Set the behavioural domain of the {@linkplain Function}.
     * @param behavioralDomain The value to set.
     */
    public void setBehavioralDomain(String behavioralDomain) {
        this.behavioralDomainRegistry.setDomainString(behavioralDomain);
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
     * @return The result of the evaluation.
     */
    public abstract Double evaluate(Type x);

}
