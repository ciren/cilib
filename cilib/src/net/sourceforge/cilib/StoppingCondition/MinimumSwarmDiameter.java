/*
 * MinimumSwarmDiameter.java
 *
 * Created on January 26, 2003, 2:40 PM
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

package net.sourceforge.cilib.StoppingCondition;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.PSO.PSO;

/**
 *
 * @author  espeer
 */
public class MinimumSwarmDiameter implements StoppingCondition {
    
    /** Creates a new instance of MinimumSwarmDiameterIndicator */
    public MinimumSwarmDiameter() {
        minimumSwarmDiameter = 0.0001;
    }
    
    public MinimumSwarmDiameter(double minimumSwarmDiameter) {
        this.minimumSwarmDiameter = minimumSwarmDiameter;
    }
    
    public void setDiameter(double minimumSwarmDiameter) {
        this.minimumSwarmDiameter = minimumSwarmDiameter;
    }
    
    public double getDiameter() {
    	return minimumSwarmDiameter;
    }
    
    public double getPercentageCompleted() {
        if (algorithm.getSwarmDiameter() <= minimumSwarmDiameter) {
            return 1;
        }
        return minimumSwarmDiameter / algorithm.getSwarmDiameter();
    }
    
    public boolean isCompleted() {
        return algorithm.getSwarmDiameter() <= minimumSwarmDiameter;
    }
    
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = (PSO) algorithm;
    }
    
    double minimumSwarmDiameter;
    PSO algorithm;
}
