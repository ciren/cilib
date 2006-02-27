/*
 * BestParticlePosition.java
 *
 * Created on August 24, 2004, 12:16 AM
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

package net.sourceforge.cilib.measurement;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * Print the position of the best particle in the swarm
 * 
 * @author Gary Pampara
 */
public class BestParticlePosition implements Measurement { 
	
	public BestParticlePosition() {
    }

    public String getDomain() {
    	return "T";
    }
    
    public Type getValue() {
    	OptimisationAlgorithm alg = (OptimisationAlgorithm)Algorithm.get();
        Vector solution = (Vector) alg.getBestSolution().getPosition();
        
        /*StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < solution.getDimension()-1; i++) {
        	buffer.append(solution.getReal(i) + ",");
        }
        
        buffer.append(solution.getReal(solution.getDimension()-1));*/
        
        StringType t = new StringType();
        //t.setString(buffer.toString());
        t.setString(solution.toString());
        
        return t;    	
    }

}
