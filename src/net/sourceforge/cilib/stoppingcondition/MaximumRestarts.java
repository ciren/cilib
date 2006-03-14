/*
 * MaximumRestarts.java
 *
 * Created on February 4, 2003, 10:49 AM
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
import net.sourceforge.cilib.algorithm.MultistartOptimisationAlgorithm;

/**
 *
 * @author  Edwin Peer
 */
public class MaximumRestarts implements StoppingCondition {
    
    /** Creates a new instance of MaximumRestarts */
    public MaximumRestarts() {
        maximumRestarts = 10;
    }
    
    public MaximumRestarts(int maximumRestarts) {
        this.maximumRestarts = maximumRestarts;
    }
    
    public int getRestarts() {
    	return maximumRestarts;
    }
    
    public void setRestarts(int maximumRestarts) {
        this.maximumRestarts = maximumRestarts;
    }
    
    public double getPercentageCompleted() {
        return ((double) algorithm.getRestarts()) / ((double) maximumRestarts + 1);
    }    
    
    public boolean isCompleted() {
        return algorithm.getRestarts() > maximumRestarts;
    }
    
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = (MultistartOptimisationAlgorithm) algorithm;
    }    
    
    private int maximumRestarts;
    private MultistartOptimisationAlgorithm algorithm;
}
