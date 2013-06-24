/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import fj.F;
import fj.F2;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

public final class Particles {
    public static <P extends Particle> F<P, Vector> getPosition() {
        return new F<P, Vector>() {
            @Override
            public Vector f(P a) {
                return (Vector) a.getPosition();
            }
        };
    }

    public static <P extends Particle> F<P, Vector> getVelocity() {
        return new F<P, Vector>() {
            @Override
            public Vector f(P a) {
                return (Vector) a.getVelocity();
            }
        };
    }

    public static <P extends Particle> F<P, Particle> getNeighbourhoodBest() {
        return new F<P, Particle>() {
            @Override
            public Particle f(P a) {
                return a.getNeighbourhoodBest();
            }
        };
    }

    public static <P extends Particle> F2<P, P, P> setNeighbourhoodBest() {
        return new F2<P, P, P>() {
            @Override
            public P f(P a, P b) {
                b.setNeighbourhoodBest(a);
                return b;
            }
        };
    }

    public static <P extends Particle> F2<P, StructuredType, P> updatePosition() {
        return new F2<P, StructuredType, P>() {
            @Override
            public P f(P a, StructuredType b) {
                a.updatePosition(b);
                return a;
            }
        };
    }

    public static <P extends Particle> F2<P, StructuredType, P> updateVelocity() {
        return new F2<P, StructuredType, P>() {
            @Override
            public P f(P a, StructuredType b) {
                a.updatePosition(b);
                return a;
            }
        };
    }
}
