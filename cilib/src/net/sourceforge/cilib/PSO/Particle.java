/*
 * Particle.java
 *
 * Created on January 15, 2003, 8:27 PM
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

package net.sourceforge.cilib.PSO;

import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;

/**
 *
 * @author  espeer
 */
public interface Particle extends Cloneable {

    
    public Object clone() throws CloneNotSupportedException;
    
    public int getId();
    
    public void setFitness(Fitness fitness);

    public Fitness getFitness();
    
    public Fitness getBestFitness();

    public int getDimension();
   
    public void initialise(OptimisationProblem problem, Initialiser i);

    public double[] getPosition();
  
    public double[] getBestPosition();
    
    public double[] getVelocity();

    public void setNeighbourhoodBest(Particle particle);
    
    public Particle getNeighbourhoodBest();

    public void move();
    
    public void updateVelocity(VelocityUpdate vu);
    
    public Particle getDecorator(Class decorator);
    
    public static byte _ciclops_exclude_neighbourhoodBest = 1;
    public static byte _ciclops_exclude_fitness = 1;
}
