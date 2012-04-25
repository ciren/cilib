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
package net.sourceforge.cilib.niching.creation;

import fj.*;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.niching.utils.JoinedTopologyProvider;
import net.sourceforge.cilib.niching.utils.TopologyProvider;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import net.sourceforge.cilib.util.functions.Populations;

public class VectorBasedNicheCreationStrategy extends NicheCreationStrategy {
    private DistanceMeasure distanceMeasure;
    private TopologyProvider topologyProvider;
    private ControlParameter minSwarmSize;

    public VectorBasedNicheCreationStrategy() {
        distanceMeasure = new EuclideanDistanceMeasure();
        topologyProvider = new JoinedTopologyProvider();
        minSwarmSize = ConstantControlParameter.of(3.0);
        swarmBehavior = new ParticleBehavior();
        swarmBehavior.setVelocityProvider(new StandardVelocityProvider(ConstantControlParameter.of(0.8), ConstantControlParameter.of(1.0), ConstantControlParameter.of(1.0),
                new MersenneTwister(), new MersenneTwister()));
        swarmType = new PSO();
        swarmType.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 500));
    }

    public static F<Particle, Double> dot(final Particle nBest) {
        return new F<Particle, Double>() {
            @Override
            public Double f(Particle p) {
                Vector gBest = (Vector) nBest.getBestPosition();
                Vector vg = gBest.subtract((Vector) p.getCandidateSolution());
                Vector vp = ((Vector) p.getBestPosition()).subtract((Vector) p.getCandidateSolution());

                return vp.dot(vg);
            }
        };
    }

    public static final F<Double, Boolean> ltZero = new F<Double, Boolean>() {
        @Override
        public Boolean f(final Double i) {
            return Double.compare(i, 0) < 0;
        }
    };

    public static F2<Particle, Particle, Ordering> sortByDistance(final Particle nBest, final DistanceMeasure distance) {
        return new F2<Particle, Particle, Ordering>() {
            @Override
            public Ordering f(Particle a, Particle b) {
                double aDist = distance.distance(a.getCandidateSolution(), nBest.getBestPosition());
                double bDist = distance.distance(b.getCandidateSolution(), nBest.getBestPosition());

                return Ordering.values()[-Double.compare(aDist, bDist) + 1];
            }
        };
    }

    public static F2<Particle, Particle, Boolean> equalParticle = new F2<Particle, Particle, Boolean>() {
        @Override
        public Boolean f(Particle a, Particle b) {
            return a.getBestPosition().equals(b.getBestPosition())
                    && a.getBestFitness().equals(b.getBestFitness())
                    && a.getCandidateSolution().equals(b.getCandidateSolution())
                    && a.getFitness().equals(b.getFitness());
        }
    };

    public static F<Particle, Boolean> filter(final DistanceMeasure distanceMeasure, final Particle nBest, final double nRadius) {
        return new F<Particle, Boolean>() {
            @Override
            public Boolean f(Particle p) {                
                double pRadius = distanceMeasure.distance(p.getCandidateSolution(), nBest.getBestPosition());                

                return pRadius < nRadius && dot(nBest).f(p) > 0;
            }
        };
    }
    
    @Override
    public NichingSwarms f(NichingSwarms swarms, Entity b) {
        Particle gBest = (Particle) Topologies.getBestEntity(swarms.getMainSwarm().getTopology(), new SocialBestFitnessComparator());
        List<Particle> newTopology = List.list(gBest);
        List<Particle> swarm = ((List<Particle>) topologyProvider.f(swarms)).delete(gBest, Equal.equal(equalParticle.curry()));

        List<Particle> filteredSwarm = swarm.filter(dot(gBest).andThen(ltZero));
        if(!filteredSwarm.isEmpty()) {
            Particle closest = filteredSwarm.minimum(Ord.ord(sortByDistance(gBest, distanceMeasure).curry()));
            double nRadius = distanceMeasure.distance(closest.getCandidateSolution(), gBest.getCandidateSolution());
            newTopology = newTopology.append(swarm.filter(filter(distanceMeasure, gBest, nRadius)));
        }

        // Add particles until the swarm has at least 3 particles
        for (int i = 0; i < minSwarmSize.getParameter() - newTopology.length(); i++) {
            Particle newP = gBest.getClone();
            newP.reinitialise();
            newTopology = newTopology.cons(newP);
        }

        // Create the new subswarm, set its optimisation problem, add the particles to it
        PopulationBasedAlgorithm newSubswarm = swarmType;
        newSubswarm.setOptimisationProblem(swarms.getMainSwarm().getOptimisationProblem());
        newSubswarm.getTopology().clear();
        ((Topology<Particle>) newSubswarm.getTopology()).addAll(newTopology.toCollection());

        // Remove the subswarms particles from the main swarm
        PopulationBasedAlgorithm newMainSwarm = swarms.getMainSwarm().getClone();
        newMainSwarm.getTopology().clear();
        for(Entity e : swarms.getMainSwarm().getTopology()) {
            Particle p = (Particle) e;

            if (!newTopology.exists(equalParticle.f(p))) {
                ((Topology<Entity>) newMainSwarm.getTopology()).add(e.getClone());
            }
        }

        // Set the subswarm's behavior and return the new swarms
        return NichingSwarms.of(newMainSwarm, swarms.getSubswarms().cons(Populations.enforceTopology(swarmBehavior).f(newSubswarm.getClone())));
   }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    public ControlParameter getMinSwarmSize() {
        return minSwarmSize;
    }

    public void setMinSwarmSize(ControlParameter minSwarmSize) {
        this.minSwarmSize = minSwarmSize;
    }

    public void setTopologyProvider(TopologyProvider topologyProvider) {
        this.topologyProvider = topologyProvider;
    }

    public TopologyProvider getTopologyProvider() {
        return topologyProvider;
    }
}
