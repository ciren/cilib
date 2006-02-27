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
 */

package net.sourceforge.cilib.PSO;

import java.util.*;
import net.sourceforge.cilib.Random.*;

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
        successThreshold = 5;
        failureThreshold = 5;
        rhoExpandCoefficient = 1.2;
        rhoContractCoefficient = 0.5;
        rhoRandomGenerator = new KnuthSubtractive(getSeedRandomGenerator().nextLong());
        setParticleClass(GCParticle.class);
    }
    
    /**
     * Sets the random number generator used in the GC update equation. The default is {@link net.sourceforge.cilib.Random.KnuthSubtractive}.
     *
     * @param rhoRandomGenerator The {@link java.util.Random} generator used in the GC update.
     */
    public void setRhoRandomGenerator(Random rhoRandomGenerator) {
        this.rhoRandomGenerator = rhoRandomGenerator;
        this.rhoRandomGenerator.setSeed(getSeedRandomGenerator().nextLong());
    }
    
    /**
     * Accessor for the random number generator used in the GC update equation.
     *
     * @return The {@link java.util.Random} generator used in the GC update.
     */
    public Random getRhoRandomGenerator() {
        return rhoRandomGenerator;
    }
    
    /**
     * Sets the success threshold used to determine when to increase rho. The default is 5.
     *
     * @param successThreshold The success threshold.
     */
    public void setSuccessThreshold(int successThreshold) {
        this.successThreshold = successThreshold;
    }
    
    /**
     * Accessor for the success threshold used to determine when to increase rho.
     *
     * @return The success threshold.
     */
    public int getSuccessThreshold() {
        return successThreshold;
    }
    
    /**
     * Sets the failure threshold used to determine when to decrease rho. The default is 5.
     *
     * @param failureThreshold The failure threshold.
     */
    public void setFailureThreshold(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }
    
    /**
     * Accessor for the failure threshold used to determine when to decrease rho. The default is 5.
     *
     * @return The failure threshold.
     */
    public int getFailureThreshold() {
        return failureThreshold;
    }
    
    /**
     * Sets the multiplication coefficient used to increase rho. The default is 1.2.
     *
     * @param rhoExpandCoefficient The rho expansion coefficient
     */
    public void setRhoExpandCoefficient(double rhoExpandCoefficient) {
        this.rhoExpandCoefficient = rhoExpandCoefficient;
    }
    
    /**
     * Accessor for the multiplication coefficient used to increase rho.
     *
     * @return The rho expansion coefficient.
     */
    public double getRhoExpandCoefficient() {
        return rhoExpandCoefficient;
    }
    
    /**
     * Sets the multiplication coefficient used to decrease rho. The default is 0.5.
     *
     * @param rhoContractCoefficient The rho contraction coefficient.
     */
    public void setRhoContractCoefficient(double rhoContractCoefficient) {
        this.rhoContractCoefficient = rhoContractCoefficient;
    }
    
    /** 
     * Accessor for the multiplication coefficient used to decrease rho. 
     *
     * @return The rho contration coefficient.
     */
    public double getRhoContractCoefficient() {
        return rhoContractCoefficient;
    }
    
    private Random rhoRandomGenerator;
    private int successThreshold;
    private int failureThreshold;
    private double rhoExpandCoefficient;
    private double rhoContractCoefficient;
}
