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
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 *
 */
public class VectorBasedNicheDetection extends NicheDetection {
    private DistanceMeasure distanceMeasure;

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

    public static Equal<Particle> equalParticle = Equal.equal(new F2<Particle, Particle, Boolean>() {
        @Override
        public Boolean f(Particle a, Particle b) {
            return a.getBestPosition().equals(b.getBestPosition())
                    && a.getBestFitness().equals(b.getBestFitness())
                    && a.getCandidateSolution().equals(b.getCandidateSolution())
                    && a.getFitness().equals(b.getFitness());
        }
    }.curry());
    
    @Override
    public Boolean f(PopulationBasedAlgorithm a, Entity b) {
        Particle p = (Particle) b;
        Particle nBest = (Particle) Topologies.getBestEntity(a.getTopology(), new SocialBestFitnessComparator());
        double pRadius = distanceMeasure.distance(p.getCandidateSolution(), nBest.getBestPosition());
        Particle closest = List.iterableList((Topology<Particle>) a.getTopology())
                .filter(dot(nBest).andThen(Integers.ltZero))
                .delete(nBest, equalParticle)
                .minimum(Ord.ord(sortByDistance(nBest, distanceMeasure).curry()));
        double nRadius = distanceMeasure.distance(closest.getCandidateSolution(), nBest.getBestPosition());

        return pRadius < nRadius && dot(nBest).f(p) > 0;
    }
}
