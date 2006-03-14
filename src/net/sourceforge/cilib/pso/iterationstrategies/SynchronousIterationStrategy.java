/*
 * SyncronousIterationStrategy.java
 *
 * Created on Oct 14, 2005
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
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import java.util.Iterator;

import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * @author Gary Pampara
 */
public class SynchronousIterationStrategy implements IterationStrategy {

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.IterationStrategy#performIteration(net.sourceforge.cilib.PSO.PSO)
	 */
	public void performIteration(PSO pso) {
	   for (Iterator<Particle> i = pso.getTopology().iterator(); i.hasNext(); ) {
            Particle current = i.next();
            current.setFitness(pso.getOptimisationProblem().getFitness(current.getPosition(), true));
            
            for (Iterator<Particle> j = pso.getTopology().neighbourhood(i); j.hasNext(); ) {
                Particle other = j.next();
                if (current.getSocialBestFitness().compareTo( other.getNeighbourhoodBest().getSocialBestFitness()) > 0) {
                    other.setNeighbourhoodBest(current); // TODO: neighbourhood visitor?
                }
            }
       }

       for (Iterator<Particle> i = pso.getTopology().iterator(); i.hasNext(); ) {
           Particle current = i.next();
           //current.updateVelocity(pso.getVelocityUpdate());      // TODO: replace with visitor (will simplify particle interface)
           current.updateVelocity();
           current.updatePosition();                                        // TODO: replace with visitor (will simplify particle interface)
       }
	}

}
