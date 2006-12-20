/*
 * ParticlaAdapter.java
 * 
 * Created on Aug 3, 2005
 *
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
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
package net.sourceforge.cilib.ec.particleadaptor;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * @author otter
 */
@Deprecated
public class ParticleAdapter extends Individual {
    
    /**
     * Constructor
     * Receives the particle 
     * How must it know now if it wants to do stuff with the velocity or position???
     */
    public ParticleAdapter(Particle par) {
        //set the stuff
        this.id = "Particle"+par.getId();
        this.dimension = par.getDimension();
        
        //TODO: How do you build up the genetic structure? Depends on how you want to use the particle as an individual.
        this.genes = par.getPosition(); 
        
        //is it relevant to intitialize the fitness? Yes what if you do fitness based mutation...
        this.fitness = par.getFitness(); 
    }
}
