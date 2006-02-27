/*
 * LinearDecreasingInertia.java
 * 
 * Created on Mar 2, 2004
 * 
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.PSO;

import net.sourceforge.cilib.Algorithm.Algorithm;

/**
 * @author espeer
 */
public class LinearDecreasingInertia implements VelocityComponent {

    public LinearDecreasingInertia() {
        minimumInertia = 0.2;
        maximumInertia = 1.0;
    }
    
    public double get() {
        return maximumInertia - (maximumInertia - minimumInertia) * Algorithm.get().getPercentageComplete();
    }

    /**
     * @return Returns the maximumInertia.
     */
    public double getMaximumInertia() {
        return maximumInertia;
    }

    /**
     * @param maximumInertia The maximumInertia to set.
     */
    public void setMaximumInertia(double maximumInertia) {
        this.maximumInertia = maximumInertia;
    }

    /**
     * @return Returns the minimumInertia.
     */
    public double getMinimumInertia() {
        return minimumInertia;
    }

    /**
     * @param minimumInertia The minimumInertia to set.
     */
    public void setMinimumInertia(double minimumInertia) {
        this.minimumInertia = minimumInertia;
    }

    private double minimumInertia;
    private double maximumInertia;

}
