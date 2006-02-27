/*
 * Domain.java
 *
 * Created on January 11, 2003, 1:12 PM
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

package net.sourceforge.cilib.Problem;

import java.io.Serializable;

/**
 * This represents a lower and upper bound for a search domain.
 *
 * @author  espeer
 */
public class Domain implements Serializable {
    
    /**
     * Creates a new instance of <code>Domain</code> that is unbounded
     */
    public Domain() {
        lowerBound = - Double.MAX_VALUE;
        upperBound = Double.MAX_VALUE;
    }
    
    /**
     * Creates a new instance of <code>Domin</code> with the given bounds.
     *
     * @param lowerBound The lower bound.
     * @param upperBound The upper bound.
     */
    public Domain(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    /**
     * Accessor for the lower bound of the search domain.
     *
     * @return The lower bound.
     */
    public double getLowerBound() {
        return lowerBound;
    }
    
    /**
     * Accessor for the upper bound of the search domain.
     *
     * @return The upper bound.
     */
    public double getUpperBound() {
        return upperBound;
    }
    
    /**
     * Sets the lower search bound.
     *
     * @param lowerBound The lower bound.
     */
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    /**
     * Sets the upper search bound.
     *
     * @param upperBound The upper bound.
     */
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    private double lowerBound;
    private double upperBound;
}
