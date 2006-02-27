/*
 * GCVelocityUpdate.java
 *
 * Created on September 22, 2003, 2:52 PM
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
 */

package net.sourceforge.cilib.PSO;

import java.util.Random;

import net.sourceforge.cilib.Random.KnuthSubtractive;

/**
 *
 * @author  espeer
 */
public class GCVelocityUpdate implements VelocityUpdate {
    
    /** Creates a new instance of GCVelocityUpdate */
    public GCVelocityUpdate() {
        standard = new StandardVelocityUpdate();
        rhoRandomGenerator = new KnuthSubtractive();
        
        successThreshold = 5;
        failureThreshold = 5;
        rhoExpandCoefficient = 1.2;
        rhoContractCoefficient = 0.5;
    }
    
    public void setStandardVelocityUpdate(StandardVelocityUpdate standard) {
        this.standard = standard;
    }
    
    public StandardVelocityUpdate getStandardVelocityUpdate() {
        return standard;
    }
    
    public void updateVelocity(Particle particle) {
        if (particle.getNeighbourhoodBest().getId() == particle.getId()) {
            double rho = GCDecorator.extract(particle).getRho();
            double[] velocity = particle.getVelocity();
            double[] position = particle.getPosition();
            double[] bestPosition = particle.getBestPosition();
            
            for (int i = 0; i < particle.getDimension(); ++i) {
                velocity[i] = bestPosition[i] - position[i] + 
                standard.getInertiaComponent().get() * velocity[i] 
                  + rho * (1 - 2 * rhoRandomGenerator.nextFloat());
            }
        }
        else {
            standard.updateVelocity(particle);
        }
    }
    
    /**
     * Sets the random number generator used in the GC update equation. The default is {@link net.sourceforge.cilib.Random.KnuthSubtractive}.
     *
     * @param rhoRandomGenerator The {@link java.util.Random} generator used in the GC update.
     */
    public void setRhoRandomGenerator(Random rhoRandomGenerator) {
        this.rhoRandomGenerator = rhoRandomGenerator;
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
    
    private StandardVelocityUpdate standard;
    private Random rhoRandomGenerator;
    
    private int successThreshold;
    private int failureThreshold;
    private double rhoContractCoefficient;
    private double rhoExpandCoefficient;
    
}
