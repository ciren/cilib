/*
 * StandardVelocityUpdate.java
 *
 * Created on September 22, 2003, 1:03 PM
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
 */

package net.sourceforge.cilib.PSO;

import java.util.Random;

import net.sourceforge.cilib.Random.KnuthSubtractive;

/**
 *
 * @author  espeer
 */
public class StandardVelocityUpdate implements VelocityUpdate {
    
    /** Creates a new instance of StandardVelocityUpdate */
    public StandardVelocityUpdate() {
        maximumVelocity = Double.MAX_VALUE;
        inertia = 0.729844;
        cognitiveAcceleration = 1.496180;
        socialAcceleration = 1.496180;
        
        cognitiveRandomGenerator = new KnuthSubtractive();
        socialRandomGenerator = new KnuthSubtractive();
    }

    public void updateVelocity(Particle particle) {
        double[] velocity = particle.getVelocity();
        double[] position = particle.getPosition();
        double[] bestPosition = particle.getBestPosition();
        double[] nBestPosition = particle.getNeighbourhoodBest().getBestPosition();
        
        for (int i = 0; i < particle.getDimension(); ++i) {
            velocity[i] = inertia * velocity[i]
                + (bestPosition[i] - position[i]) * cognitiveAcceleration * cognitiveRandomGenerator.nextFloat()
                + (nBestPosition[i] - position[i]) * socialAcceleration* socialRandomGenerator.nextFloat();
            if (velocity[i] < -maximumVelocity) {
                velocity[i] = -maximumVelocity;
            }
            else if (velocity[i] > maximumVelocity) {
                velocity[i] = maximumVelocity;
            }
        }
    }    
    
    /**
     * Accessor for the random number generator used in the cognitive term of the particle update equation. 
     *
     * @return The {@link java.util.Random} generator used in the cognitive term.
     */
    public Random getCognitiveRandomGenerator() {
        return cognitiveRandomGenerator;
    }
    
    /**
     * Sets the random number generator used in the cognitive term of the particle update equation. The default is {@link net.sourceforge.cilib.Random.KnuthSubtractive}.
     *
     * @param cognitiveRandomGenerator The {@link java.util.Random} generator used in the cognitive term.
     */
    public void setCognitiveRandomGenerator(Random cognitiveRandomGenerator) {
        this.cognitiveRandomGenerator = cognitiveRandomGenerator;
    }
    
    /**
     * Accessor for the random number generator used in the social term of the particle update equation. 
     *
     * @return The {@link java.util.Random} generator used in the social term.
     */
    public Random getSocialRandomGenerator() {
        return socialRandomGenerator;
    }
    
    /**
     * Sets the random number generator used in the social term of the particle update equation. The default is {@link net.sourceforge.cilib.Random.KnuthSubtractive}.
     *
     * @param socialRandomGenerator The {&link java.util.Random} generator used in the social term.
     */
    public void setSocialRandomGenerator(Random socialRandomGenerator) {
        this.socialRandomGenerator = socialRandomGenerator;
    }
    
    /** 
     * Accessor for the cognitive acceleration coefficient for the cognitive term of the particle update equation.
     *
     * @return The cognitive acceleration coefficient.
     */
    public double getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }
    
    /** 
     * Sets the cognitive acceleration coefficient for the cognitive term of the particle update equation. The default is 1.496180.
     *
     * @param cognitiveAcceleration The cognitive acceleration coefficient.
     */
    public void setCognitiveAcceleration(double cognitiveAcceleration) {
        this.cognitiveAcceleration = cognitiveAcceleration;
    }

    /** 
     * Accessor for the social acceleration coefficient for the social term of the particle update equation.
     *
     * @return The social acceleration coefficient.
     */
    public double getSocialAcceleration() {
        return socialAcceleration;
    }
    
    /** 
     * Sets the social acceleration coefficient for the social term of the particle update equation. The default is 1.496180.
     *
     * @param socialAcceleration The cognitive acceleration coefficient.
     */
    public void setSocialAcceleration(double socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }
    
    /**
     * Accessor for the inertia weight in the particle swarm update equation.
     *
     * @return The inertia weight.
     */
    public double getInertia() {
        return inertia;
    }
    
    /**
     * Sets the intertia weight in the particle swarm update equation. The default is 0.729844.
     *
     * @param inertia The inertia weight.
     */
    public void setInertia(double inertia) {
        this.inertia = inertia;
    }
    
    /**
     * Accessor for the maximum absolute velocity permitted in any dimension. 
     *
     * @return The maximum velocity.
     */
    public double getMaximumVelocity() {
        return maximumVelocity;
    }
    
    /**
     * Sets the maximum absolute velocity permitted in any dimension. The default is Double.MAX_VALUE. That is, vmax is not used by default.
     *
     * @param vmax The maximum velocity.
     */
    public void setMaximumVelocity(double maximumVelocity) {
        this.maximumVelocity = maximumVelocity;
    }

    
    private Random cognitiveRandomGenerator;
    private Random socialRandomGenerator;
    
    private double cognitiveAcceleration;
    private double socialAcceleration;
    private double inertia;
    private double maximumVelocity;
}
