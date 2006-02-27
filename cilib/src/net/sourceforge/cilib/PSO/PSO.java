/*
 * PSO.java
 *
 * Created on January 18, 2003, 4:08 PM
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
import net.sourceforge.cilib.Problem.*;
import net.sourceforge.cilib.Random.*;

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
        topology = new GBestTopology();
        seedRandomGenerator = new Random();
        initialisationRandomGenerator = new MersenneTwister(seedRandomGenerator.nextLong());
        cognitiveRandomGenerator = new KnuthSubtractive(seedRandomGenerator.nextLong());
        socialRandomGenerator = new KnuthSubtractive(seedRandomGenerator.nextLong());
        vmax = Double.MAX_VALUE;
        inertia = 0.729844;
        cognitiveAcceleration = 1.496180;
        socialAcceleration = 1.496180;
        particleClass = Particle.class;
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
        topology.initialise(particleClass);
        for (Iterator i = topology.particles(); i.hasNext(); ) {
            Particle particle = (Particle) i.next();
            particle.setPSO(this);
            particle.setDimension(problem.getDimension());
            for (int j = 0; j < problem.getDimension(); ++j) {
                Domain domain = problem.getDomain(j);
                double position = initialisationRandomGenerator.nextDouble();
                position *= (domain.getUpperBound() - domain.getLowerBound());
                position += domain.getLowerBound();
                particle.initialise(j, position);
            }
            particle.reset();
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
                    other.setNeighbourhoodBest(current);
                }
            }
            current.updateRho();
        }
        for (Iterator i = topology.particles(); i.hasNext(); ) {
            Particle current = (Particle) i.next();
            current.updateVelocity();
            current.fly();
            if (dissipativeStep != null) {
                dissipativeStep.execute(current);
            }
        }
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
    
    public double[] getSolution() {
        return getBestParticle().getBestPosition();
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
     
    protected void setParticleClass(Class particleClass) {
        this.particleClass = particleClass;
    }
    
    protected Class getParticleClass() {
        return particleClass;
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

    /**
     * Sets the random number generator used for initialising particle positions. The default is {@link net.sourceforge.cilib.Random.MersenneTwister}.
     * 
     * @param initialisationRandomGenerator The {@link java.util.Random} generator used to initialise particle positions.
     */
    public void setInitialisationRandomGenerator(Random initialisationRandomGenerator) {
        this.initialisationRandomGenerator = initialisationRandomGenerator;
        this.initialisationRandomGenerator.setSeed(seedRandomGenerator.nextLong());
    }
    
    /**
     * Accessor for the random number generator used for initialising particle positions.
     *
     * @return The {@link java.util.Random} generator used for initialising particle positions.
     */
    public Random getInitialisationRandomGenerator() {
        return initialisationRandomGenerator;
    }
    
    protected double evaluateFitness(Particle particle) {
        ++fitnessEvaluations;
        return problem.getFitness(particle.getPosition());
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
        this.cognitiveRandomGenerator.setSeed(seedRandomGenerator.nextLong());
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
        this.socialRandomGenerator.setSeed(seedRandomGenerator.nextLong());
    }

    /**
     * Accessor for the random number generator used to seed the other random number generators.
     *
     * @return The {@link java.util.Random} generator used to seed the other random number generators.
     */
    public Random getSeedRandomGenerator() {
        return seedRandomGenerator;
    }

    /** 
     * Sets the random number generator used to seed the other random number generators. 
     * This ensures that the random number generators are not seeded with the same or simular values due to the small time delta between initialising them.
     * The default is {@link java.util.Random}.
     *
     * @param seedRandomGenerator The {@link java.util.Random} generator used to seed the other ranodm generators.
     */
    public void setSeedRandomGenerator(Random seedRandomGenerator) {
        this.seedRandomGenerator = seedRandomGenerator;
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
    public double getVmax() {
        return vmax;
    }
    
    /**
     * Sets the maximum absolute velocity permitted in any dimension. The default is Double.MAX_VALUE. That is, vmax is not used by default.
     *
     * @param vmax The maximum velocity.
     */
    public void setVmax(double vmax) {
        this.vmax = vmax;
    }
    
    public double[] getContribution() {
        return getBestParticle().getBestPosition();
    }

    public double getContributionFitness() {
        return getBestParticle().getBestFitness();
    }
    
    public void updateContributionFitness(double fitness) {
        getBestParticle().setBestFitness(fitness);
    }
    
    public int getFitnessEvaluations() {
        return fitnessEvaluations;
    }
    
    public double getSolutionFitness() {
        return getBestParticle().getBestFitness();
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
    
    private Topology topology;
    private OptimisationProblem problem;
    private Random initialisationRandomGenerator;

    private Random seedRandomGenerator;
    private Random cognitiveRandomGenerator;
    private Random socialRandomGenerator;
    
    private double cognitiveAcceleration;
    private double socialAcceleration;
    private double inertia;
    private double vmax;
    
    private Particle bestParticle;
    
    private int fitnessEvaluations;
    
    private Class particleClass;
    
    private DissipativeStep dissipativeStep;
}
