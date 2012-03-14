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
package net.sourceforge.cilib.pso.niching.creation;

import fj.P;
import fj.P2;
import java.util.Arrays;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.ClosestEntityVisitor;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.niching.merging.SingleSwarmMergeStrategy;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.velocityprovider.GCVelocityProvider;

/**
 * <p>
 * Create a set of niching locations, based on a provided set of identified
 * niching entities.
 * </p>
 * <p>
 * For each newly discovered niching location, a new sub-swarm is creates that will
 * maintain the niche. For the case of the PSO, the niching particle and the closest
 * particle to the identified particle are gropuped into a niche. Sub-swarms will always
 * then have at least two particles.
 * </p>
 * <p>
 * The rational for two particles is that a particle is a social entity and as a result
 * needs to share information. Ensuring that there are at least two particles within
 * a sub-swarm will enable the velocity update equation associated with the particle
 * to still operate.
 * </p>
 */
public class StandardNicheCreationStrategy extends NicheCreationStrategy {
    
    private PopulationBasedAlgorithm subSwarm;
    private ParticleBehavior behavior;
    
    /**
     * Default constructor.
     */
    public StandardNicheCreationStrategy() {
        this.subSwarm = new PSO();
        ((SynchronousIterationStrategy) ((PSO) this.subSwarm).getIterationStrategy())
                .setBoundaryConstraint(new ReinitialisationBoundary());
        
        this.behavior = new ParticleBehavior();
        this.behavior.setVelocityProvider(new GCVelocityProvider());
    }

    @Override
    public P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f(PopulationBasedAlgorithm a, Entity b) {
        ClosestEntityVisitor closestEntityVisitor = new ClosestEntityVisitor();
        closestEntityVisitor.setTargetEntity(b);
        a.accept(closestEntityVisitor);
        
        Particle nicheMainParticle = (Particle) b.getClone();
        Particle nicheClosestParticle = (Particle) closestEntityVisitor.getResult().getClone();
        
        nicheMainParticle.setNeighbourhoodBest(nicheMainParticle);
        nicheClosestParticle.setNeighbourhoodBest(nicheMainParticle);
        
        nicheMainParticle.setParticleBehavior(behavior.getClone());
        nicheClosestParticle.setParticleBehavior(behavior.getClone());
        
        PopulationBasedAlgorithm newSubSwarm = subSwarm.getClone();
        newSubSwarm.setOptimisationProblem(a.getOptimisationProblem());
        ((Topology<Particle>) newSubSwarm.getTopology()).addAll(Arrays.asList(nicheMainParticle, nicheClosestParticle));
        
        PopulationBasedAlgorithm newMainSwarm = a.getClone();
        newMainSwarm.getTopology().clear();
        for(Entity e : a.getTopology()) {
            if (!e.equals(b) && !e.equals(closestEntityVisitor.getResult())) {
                ((Topology<Entity>) newMainSwarm.getTopology()).add(e.getClone());
            }
        }
        
        return P.p(new SingleSwarmMergeStrategy().f(newMainSwarm, null), newSubSwarm);
    }

    public void setSubSwarm(PopulationBasedAlgorithm subSwarm) {
        this.subSwarm = subSwarm;
    }

    public PopulationBasedAlgorithm getSubSwarm() {
        return subSwarm;
    }

    public void setBehavior(ParticleBehavior behavior) {
        this.behavior = behavior;
    }

    public ParticleBehavior getBehavior() {
        return behavior;
    }
}
