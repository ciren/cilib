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

import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.InferiorFitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Type.DomainParser;
import net.sourceforge.cilib.Type.Types.MixedVector;
import net.sourceforge.cilib.Type.Types.Vector;

/**
 *
 * @author  espeer
 */
public class StandardParticle implements Particle, Cloneable {
    
    /** Creates a new instance of StandardParticle */
    public StandardParticle() {
    	//position = new double[0];
    	//bestPosition = new double[0];
        //velocity = new double[0];
    	
        position = new MixedVector();
        bestPosition = new MixedVector();
        velocity = new MixedVector();
    }
    
    public Object clone() throws CloneNotSupportedException {
        StandardParticle clone = (StandardParticle) super.clone();
        
        //clone.position = (double []) position.clone();
        //clone.bestPosition = (double []) bestPosition.clone();
        //clone.velocity = (double []) velocity.clone();
        
        clone.position = (MixedVector) position.clone();
        clone.bestPosition = (MixedVector) bestPosition.clone();
        clone.velocity = (MixedVector) velocity.clone();
        
        return clone;
    }
    
    public Fitness getBestFitness() {
        return bestFitness;
    }
    
    public Vector getBestPosition() {
        return bestPosition;
    }
    
    public int getDimension() {
        //return position.length;
    	return position.getDimension();
    }
    
    public Fitness getFitness() {
        return fitness;
    }
    
    public Particle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public Vector getVelocity() {
        return velocity;
    }
    
    public void initialise(OptimisationProblem problem, Initialiser initialiser) {
        id = PSO.getNextParticleId();
        position = initialiser.getInitialPosition(problem);
        //bestPosition = new double[problem.getDomain().getDimension()];
                
        bestPosition = new MixedVector(DomainParser.getInstance().getDimension());
        //for (int i = 0; i < problem.getDomain().getDimension(); ++i) {
        for (int i = 0; i < DomainParser.getInstance().getDimension(); ++i) {
            //bestPosition[i] = position[i];
        	//bestPosition.set(i, position.get(i));
        	bestPosition.append(position.get(i));
        }
        
        //try { bestPosition = (Vector) position.clone(); } catch (CloneNotSupportedException c) {}
        velocity = initialiser.getInitialVelocity(problem);
        fitness = InferiorFitness.instance();
        bestFitness = InferiorFitness.instance();
        neighbourhoodBest = this;
    }
    
    public void move() {
        //for (int i = 0; i < position.length; ++i) {
        //    position[i] += velocity[i];
        //}
    	
    	// FIXME: There could be a bug here (solved the setReal bug)
    	for (int i = 0; i < position.getDimension(); ++i) {
    		double value = position.getReal(i);
    		value += velocity.getReal(i);
    		position.setReal(i, value);
    	}
    }
    
    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
        if (fitness.compareTo(bestFitness) > 0) {
            bestFitness = fitness;
            //for (int i = 0; i < position.length; ++i) {
            //    bestPosition[i] = position[i];
            for (int i = 0; i < position.getDimension(); ++i) {
            	bestPosition.set(i, position.get(i)); // FIXME: BUG? same object? clone needed?
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
    
    protected Vector position;
    protected Vector bestPosition;
    protected Vector velocity;

    private Fitness fitness;
    private Fitness bestFitness;

    private Particle neighbourhoodBest;
    
}
