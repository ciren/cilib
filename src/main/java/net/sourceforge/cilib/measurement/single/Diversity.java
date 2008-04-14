/*
 * Diversity.java
 *
 * Copyright (C) 2003 - 2008
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

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Function to calculate the diversity of a swarm according to the definition of Krink et al
 * </p><p>
 * References:
 * </p><p><ul><li>
 * T. Krink, J.S. Vesterstrom, J. Riget, "Particle Swarm Optimisation with Spatial Particle Extension",
 * Proceedings of the Fourth Congress on Evolutionary Computation, 
 * Volume 2, pages 1474--1479, 2002.
 * </li><li>
 * AP Engelbrecht, "Fundamentals of Computational Swarm Intelligence",
 * Wiley & Sons, pages 124--125, 2005.
 * </li></ul></p>
 * @author Andries Engelbrecht
 */

public class Diversity implements Measurement {
	private static final long serialVersionUID = -6536136932133521018L;

	public Diversity() {
	}
	
	public Diversity(Diversity copy) {
		
	}
	
	public Diversity getClone() {
		return new Diversity(this);
	}

	public String getDomain() {
		return "R";
	}

	public Type getValue() {
		
		PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
		int numberOfEntities = algorithm.getPopulationSize();
				        
        Iterator<? extends Entity> k = algorithm.getTopology().iterator();
        Entity entity = k.next();
        Vector averageParticlePosition = (Vector) entity.getContents().getClone();
        while (k.hasNext()) {
        	entity = k.next();
        	Vector v = (Vector) entity.getContents();
        	for (int j = 0; j < averageParticlePosition.getDimension(); ++j)
        	   averageParticlePosition.setReal(j, averageParticlePosition.getReal(j)+v.getReal(j));
        }
        for (int j = 0; j < averageParticlePosition.getDimension(); ++j)
           averageParticlePosition.setReal(j, averageParticlePosition.getReal(j)/numberOfEntities);
        //System.out.println(averageParticlePosition);
		
		Iterator<? extends Entity> i = algorithm.getTopology().iterator();
		double particleSum = 0.0;
		while (i.hasNext()) {
			entity = i.next();
			
			double dimensionSum = 0.0;
			Vector v = (Vector) entity.getContents();
			for (int j = 0; j < entity.getDimension(); ++j) {
				dimensionSum += (v.getReal(j)-averageParticlePosition.getReal(j))*(v.getReal(j)-averageParticlePosition.getReal(j));
				
			}
			particleSum += Math.sqrt(dimensionSum);
		}
		
    	return new Real(particleSum/numberOfEntities);
	}

}
