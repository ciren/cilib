/*
 * Domain.java
 *
 * Created on January 11, 2003, 1:12 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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

import java.io.*;

/**
 *
 * @author  espeer
 */
public class Domain implements Serializable {
    
    public Domain() {
        lowerBound = - Double.MAX_VALUE;
        upperBound = Double.MAX_VALUE;
    }
    
    public Domain(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    public double getLowerBound() {
        return lowerBound;
    }
    
    public double getUpperBound() {
        return upperBound;
    }
    
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    private double lowerBound;
    private double upperBound;
}
