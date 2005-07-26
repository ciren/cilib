/*
 * QuantitativeBoundValidator.java
 * 
 * Created on Apr 17, 2004
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
package net.sourceforge.cilib.Domain.Validator;

import net.sourceforge.cilib.Domain.DomainComponent;
import net.sourceforge.cilib.Domain.Quantitative;

/**
 * @author espeer
 */
public class QuantitativeBoundValidator implements Validator {

    public QuantitativeBoundValidator(Number lowerBound, Number upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        quantitativeConstraint = new TypeValidator(Quantitative.class);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Constraint.Constraint#check(net.sourceforge.cilib.Domain.Component)
     */
    public void validate(DomainComponent component) {
        quantitativeConstraint.validate(component);
        Quantitative tmp = (Quantitative) component;
        if (tmp.getLowerBound().doubleValue() < lowerBound.doubleValue()) {
            throw new ValidationFailedException("Expected lower bound of at least " + lowerBound.toString());
        } 
        if (tmp.getUpperBound().doubleValue() > upperBound.doubleValue()) {
            throw new ValidationFailedException("Expected upper bound of at most " + upperBound.toString());
        }
    }

    private Number lowerBound;
    private Number upperBound;
    private Validator quantitativeConstraint;
}
