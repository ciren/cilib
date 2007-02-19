/*
 * MaximumIterations.java
 *
 * Created on January 20, 2003, 10:54 AM
 *
 * 
 * Copyright (C) 2003 - 2006 
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

package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;

/**
 *
 * @author  Edwin Peer
 */
public class MaximumIterations implements StoppingCondition {
    
    /** Creates a new instance of MaximumIterationIndicator */
    public MaximumIterations() {
        maximumIterations = 10000;
    }
    
    public MaximumIterations(MaximumIterations copy) {
    	this.maximumIterations = copy.maximumIterations;
    	this.algorithm = copy.algorithm;
    }
    
    public MaximumIterations clone() {
    	return new MaximumIterations(this);
    }
    
    public MaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }
    
    public int getIterations() {
    	return maximumIterations;
    }
    
    public double getPercentageCompleted() {
        return ((double) algorithm.getIterations()) / ((double) maximumIterations);
    }
    
    public boolean isCompleted() {
        return algorithm.getIterations() >= maximumIterations;
    }
    
    public void setIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }
    
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    private Algorithm algorithm;
    private int maximumIterations;
}
