/*
 * StandardParticle.java
 *
 * Created on September 22, 2003, 1:29 PM
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

import net.sourceforge.cilib.Problem.OptimisationProblem;

/**
 *
 * @author  espeer
 */
public class StandardParticle implements Particle, Cloneable {
    
    /** Creates a new instance of StandardParticle */
    public StandardParticle() {
        position = new double[0];
        bestPosition = new double[0];
        velocity = new double[0];
        fitness = - Double.MAX_VALUE;
        bestFitness = - Double.MAX_VALUE;
        neighbourhoodBest = this;
    }
    
    public Object clone() throws CloneNotSupportedException {
        StandardParticle clone = (StandardParticle) super.clone();
        
        clone.position = (double []) position.clone();
        clone.bestPosition = (double []) bestPosition.clone();
        clone.velocity = (double []) velocity.clone();
        return clone;
    }
    
    public double getBestFitness() {
        return bestFitness;
    }
    
    public double[] getBestPosition() {
        return bestPosition;
    }
    
    public int getDimension() {
        return position.length;
    }
    
    public double getFitness() {
        return fitness;
    }
    
    public Particle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }
    
    public double[] getPosition() {
        return position;
    }
    
    public double[] getVelocity() {
        return velocity;
    }
    
    public void initialise(OptimisationProblem problem, Initialiser initialiser) {
        id = PSO.getNextParticleId();
        position = initialiser.getInitialPosition(problem);
        bestPosition = new double[problem.getDimension()];
        for (int i = 0; i < problem.getDimension(); ++i) {
            bestPosition[i] = position[i];
        }
        velocity = initialiser.getInitialVelocity(problem);
        fitness = - Double.MAX_VALUE;
        bestFitness = - Double.MAX_VALUE;
        neighbourhoodBest = this;
    }
    
    public void move() {
        for (int i = 0; i < position.length; ++i) {
            position[i] += velocity[i];
        }
    }
    
    public void setFitness(double fitness) {
        this.fitness = fitness;
        if (fitness > bestFitness) {
            bestFitness = fitness;
            for (int i = 0; i < position.length; ++i) {
                bestPosition[i] = position[i];
            }
        }
    }
    
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = particle;
    }
    
    public void updateVelocity(VelocityUpdate vu) {
        vu.updateVelocity(this);
    }
    
    public int getId() {
        return id;
    }
    
    public Particle getDecorator(Class decorator) {
        throw new RuntimeException("This is not a decorator");
    }
    
    private int id;
    
    private double[] position;
    private double[] bestPosition;
    private double[] velocity;

    private double fitness;
    private double bestFitness;

    private Particle neighbourhoodBest;
}
