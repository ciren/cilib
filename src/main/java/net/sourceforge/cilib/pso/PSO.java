/*
 * PSO.java
 *
 * Created on January 18, 2003, 4:08 PM
 *
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
 *
 */

package net.sourceforge.cilib.pso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import net.sourceforge.cilib.algorithm.initialisation.ClonedEntityInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * <p>
 * An implementation of the standard PSO algorithm.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * J. Kennedy and R.C. Eberhart, "Particle swarm optimization,"
 * in Proceedings of IEEE International Conference on Neural Networks,
 * vol. IV, (Perth Australia), pp. 1942-1948, 1995.
 * </li><li>
 * R.C. Eberhart and J. Kennedy, "A new optimizer using particle swarm theory,"
 * in Proceedings of the Sixth International Symposium on Micro Machine and Human Science,
 * (Nagoya, Japan), pp. 39-43, 1995.
 * </li><li>
 * Y. Shi amd R.C. Eberhart, "A modified particle swarm optimizer,"
 * in Proceedings of the IEEE Congress on Evolutionary Computation,
 * (Anchorage, Alaska), pp. 69-73, May 1998.
 * </li></ul></p>
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class PSO extends PopulationBasedAlgorithm implements ParticipatingAlgorithm {
	
	private static final long serialVersionUID = -8234345682394295357L;

	private static Logger log = Logger.getLogger(PSO.class);
	
	private static int currentParticleId = 0;

	private int particles;

	private Topology<Particle> topology;
	 
	private Particle bestParticle;
	    
	private IterationStrategy iterationStrategy;
	    
	private InitialisationStrategy initialisationStrategy;
	protected boolean participation = false;
	 
    /**
     * Creates a new instance of <code>PSO</code>. All fields are initialised to
     * reasonable defaults. Note that the {@link net.sourceforge.cilib.problem.OptimisationProblem}
     * is initially <code>null</code> and must be set before {@link #initialise()} is called.
     *
     */
    public PSO() {
        //particles = 20;
        topology = new GBestTopology<Particle>();

        //prototypeParticle = new StandardParticle();
        iterationStrategy = new SynchronousIterationStrategy();
        
        initialisationStrategy = new ClonedEntityInitialisationStrategy();
        initialisationStrategy.setEntityType(new StandardParticle());
    }
    
    public PSO(PSO copy) {
    	this.topology = copy.topology.clone();
    	this.iterationStrategy = copy.iterationStrategy; // need to clone?
    	this.initialisationStrategy = copy.initialisationStrategy; // need to clone?
    }
    
    public PSO clone() {
    	return new PSO(this);
    }

    
    /**
     * Perform the required initialisation for the algorithm. Create the particles and add
     * then to the specified topology.
     */
    public void performInitialisation() {
        this.initialisationStrategy.intialise(this.topology, this.getOptimisationProblem());
    }

    
    /**
     * Perform the iteration of the PSO algorithm, use the appropriate <code>IterationStrategy</code>
     * to perform the iteration. 
     */
    @SuppressWarnings("unchecked")
	public void performIteration() {
        bestParticle = null;
        
        iterationStrategy.performIteration(this);

        for (Iterator<Particle> i = this.getTopology().iterator(); i.hasNext(); ) {
        	i.next().getVelocityUpdateStrategy().updateControlParameters();
        }
        log.debug("Performing iteration");
    }

    
    /**
     * 
     * @return
     */   
    public InitialisationStrategy getInitialisationStrategy() {
		return initialisationStrategy;
	}


    /**
     * 
     * @param initialisationStrategy
     */
	public void setInitialisationStrategy(
			InitialisationStrategy initialisationStrategy) {
		this.initialisationStrategy = initialisationStrategy;
	}   
    
    
    /**
     * Get the best current solution. This best solution is determined from the personal bests of the particles.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    public OptimisationSolution getBestSolution() {
    	OptimisationSolution solution = new OptimisationSolution(this.getOptimisationProblem(), getBestParticle().getBestPosition().clone());
        
        return solution;
    }

    
    /**
     * Get the collection of best solutions. This result does not actually make sense in the normal PSO
     * algorithm, but rather in a MultiObjective optimisation.
     * @return  The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions. 
     */
    public List<OptimisationSolution> getSolutions() {
        ArrayList<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>(1);
        solutions.add(this.getBestSolution());
        return solutions;
    }

    
    /**
     * Get the current best particle. This is determined from the personal bests
     * @return The best <code>Particle</code> so far.
     */
    public Particle getBestParticle() {
        if (bestParticle == null) {
        	Iterator<Particle> i = topology.iterator();
            bestParticle =  i.next();
            Fitness bestFitness = bestParticle.getBestFitness();
            while (i.hasNext()) {
                Particle particle = i.next();
                if (particle.getBestFitness().compareTo(bestFitness) > 0) {
                    bestParticle = particle;
                    bestFitness = bestParticle.getBestFitness();
                }
            }
        }
        return bestParticle;
    }

    
    /**
     * Sets the particle topology used. The default is {@link GBestTopology}.
     *
     * @param A class that implements the {@link Topology} interface.
     */
	@SuppressWarnings("unchecked")
	public void setTopology(Topology topology) {
	//	log.debug("topology set: " + topology);
        this.topology = topology;
    }

    
    /**
     * Accessor for the topology being used.
     *
     * @return The {@link Topology} being used.
     */
    public Topology<Particle> getTopology() {
        return topology;
    }
    
	// TODO: Move down heirarchy into MOPSO????
	public Particle getContribution() {
		return getBestParticle();
	}

    public Fitness getContributionFitness() {
        return getBestParticle().getBestFitness();
    }

    public void updateContributionFitness(Fitness fitness) {
        getBestParticle().setFitness(fitness);
    }
    
    public boolean participated() {
		return participation;
	}

	public void participated(boolean p) {
		participation = p;
	}

    // TODO: This does not fit really here.... move to another class PSOUtilities. 
    // Does not really calculate the diameter.
    /**
     * Calculates the diameter of the swarm around the position of the best particle.
     *  The diameter is calculated using the average Euclidean distance.
     *
     * @return The calculated diameter of the swarm.
     */
    public double getDiameter() {
  
    	/* The code below has been moved to Measurement.EuclideanDiversityAroundGBest
    	 * The new code as updated by Andries Engelbrecht calculates the real diameter
    	 *  	Vector center = (Vector) getBestParticle().getPosition();
    	DistanceMeasure distance = new EuclideanDistanceMeasure();
        double diameter = 0;
        int count = 0;

        for (Iterator<Particle> i = topology.iterator(); i.hasNext(); ++count) {
            Particle other = i.next();
            diameter += distance.distance(center, (Vector) other.getPosition());
        }*/
    
    	//TODO: Can be optimised by not calculating symmetrical distances
    	
    	double maxDistance = 0.0;
    	
    	Iterator k1 = getTopology().iterator();
        while (k1.hasNext()) {
            Particle p1 = (Particle) k1.next();
        	Vector position1 = (Vector) p1.getPosition();
           	
        	Iterator k2 = getTopology().iterator();
        	while (k2.hasNext()) {
        		Particle p2 = (Particle) k2.next();
        		Vector position2 = (Vector) p2.getPosition();
        		DistanceMeasure distance = new EuclideanDistanceMeasure();
        		double actualDistance = distance.distance(position1,position2);
        		if (actualDistance > maxDistance)
        			maxDistance = actualDistance;
        	}
        }
    	
        return maxDistance;
    }
    
    
    /**
     * Calculates the radius of the swarm around the position of the best particle.
     *  The radius is calculated using the average Euclidean distance.
     *
     * @return The calculated radius of the swarm.
     */
    @Override
    public double getRadius() {
    	double maxDistance = 0.0;
    	
    	Particle swarmBestParticle = getBestParticle();
    	Vector swarmBestParticlePosition = (Vector)swarmBestParticle.getPosition();
    	
    	Iterator swarmIterator = getTopology().iterator();
    	
    	while(swarmIterator.hasNext()) {
    		Particle swarmParticle = (Particle)swarmIterator.next();
    		Vector swarmParticlePosition = (Vector)swarmParticle.getPosition();
    	
    		DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
    		double actualDistance = distanceMeasure.distance(swarmBestParticlePosition, swarmParticlePosition);
    	
    		if(actualDistance > maxDistance)
    			maxDistance = actualDistance;
    	}
    	
    	return maxDistance;
    }

    
    /**
     * Get the next sequential unique particle identifier
     * @return The next unique particle identifier
     */
    public static int getNextParticleId() {
        return currentParticleId++;
    }

    
    /**
     * Set the required number of particles.
     * @param particles The required number of particles
     */
    public void setParticles(int particles)  {
      this.particles = particles;
      this.initialisationStrategy.setEntities(particles);
    }
    
    
    /**
     * Get the number of particles set
     * @return The number of particles
     */
    public int getParticles() {
    	return particles;
    }
    
    
    public int getPopulationSize() {
    	return this.getParticles();
    }
    
	public void setPopulationSize(int populationSize) {
		this.setParticles(populationSize);
	}
    
    
    /**
     * Get the <code>IterationStrategy</code> of the PSO algorithm
	 * @return Returns the iterationStrategy.
	 */
	public IterationStrategy getIterationStrategy() {
		return iterationStrategy;
	}
	
	
	/**
	 * Set the <code>IterationStrategy</code> to be used
	 * @param iterationStrategy The iterationStrategy to set.
	 */
	public void setIterationStrategy(IterationStrategy iterationStrategy) {
		this.iterationStrategy = iterationStrategy;
	}
	
   
}
