/*
 * GCPSO.java
 *
 * Created on January 23, 2003, 5:21 PM
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


/**
 * <p>
 * An implementation of the Guaranteed Convergence PSO algorithm.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * F. van den Bergh and A. Engelbrecht, "A new locally convergent particle swarm optimizer,"
 * in Proceedings of IEEE Conference on Systems, Man and Cybernetics,
 * (Hammamet, Tunisia), Oct. 2002.
 * </li><li>
 * F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 * PhD thesis, Department of Computer Science, 
 * University of Pretoria, South Africa, 2002.
 * </li></ul></p>
 *
 * @author  espeer
 */
public class GCPSO extends PSO {
    
    /** Creates a new instance of GCPSO */
    public GCPSO() {
        super();
        super.setVelocityUpdate(new GCVelocityUpdate());
        super.setPrototypeParticle(new GCDecorator(new StandardParticle()));
    }
    
    public void setPrototypeParticle(Particle particle) {
        super.setPrototypeParticle(new GCDecorator(particle));
    }
    
    public void setVelocityUpdate(VelocityUpdate vu) {
        if (vu instanceof GCVelocityUpdate) {
            super.setVelocityUpdate(vu);
        }
        else {
            throw new RuntimeException("Vecocity update must be a GCVelocityUpdate");
        }
    }
    
    protected void postFitnessUpdateEvent(Particle current) {
        GCDecorator.extract(current).updateRho((GCVelocityUpdate) super.getVelocityUpdate());
    }
    
}
