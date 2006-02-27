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
 *
 * This class is an implementation of the standard PSO algorithm, references:
 *      1.  J. Kennedy and R.C. Eberhart, "Particle swarm optimization,"
 *          in Proceedings of IEEE International Conference on Neural Networks,
 *          vol. IV, (Perth Australia), pp. 1942-1948, 1995.
 *      2.  R.C. Eberhart and J. Kennedy, "A new optimizer using particle swarm theory,"
 *          in Proceedings of the Sixth International Symposium on Micro Machine and Human Science,
 *          (Nagoya, Japan), pp. 39-43, 1995.
 *      3.  Y. Shi amd R.C. Eberhart, "A modified particle swarm optimizer,"
 *          in Proceedings of the IEEE Congress on Evolutionary Computation,
 *          (Anchorage, Alaska), pp. 69-73, May 1998.
 *  
 */

package net.sourceforge.cilib.PSO;

import java.lang.*;
import java.util.*;
import net.sourceforge.cilib.Algorithm.*;
import net.sourceforge.cilib.Problem.*;
import net.sourceforge.cilib.Random.*;

/**
 *
 * @author  espeer
 */
public class PSO extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {
    
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
    }
    
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
    
    public void performIteration() {
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
        }
    }
    
    
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = problem;
    }
    
    public OptimisationProblem getOptimisationProblem() {
        return problem;
    }
    
    public double[] getSolution() {
        return getBestParticle().getBestPosition();
    }
    
    public Particle getBestParticle() {
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
    
    public void setParticleClass(Class particleClass) {
        this.particleClass = particleClass;
    }
    
    public Class getParticleClass() {
        return particleClass;
    }
    
    public void setTopology(Topology topology) {
        this.topology = topology;
    }
    
    public Topology getTopology() {
        return topology;
    }

    public void setInitialisationRandomGenerator(Random initialisationRandomGenerator) {
        this.initialisationRandomGenerator = initialisationRandomGenerator;
        this.initialisationRandomGenerator.setSeed(seedRandomGenerator.nextLong());
    }
    
    public Random getInitialisationRandomGenerator() {
        return initialisationRandomGenerator;
    }
    
    protected double evaluateFitness(Particle particle) {
        ++fitnessEvaluations;
        return problem.getFitness(particle.getPosition());
    }
    
    public Random getCognitiveRandomGenerator() {
        return cognitiveRandomGenerator;
    }
    
    public void setCognitiveRandomGenerator(Random cognitiveRandomGenerator) {
        this.cognitiveRandomGenerator = cognitiveRandomGenerator;
        this.cognitiveRandomGenerator.setSeed(seedRandomGenerator.nextLong());
    }
    
    public Random getSocialRandomGenerator() {
        return socialRandomGenerator;
    }
    
    public void setSocialRandomGenerator(Random socialRandomGenerator) {
        this.socialRandomGenerator = socialRandomGenerator;
        this.socialRandomGenerator.setSeed(seedRandomGenerator.nextLong());
    }

    public Random getSeedRandomGenerator() {
        return seedRandomGenerator;
    }

    public void setSeedRandomGenerator(Random seedRandomGenerator) {
        this.seedRandomGenerator = seedRandomGenerator;
    }
    
    public double getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }
    
    public void setCognitiveAcceleration(double cognitiveAcceleration) {
        this.cognitiveAcceleration = cognitiveAcceleration;
    }
    
    public double getSocialAcceleration() {
        return socialAcceleration;
    }
    
    public void setSocialAcceleration(double socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }
    
    public double getInertia() {
        return inertia;
    }
    
    public void setInertia(double inertia) {
        this.inertia = inertia;
    }
    
    public double getVmax() {
        return vmax;
    }
    
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
}
