/*
 * PSO.java
 *
 * Created on January 18, 2003, 4:08 PM
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Algorithm.InitialisationException;
import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.Algorithm.ParticipatingAlgorithm;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationSolution;

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
 * @author  espeer
 */
public class PSO extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {

    /**
     * Creates a new instance of <code>PSO</code>. All fields are initialised to
     * reasonable defaults. Note that the {@link net.sourceforge.cilib.Problem.OptimisationProblem}
     * is initially <code>null</code> and must be set before {@link #initialise()} is called.
     *
     */
    public PSO() {
        problem = null;

        particles = 20;
        topology = new GBestTopology();
        initialiser = new RandomInitialiser();
        velocityUpdate = new StandardVelocityUpdate();
        prototypeParticle = new StandardParticle();
        dissipativeStep = null;
    }

    /**
     * Initialises the <code>PSO</code>. The particle {@link Topology} is initialised.
     * Each particle is initialised to random positions within the search {@link net.sourceforge.cilib.Problem.Domain}
     * using the random generator set by {@link #setInitialisationRandomGenerator(Random)}.
     *
     * @exception InitialistionException The <code>OptimisationProblem</code> is <code>null</code> or the {@link Topology} failed to {@link Topology#initialise(Class)}.
     */
    public void initialise() {
        super.initialise();

        if (problem == null) {
            throw new InitialisationException("No problem has been specified");
        }

        fitnessEvaluations = 0;

        for (int i = 0; i < particles; ++i) {
            Particle particle = null;
            try {
                particle = (Particle) prototypeParticle.clone();
            }
            catch (CloneNotSupportedException ex) {
                throw new InitialisationException("Could not clone prototype particle");
            }
            particle.initialise(problem, initialiser);
            topology.add(particle);
        }
    }

    protected void performIteration() {
        bestParticle = null;
        for (Iterator i = topology.particles(); i.hasNext(); ) {
            Particle current = (Particle) i.next();
            current.setFitness(evaluateFitness(current));
            for (Iterator j = topology.neighbourhood(i); j.hasNext(); ) {
                Particle other = (Particle) j.next();
                if (current.getBestFitness() > other.getNeighbourhoodBest().getBestFitness()) {
                    other.setNeighbourhoodBest(current); // TODO: neighbourhood visitor?
                }
            }
            postFitnessUpdateEvent(current);                // TODO: post fitness visitor for GC rho update?
        }
        for (Iterator i = topology.particles(); i.hasNext(); ) {
            Particle current = (Particle) i.next();
            current.updateVelocity(velocityUpdate);      // TODO: replace with visitor (will simplify particle interface)
            current.move();                                        // TODO: replace with visitor (will simplify particle interface)
            if (dissipativeStep != null) {
                dissipativeStep.execute(current);           // TODO: replace with visitor -- use a composite visitor that this visitor can be added to
            }
        }
    }

    protected void postFitnessUpdateEvent(Particle current) {
    }

    public void setPrototypeParticle(Particle particle) {
        prototypeParticle = particle;
    }

    public Particle getPrototypeParticle() {
    	return prototypeParticle;
    }
    
    public void setVelocityUpdate(VelocityUpdate velocityUpdate) {
        this.velocityUpdate = velocityUpdate;
    }

    public VelocityUpdate getVelocityUpdate() {
        return velocityUpdate;
    }

    public void setInitialiser(Initialiser initialiser) {
        this.initialiser = initialiser;
    }
    
    public Initialiser getInitialiser() {
    	return initialiser;
    }

    /**
     * Set the optimisation problem to be solved. By default, the problem is
     * <code>null</code>. That is, it is necessary to set the optimisation problem
     * before calling {@link #initialise()}.
     *
     * @param problem An implementation of the {@link net.sourceforge.cilib.Problem.OptimisationProblem} interface.
     *
     */
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = problem;
    }

    public OptimisationProblem getOptimisationProblem() {
        return problem;
    }

    public OptimisationSolution getBestSolution() {
        return new OptimisationSolution(problem, getBestParticle().getBestPosition());
    }

    public Collection getSolutions() {
        ArrayList solutions = new ArrayList(1);
        solutions.add(this.getBestSolution());
        return solutions;
    }

    protected Particle getBestParticle() {
        if (bestParticle == null) {
            Iterator i = topology.particles();
            bestParticle = (Particle) i.next();
            double bestFitness = bestParticle.getBestFitness();
            while (i.hasNext()) {
                Particle particle = (Particle) i.next();
                if (particle.getBestFitness() > bestFitness) {
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
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    /**
     * Accessor for the topology being used.
     *
     * @return The {@link Topology} being used.
     */
    public Topology getTopology() {
        return topology;
    }
    
    protected double evaluateFitness(Particle particle) {
        ++fitnessEvaluations;
        return problem.getFitness(particle.getPosition());
    }

    public double[] getContribution() {
        return getBestParticle().getBestPosition();
    }

    public double getContributionFitness() {
        return getBestParticle().getBestFitness();
    }

    public void updateContributionFitness(double fitness) {
        getBestParticle().setFitness(fitness);
    }

    public int getFitnessEvaluations() {
        return fitnessEvaluations;
    }

    public void setDissipativeStep(DissipativeStep dissipativeStep) {
        this.dissipativeStep = dissipativeStep;
        this.dissipativeStep.setPSO(this);
    }

    /**
     * Calculates the diameter of the swarm around the best position. The diameter is calculated using Euclidean distance between each particle's position and the best position.
     *
     * @return The calculated diameter of the swarm.
     */
    public double getSwarmDiameter() {
        double diameter = 0;
        Particle center = getBestParticle();
        for (Iterator i = topology.particles(); i.hasNext(); ) {
            Particle other = (Particle) i.next();
            double sumsq = 0;
            for (int j = 0; j < problem.getDimension(); ++j) {
                double tmp = (center.getPosition()[j] - other.getPosition()[j]);
                sumsq += tmp * tmp;
            }
            diameter += Math.sqrt(sumsq);
        }
        return diameter;
    }

    public static int getNextParticleId() {
        return currentParticleId++;
    }

    public void setParticles(int particles)  {
      this.particles = particles;
    }
    
    public int getParticles() {
    	return particles;
    }

    private static int currentParticleId = 0;

    private int particles;

    private Topology topology;
    private OptimisationProblem problem;

    private Particle bestParticle;
    private Particle prototypeParticle;

    private int fitnessEvaluations;

    private VelocityUpdate velocityUpdate;
    private Initialiser initialiser;

    private DissipativeStep dissipativeStep;
}
