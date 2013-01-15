/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching;

import fj.F;
import fj.F2;
import fj.Ordering;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;

public final class VectorBasedFunctions {

    private VectorBasedFunctions() {}

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

    public static F<Particle, F<Particle, Ordering>> sortByDistance(final Particle nBest, final DistanceMeasure distance) {
        return new F2<Particle, Particle, Ordering>() {
            @Override
            public Ordering f(Particle a, Particle b) {
                double aDist = distance.distance(a.getCandidateSolution(), nBest.getBestPosition());
                double bDist = distance.distance(b.getCandidateSolution(), nBest.getBestPosition());

                return Ordering.values()[Double.compare(aDist, bDist) + 1];
            }
        }.curry();
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
}
