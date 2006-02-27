/*
 * ProgressIndicator.java
 *
 * Created on January 17, 2003, 5:06 PM
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

package net.sourceforge.cilib.Algorithm;

import java.io.*;

/**
 * <p>
 * A class that implements this interface can be used to measure the progress
 * of an algorithm. Primarily, progress indicators are used to determine the
 * stopping criteria for an {@link net.sourceforge.cilib.Algorithm.Algorithm}. Progress indicators are applied  
 * to algorithms using {@link Algorithm#addProgressIndicator(ProgressIndicator)}.
 * The algorithm terminates as soon as any progress indicator returns 100% (a value >= 1.0).
 * </p>
 * <p> 
 * Progress indicators are also useful for implementing graphical progress bars and 
 * varying inertia weights etc.
 * </p>
 *
 * @author  espeer
 */
public interface ProgressIndicator extends Serializable {
    /**
     * Determines the percentage complete for the algorithm set by {@link #setAlgorithm(Algorithm)}.
     * 
     * @returns The percentage completed as a fraction (0 <= i <= 1.0). 
     */
    public double getPercentageCompleted();
    
    /**
     * Sets the algorithm that this progress indicator should be applied to. Called 
     * by {@link Algorithm#addProgressIndicator(ProgressIndicator)}. This ensures that any
     * down casting necessary is done only once, when the progress indicator is added
     * to an alogorithm (as apposed to after each iteration).
     *
     * @param algorithm The applicable {@link Algorithm}.
     */
    public void setAlgorithm(Algorithm algorithm);
}
