/*
 * ContentValidator.java
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

import net.sourceforge.cilib.Domain.Component;

/**
 * @author espeer
 */
public class ContentValidator implements Validator {

    public ContentValidator(int element, Validator constraint) {
        this.element = element;
        this.constraint = constraint;
    }

    public ContentValidator(Validator constraint) {
        this.constraint = constraint;
        element = -1;
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Domain.Constraint.Constraint#check(net.sourceforge.cilib.Domain.Component)
     */
    public void validate(Component component) {
        if (element == -1) {
            for (int i = 0; i < component.getDimension(); ++i) {
                constraint.validate(component.getComponent(i));
            }
        }
        else {
            constraint.validate(component.getComponent(element));
        }
    }

    private Validator constraint;
    private int element; 
}
