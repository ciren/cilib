/*
 * GCPSO.java
 *
 * Created on January 23, 2003, 5:21 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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
 * This class is an implementation of the Guaranteed Convergence PSO algorithm, references:
 *      1.  F. van den Bergh and A. Engelbrecht, "A new locally convergent particle swarm optimizer,"
 *          in Proceedings of IEEE Conference on Systems, Man and Cybernetics,
 *          (Hammamet, Tunisia), Oct. 2002.
 *      2.  F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 *          PhD thesis, Department of Computer Science, 
 *          University of Pretoria, South Africa, 2002.
 */

package net.sourceforge.cilib.PSO;

import java.util.*;
import net.sourceforge.cilib.Random.*;

/**
 *
 * @author  espeer
 */
public class GCPSO extends PSO {
    
    /** Creates a new instance of GCPSO */
    public GCPSO() {
        super();
        successThreshold = 5;
        failureThreshold = 5;
        rhoExpandCoefficient = 1.2;
        rhoContractCoefficient = 0.5;
        rhoRandomGenerator = new KnuthSubtractive(getSeedRandomGenerator().nextLong());
        setParticleClass(GCParticle.class);
    }
    
    public void setRhoRandomGenerator(Random rhoRandomGenerator) {
        this.rhoRandomGenerator = rhoRandomGenerator;
        this.rhoRandomGenerator.setSeed(getSeedRandomGenerator().nextLong());
    }
    
    public Random getRhoRandomGenerator() {
        return rhoRandomGenerator;
    }
    
    public void setSuccessThreshold(int successThreshold) {
        this.successThreshold = successThreshold;
    }
    
    public int getSuccessThreshold() {
        return successThreshold;
    }
    
    public void setFailureThreshold(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }
    
    public int getFailureThreshold() {
        return failureThreshold;
    }
    
    public void setRhoExpandCoefficient(double rhoExpandCoefficient) {
        this.rhoExpandCoefficient = rhoExpandCoefficient;
    }
    
    public double getRhoExpandCoefficient() {
        return rhoExpandCoefficient;
    }
    
    public void setRhoContractCoefficient(double rhoContractCoefficient) {
        this.rhoContractCoefficient = rhoContractCoefficient;
    }
    
    public double getRhoContractCoefficient() {
        return rhoContractCoefficient;
    }
    
    private Random rhoRandomGenerator;
    private int successThreshold;
    private int failureThreshold;
    private double rhoExpandCoefficient;
    private double rhoContractCoefficient;
}
