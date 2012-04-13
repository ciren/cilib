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
import fj.function.Integers;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.niching.JoinedTopologyProvider;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.niching.TopologyProvider;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

public class VectorBasedNicheCreationStrategy extends NicheCreationStrategy {
    private DistanceMeasure distanceMeasure;
    private TopologyProvider topologyProvider;
    private ControlParameter minSwarmSize;
    private PopulationBasedAlgorithm subSwarm;
    private ParticleBehavior behavior;

    public VectorBasedNicheCreationStrategy() {
        distanceMeasure = new EuclideanDistanceMeasure();
        topologyProvider = new JoinedTopologyProvider();
        minSwarmSize = ConstantControlParameter.of(3.0);
        behavior = new ParticleBehavior();
        subSwarm = new PSO();
    }

    public static F<Particle, Integer> dot(final Particle nBest) {
        return new F<Particle, Integer>() {
            @Override
            public Integer f(Particle p) {
                Vector gBest = (Vector) nBest.getBestPosition();
                Vector vg = gBest.subtract((Vector) p.getCandidateSolution());
                Vector vp = ((Vector) p.getBestPosition()).subtract((Vector) p.getCandidateSolution());

                return (int) vp.dot(vg);
            }
        };
    }

    public static F2<Particle, Particle, Ordering> sortByDistance(final Particle nBest, final DistanceMeasure distance) {
        return new F2<Particle, Particle, Ordering>() {
            @Override
            public Ordering f(Particle a, Particle b) {
                double aDist = distance.distance(a.getCandidateSolution(), nBest.getBestPosition());
                double bDist = distance.distance(b.getCandidateSolution(), nBest.getBestPosition());

                switch(Double.compare(aDist, bDist)) {
                    case -1:
                        return Ordering.GT;
                    case 1:
                        return Ordering.LT;
                    default:
                        return Ordering.EQ;
                }
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
    public NichingSwarms f(NichingSwarms a, Entity b) {
        Particle nBest = (Particle) Topologies.getBestEntity(a._1().getTopology(), new SocialBestFitnessComparator());
        List<Particle> swarm = (List<Particle>) topologyProvider.f(a);

        Particle closest = swarm
                .filter(dot(nBest).andThen(Integers.ltZero))
                .delete(nBest, Equal.equal(equalParticle.curry()))
                .minimum(Ord.ord(sortByDistance(nBest, distanceMeasure).curry()));

        double nRadius = distanceMeasure.distance(closest.getCandidateSolution(), nBest.getBestPosition());

        List<Particle> newTopology = swarm.filter(filter(distanceMeasure, nBest, nRadius));

        if (newTopology.length() < 3) {
            for (int i = 0; i < minSwarmSize.getParameter(); i++) {
                Particle newP = nBest.getClone();
                newP.reinitialise();
                newTopology.cons(newP);
            }
        }

        PopulationBasedAlgorithm newSubswarm = subSwarm.getClone();
        newSubswarm.getTopology().clear();
        ((Topology<Particle>) newSubswarm.getTopology()).addAll(newTopology.toCollection());

        PopulationBasedAlgorithm newMainSwarm = a._1().getClone();
        newMainSwarm.getTopology().clear();
        for(Entity e : a._1().getTopology()) {
            Particle p = (Particle) e;
            if (!newTopology.exists(equalParticle.f(p))) {
                ((Topology<Entity>) newMainSwarm.getTopology()).add(e.getClone());
            }
        }

        return NichingSwarms.of(newMainSwarm, a._2().cons(newSubswarm));
   }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
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

    public PopulationBasedAlgorithm getSubSwarm() {
        return subSwarm;
    }

    public void setSubSwarm(PopulationBasedAlgorithm subSwarm) {
        this.subSwarm = subSwarm;
    }

    public ParticleBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(ParticleBehavior behavior) {
        this.behavior = behavior;
    }
}
