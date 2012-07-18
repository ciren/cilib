/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * M. Senthil Arumugam, M.V.C. Rao, "On th eimproved performances of the particle 
 * swarm optimization algorithms with dapative parameters, cross-over operators 
 * and root mean square (RMS) variants for computing optimal control of a class 
 * of hybrid systems", Applied Soft Computing, vol 8, pp 324--336, 2008, 
 * doi:10.1016/j.asoc.2007.01.010
 * </p>
 */
public class GlobalLocalBestVelocityProvider implements VelocityProvider {
    
    private ControlParameter acceleration;
    private ControlParameter inertia;
    private ControlParameter random;
    
    public GlobalLocalBestVelocityProvider() {
        this.inertia = null;
        this.acceleration = null;
        this.random = new RandomControlParameter(new UniformDistribution());
    }
    
    public GlobalLocalBestVelocityProvider(GlobalLocalBestVelocityProvider copy) {
        this.random = copy.random.getClone();
        
        if (copy.acceleration != null) {
            this.acceleration = copy.acceleration.getClone();
        }
        
        if (copy.inertia != null) {
            this.inertia = copy.inertia.getClone();
        }
    }

    @Override
    public VelocityProvider getClone() {
        return new GlobalLocalBestVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.get();
        Topology<Particle> topology = (Topology<Particle>) algorithm.getTopology();
        Particle gBestParticle = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());
        double accValue;
        double inertiaValue;
        
        if (acceleration == null) {
            accValue = 1.0 + gBestParticle.getBestFitness().getValue() / particle.getBestFitness().getValue();
        } else {
            accValue = acceleration.getParameter();
        }
        
        if (inertia == null) {
            double average = 0;
            for(Particle p : topology) {
                average += p.getBestFitness().getValue();
            }

            average /= topology.size();

            inertiaValue = 1.1 - gBestParticle.getBestFitness().getValue() / average;
        } else {
            inertiaValue = inertia.getParameter();
        }
        
        Vector vel = (Vector) particle.getVelocity();
        Vector pBest = (Vector) particle.getBestPosition();
        Vector pos = (Vector) particle.getCandidateSolution();
        Vector gBest = (Vector) gBestParticle.getBestPosition();
        
        return vel.multiply(inertiaValue).plus(pBest.plus(gBest).plus(pos.multiply(-2.0)).multiply(accValue * random.getParameter()));
    }

    public void setRandom(ControlParameter random) {
        this.random = random;
    }

    public ControlParameter getRandom() {
        return random;
    }

    public void setInertia(ControlParameter inertia) {
        this.inertia = inertia;
    }

    public ControlParameter getInertia() {
        return inertia;
    }

    public void setAcceleration(ControlParameter acceleration) {
        this.acceleration = acceleration;
    }

    public ControlParameter getAcceleration() {
        return acceleration;
    }    
}
