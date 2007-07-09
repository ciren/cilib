/*
 * DominatesStrategy.java
 * 
 * Created on Apr 1, 2006
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
package net.sourceforge.cilib.moo.archive;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Andries Engelbrecht
 */
public class DominatesStrategy implements LocalGuideStrategy {

	private Vector localGuide = null;
	
	/* Must newLocalGuide be cloned??*/
	public void setLocalGuide(Vector newLocalGuide) {
		this.localGuide = newLocalGuide;
	}
	
	public Vector getLocalGuide() {
		return this.localGuide;
	}
	
	/*rather Entity than Particle*/
	public void updateLocalGuide(Particle particle) {
		/*only of position dominates personalBestPosition, then 
		 * the new personal best position becomes the current position
		 */
		if (particle.getFitness().compareTo(particle.getBestFitness()) == 0)
			 this.localGuide = (Vector) particle.getPosition().clone();
	}

}
