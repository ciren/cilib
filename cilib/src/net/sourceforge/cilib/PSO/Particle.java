/*
 * Particle.java
 *
 * Created on January 15, 2003, 8:27 PM
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

import java.lang.*;
import java.util.*;
import net.sourceforge.cilib.Algorithm.*;
import net.sourceforge.cilib.Random.*;

/**
 *
 * @author  espeer
 */
public class Particle {
    
    public Particle() {
        reset();
    }
    
    public void reset() {
        fitness = - Double.MAX_VALUE;
        bestFitness = - Double.MAX_VALUE;
        neighbourhoodBest = this;      
    }
    
    public void setFitness(double fitness) {
        this.fitness = fitness;
        if (fitness > bestFitness) {
            bestFitness = fitness;
            for (int i = 0; i < dimension; ++i) {
                bestPosition[i] = position[i];
            }
        }
    }
    
    public void setBestFitness(double fitness) {
        bestFitness = fitness;
    }
    
    public double getFitness() {
        return fitness;
    }
    
    public double getBestFitness() {
        return bestFitness;
    }
    
    public int getDimension() {
        return dimension;
    }
    
    public void setDimension(int dimension) {
        this.dimension = dimension;
        position = new double[dimension];
        bestPosition = new double[dimension];
        velocity = new double[dimension];
    }
    
    public void initialise(int component, double position) {
        this.position[component] = position;
        this.bestPosition[component] = position;
        this.velocity[component] = 0;
    }
    
    public double[] getPosition() {
        return position;
    }
   
    public double[] getBestPosition() {
        return bestPosition;
    }
    
    public double[] getVelocity() {
        return velocity;
    }
    
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = particle;
    }
    
    public Particle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }
    
    public void fly() {
        for (int i = 0; i < dimension; ++i) {
            if (velocity[i] < -pso.getVmax()) {
                velocity[i] = -pso.getVmax();
            }
            else if (velocity[i] > pso.getVmax()) {
                velocity[i] = pso.getVmax();
            }
            position[i] += velocity[i];
        }
    }
    
    public void updateVelocity() {
        for (int i = 0; i < dimension; ++i) {
            velocity[i] = pso.getInertia() * velocity[i]
                + (bestPosition[i] - position[i]) 
                    * pso.getCognitiveAcceleration()
                    * pso.getCognitiveRandomGenerator().nextFloat() 
                + (neighbourhoodBest.bestPosition[i] - position[i]) 
                    * pso.getSocialAcceleration() 
                    * pso.getSocialRandomGenerator().nextFloat();
        }
    }
    
    public void updateRho() {
    }
    
    public void setPSO(PSO pso) {
        this.pso = pso;
    }
    
    private int dimension;
    private double[] position;
    private double[] bestPosition;
    private double[] velocity;
    
    private double fitness;
    private double bestFitness;
    
    private Particle neighbourhoodBest;
    private PSO pso;
}
