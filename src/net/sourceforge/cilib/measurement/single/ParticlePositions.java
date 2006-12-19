/*
 * ParticlePositions.java
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

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author Edwin Peer
 */
public class ParticlePositions implements Measurement {
	
	public ParticlePositions() {
	}
	
	public ParticlePositions(ParticlePositions copy) {
	}
	
	public ParticlePositions clone() {
		return new ParticlePositions(this);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Measurement.Measurement#getDomain()
	 */
	public String getDomain() {
		return "T";
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Measurement.Measurement#getValue()
	 */
	public Type getValue() {
		StringBuffer tmp = new StringBuffer();
		
		PSO pso = (PSO) Algorithm.get();
		//Iterator i = pso.getTopology().particles();
		Iterator i = pso.getTopology().iterator();
		while (i.hasNext()) {
			Particle particle = (Particle) i.next();
			tmp.append("\nParticle: ");
			tmp.append(particle.getId());
			tmp.append(" Current Fitness: ");
			tmp.append(particle.getFitness().getValue());
			tmp.append(" Best Fitness: ");
			tmp.append(particle.getBestFitness().getValue());
			tmp.append(" Position: ");
			
			Vector v = (Vector) particle.getPosition();
			for (int j = 0; j < particle.getDimension(); ++j) {
				tmp.append(v.getReal(j));
				tmp.append(" ");
			}
		}
		
		StringType t = new StringType();
		t.setString(tmp.toString());
		
		//return tmp.toString();
		return t;
	}

}
