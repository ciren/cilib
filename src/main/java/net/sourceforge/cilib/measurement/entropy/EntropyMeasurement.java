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
package net.sourceforge.cilib.measurement.entropy;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * An interface to allow entropy measurements to be performed on an algorithm.
 * 
 * @author Bennie Leonard
 */
public abstract class EntropyMeasurement implements Measurement<TypeList> {
    protected int intervals; //the number of intervals over which entropy is measured

    @Override
    public abstract TypeList getValue(Algorithm algorithm);

    @Override
    public String getDomain() {
        return "T";
    }

    /**
     * Set the number of intervals over which entropy is measured.
     *
     * @param k The number of intervals to set.
     */
    public void setIntervals(int k) {
        intervals = k;
    }

    /**
     * Get the number of intervals over which entropy is measured.
     *
     * @return The number of intervals over which entropy is measured.
     */
    public int getIntervals() {
        return intervals;
    }

}
