/*
 * Diameter.java
 * 
 * Created on Jul 24, 2004
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

package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * Calculates the swarm diameter as the maximum euclidean distance between any
 * two particles. 
 * 
 * 
 * 
 * @author  Andries Engelbrecht
 */
public class Diameter implements Measurement {
    
    /** Creates a new instance of SwarmDiameter */
    public Diameter() {
    }
    
    public String getDomain() {
    	return "R";
    }
    
    public Type getValue() {
    	/*Delete the below only if PSO.PSO.getDiameter calculates the actual diameter*/
    	/*PSO pso = (PSO) Algorithm.get();
    	double maxDistance = 0.0;
    	
    	Iterator k1 = pso.getTopology().iterator();
        while (k1.hasNext()) {
            Particle p1 = (Particle) k1.next();
        	Vector position1 = (Vector) p1.getPosition();
           	
        	Iterator k2 = pso.getTopology().iterator();
        	while (k2.hasNext()) {
        		Particle p2 = (Particle) k2.next();
        		Vector position2 = (Vector) p2.getPosition();
        		DistanceMeasure distance = new EuclideanDistanceMeasure();
        		double actualDistance = distance.distance(position1,position2);
        		if (actualDistance > maxDistance)
        			maxDistance = actualDistance;
        	}
        }*/
    	
    	PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
        double diameter = algorithm.getDiameter();
        
    	return new Real(diameter);
    }
    
}
