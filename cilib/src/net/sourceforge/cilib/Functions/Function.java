/*
 * Function.java
 *
 * Created on January 11, 2003, 1:36 PM
 *
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

package net.sourceforge.cilib.Functions;

import java.io.Serializable;

import net.sourceforge.cilib.Domain.ComponentFactory;
import net.sourceforge.cilib.Domain.DomainComponent;
import net.sourceforge.cilib.Domain.Vector;
import net.sourceforge.cilib.Domain.Validator.BoundedDimensionValidator;
import net.sourceforge.cilib.Domain.Validator.CompositeValidator;
import net.sourceforge.cilib.Domain.Validator.TypeValidator;

/**
 * All functions should inherit from <code>Function</code>
 *
 * @author  espeer
 */
public abstract class Function implements Serializable {

    public Function() {
        constraint = new CompositeValidator();
        constraint.add(new BoundedDimensionValidator());
        constraint.add(new TypeValidator(Vector.class));
    }
    
    public int getDimension() {
        return domain.getDimension();
    }

    /**
     * Accessor for the domain of the function.
     * 
     * See {@link net.sourceforge.cilib.Domain.Component}.
     *
     * @returns The function domain.
     */
    public String getDomain() {
        return domain.getRepresentation();
    }

    /**
     * Sets the domain of the function. 
     *
     * See {@link net.sourceforge.cilib.Domain.Component}.
     * 
     * @param representation the string representation for the function domain. 
     */
    public void setDomain(String representation) {
        DomainComponent tmp = ComponentFactory.instance().newComponent(representation);
        constraint.validate(tmp);
        domain = tmp;
    }

    /**
     * Returns the domain component structure representing the function domain.
     * 
     * @return the domain component.
     */
    public DomainComponent getDomainComponent() {
        return domain;
    }
    
    /**
     * Accessor for the function minimum. This is the minimum value of the function
     * in the given domain.
     *
     * @return The minimum function value.
     */
    public abstract Object getMinimum();

    /**
     * Accessor for the function maximum. This is the maximum value of the function in the
     * given domain.
     * 
     * @return The maximum of the function. 
     */
    public abstract Object getMaximum();

    /**
     * Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x the position
     */
    public abstract Comparable evaluate(Object x);
    
    private DomainComponent domain;
    protected CompositeValidator constraint;
}
