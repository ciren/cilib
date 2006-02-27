/*
 * Measurement.java
 *
 * Created on February 4, 2003, 4:04 PM
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

package net.sourceforge.cilib.Measurement;

import java.io.*;
import java.lang.*;
import net.sourceforge.cilib.Algorithm.*;

/**
 * All measurements must implement this interface. The measurment must return
 * the value of what it is measuring given the algorithm that it is measuring.
 *
 * @author  espeer
 */
public interface Measurement extends Serializable {
    /**
     * Returns the value of this measurement on the given algorithm
     * 
     * @param The algorithm to take the measurement from.
     */
    public Number getValue(Algorithm algorithm);
}
