/*
 * GaussianVelocityUpdate.java
 * 
 * Created on Jul 26, 2004
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
package net.sourceforge.cilib.PSO;

import java.util.Random;

import net.sourceforge.cilib.Random.KnuthSubtractive;
import net.sourceforge.cilib.Type.Types.Vector;

/**
 *
 * TODO This class is comepletely broken - don't use it yet
 *
 * @author espeer
 */
public class GaussianVelocityUpdate implements VelocityUpdate {

	public GaussianVelocityUpdate() {
		randomiser = new KnuthSubtractive();
	}
	
	public void updateVelocity(Particle particle) {
        //double[] position = particle.getPosition();
        //double[] nBestPosition = particle.getNeighbourhoodBest().getBestPosition();
		Vector position = particle.getPosition();
		Vector nBestPosition = particle.getNeighbourhoodBest().getBestPosition();

        /*for (int i = 0; i < particle.getDimension(); ++i) {
        	double tmp = position[i] - nBestPosition[i];
        	position[i] = randomiser.nextGaussian() *  tmp * tmp * (position[i] + nBestPosition[i]) / 2;
        }*/
		for (int i = 0; i < particle.getDimension(); ++i) {
        	double tmp = position.getReal(i) - nBestPosition.getReal(i);
        	position.setReal(i, randomiser.nextGaussian() *  tmp * tmp * (position.getReal(i) + nBestPosition.getReal(i)) / 2);
        }
	}
	
	public void setRandomizer(Random randomiser) {
		this.randomiser = randomiser;
	}
	
	public Random getRandomiser() {
		return randomiser;
	}

	private Random randomiser;
	
}
