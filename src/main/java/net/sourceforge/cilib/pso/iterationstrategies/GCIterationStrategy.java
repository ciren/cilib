/*
 * GCSyncronousIterationStrategy.java
 *
 * Created on Oct 14, 2005
 *
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.pso.iterationstrategies;

import java.util.Iterator;
import java.util.Random;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityupdatestrategies.NewGCVelocityUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @TODO: This is a temporary solution. We will need to convert this class into a single
 *        VelocityUpdateStrategy.
 *        
 * @author Camiel Castillo
 * Guaranteed Convergence PSO iteration strategy
 * as proposed by F van den Bergh in "A new Locally Convergent Particle Swarm Optimiser"
 * 
 * @deprecated This class has been replaced with {@link NewGCVelocityUpdateStrategy}
 */
@Deprecated
public class GCIterationStrategy extends IterationStrategy<PSO> {
	private static final long serialVersionUID = 1183812644462220386L;
	Particle gbestParticle = null;
	Type gbestPosition = null;
	Fitness gbestFitness = null;
	Random randomizer = new Random();
	double rho,initialRho;
	int nrOfSuccesses, nrOfFailures;
	int rhoIncreaseThreshold,rhoDecreaseThreshold;
	int iterationNr = 0;
	boolean printStats = false;
		
	public GCIterationStrategy(){
		initialRho = 1.0;
		rhoIncreaseThreshold = 15;
		rhoDecreaseThreshold = 5;
	}
	
	public GCIterationStrategy clone() {
		GCIterationStrategy newGCS = new GCIterationStrategy();
		newGCS.setInitialRho(this.initialRho);
		newGCS.setRhoDecreaseThreshold(this.rhoDecreaseThreshold);
		newGCS.setRhoIncreaseThreshold(this.rhoIncreaseThreshold);
		newGCS.printStats = this.printStats;
		return newGCS;
	}

	public void performIteration(PSO pso) {
		if (printStats)System.out.println("\n\n*********** new iteration: " +(iterationNr++) + " ***********\n");
		
		for (Iterator<Particle> i = pso.getTopology().iterator(); i.hasNext();) {
			
			// fetch the particle fitnesses and the best particle
			Particle current = i.next();
			Fitness currentFitness = pso.getOptimisationProblem().getFitness(current.getPosition(), true);
			current.calculateFitness();//setFitness(currentFitness);
			// first particle at the first iteration
			if(gbestParticle == null){
				gbestParticle = current;
				gbestPosition = current.getPosition().clone();
				gbestFitness = current.getFitness().clone();
				resetGCParameters();
				
				if(printStats){
					System.out.println("first particle is GB. ID: " + current.getId() + " , fitness: " + currentFitness.toString());
				}
			}
			// found a new global best particle
			else if(currentFitness.compareTo(gbestFitness) == 1 && current.getId().compareTo(gbestParticle.getId()) != 0){
			
				if(printStats){
					System.out.println("old GB particle ID: " + gbestParticle.getId() + " , fitness: " + gbestFitness.toString());
					System.out.println("new particle is GB. ID: " + current.getId() + " , fitness: " + currentFitness.toString());
				}
				
				gbestParticle = current;
				gbestPosition = current.getPosition().clone();
				gbestFitness = current.getFitness().clone();
				resetGCParameters();
			}
			
			// update neighbourhood bests for each particle in currents neighbourhood
			for (Iterator<Particle> j = pso.getTopology().neighbourhood(i); j.hasNext();) {
				Particle other = j.next();
				if (current.getSocialBestFitness().compareTo(other.getNeighbourhoodBest().getSocialBestFitness()) > 0) {
					other.setNeighbourhoodBest(current);  
				}
			}
		}

		// update particle velocitiy and position for each particle
		for (Iterator<Particle> i = pso.getTopology().iterator(); i.hasNext();) {
			Particle current = i.next();

			// here follows the GC update strategy for the best particle
			if(current.getId().compareTo(gbestParticle.getId()) == 0){
				
				if(printStats){
					System.out.println("GC update for particle " + current.getId());
					System.out.println("current position: " + gbestPosition.toString());
				}
				
				double inertiaWeight = ((StandardVelocityUpdate)current.getVelocityUpdateStrategy()).getInertiaWeight().getParameter();
		    	double vMax = ((StandardVelocityUpdate)current.getVelocityUpdateStrategy()).getVMax().getParameter();
		    	Vector velocity = (Vector) current.getVelocity();
		    	Vector position = (Vector) current.getPosition();
		    	
		        // set the velocity and position for each dimension
		    	for (int currentDimension = 0; currentDimension < velocity.size(); currentDimension++) {
		        	double newVelocity = (-1*position.getReal(currentDimension))+((Vector)gbestPosition).getReal(currentDimension) +
		        		(inertiaWeight*velocity.getReal(currentDimension)) +(rho*(1-(2*randomizer.nextDouble()*1.496180)));
		        	
		        	// clamp the velocity
		        	if (velocity.getReal(currentDimension) < -vMax) velocity.setReal(currentDimension, -vMax);
		    		else if (velocity.getReal(currentDimension) > vMax) velocity.setReal(currentDimension, vMax);

		        	velocity.setReal(currentDimension, newVelocity);
		        	position.setReal(currentDimension, (position.getReal(currentDimension)+newVelocity));
		        }
				
				Fitness fitnessTPlusOne = pso.getOptimisationProblem().getFitness(current.getPosition(), true).clone();
				
				if(printStats){
					System.out.println("new position: " + current.getPosition().toString());
					System.out.println("current fitness: " + gbestFitness.toString());
					System.out.println("new fitness: " + fitnessTPlusOne.toString());
				}
				// set the number of consecutive successes and failures 
				if(fitnessTPlusOne.compareTo(gbestFitness) == 1){
					if(printStats)System.out.println("success!");
					gbestPosition = current.getPosition().clone();
					gbestFitness = current.getFitness().clone();
					if(nrOfSuccesses == 0)resetGCParameters();
					nrOfSuccesses++;
				}else{
					if(printStats)System.out.println("failure!");
					if(nrOfFailures == 0)resetGCParameters();
					nrOfFailures++;
				}
				
				if(nrOfSuccesses%rhoIncreaseThreshold==0 && nrOfSuccesses != 0) rho *= 2;
				else if(nrOfFailures%rhoDecreaseThreshold==0 && nrOfFailures != 0) rho /= 2;
			
				if(printStats){
					System.out.println("Rho: " + rho);
					System.out.println("nrOfSuccesses: " + nrOfSuccesses);
					System.out.println("nrOfFailures: " + nrOfFailures);
				}
			}
			// for all other particles, apply the normal update
			else{
				current.updateVelocity();
				current.updatePosition();
			}
			boundaryConstraint.enforce(current);
		}
	}
	
	private void resetGCParameters(){
		rho=initialRho;
		nrOfFailures = 0;
		nrOfSuccesses = 0;
	}

	public void setInitialRho(double someRho){
		initialRho = someRho;
	}
	
	public void setRhoIncreaseThreshold(int someThreshold){
		rhoIncreaseThreshold = someThreshold;
	}
	
	public void setRhoDecreaseThreshold(int someThreshold){
		rhoDecreaseThreshold = someThreshold;
	}
	
	public void setPrintStats(boolean setting){
		printStats = setting;
	}
}
