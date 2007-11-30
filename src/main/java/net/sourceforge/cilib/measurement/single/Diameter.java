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
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
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
    private static final long serialVersionUID = 5136996282460480831L;

	/** Creates a new instance of SwarmDiameter */
    public Diameter() {
    }
    
    public Diameter(Diameter copy) {
    	
    }
    
    public Diameter getClone() {
    	return new Diameter(this);
    }
    
    public String getDomain() {
    	return "R";
    }
    
    @SuppressWarnings("unchecked")
	public Type getValue() {
    	
    	PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
    	Topology<? extends Entity> topology = algorithm.getTopology();
    	
    	DiameterVisitor visitor = new DiameterVisitor();
        topology.accept(visitor);
        
    	return new Real(visitor.getResult());
    }
    
}
