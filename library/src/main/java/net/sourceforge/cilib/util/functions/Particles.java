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
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
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

    public static <P extends Particle> F<P, Vector> getGlobalGuide() {
        return new F<P, Vector>() {
            @Override
            public Vector f(P a) {
                return (Vector) a.getGlobalGuide();
            }
        };
    }

    public static <P extends Particle> F<P, Vector> getLocalGuide() {
        return new F<P, Vector>() {
            @Override
            public Vector f(P a) {
                return (Vector) a.getLocalGuide();
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

    public static <P extends Particle> F<P, P> updatePosition() {
        return new F<P, P>() {
            @Override
            public P f(P a) {
                a.updatePosition();
                return a;
            }
        };
    }

    public static <P extends Particle> F<P, P> updateVelocity() {
        return new F<P, P>() {
            @Override
            public P f(P a) {
                a.updatePosition();
                return a;
            }
        };
    }

    public static <P extends Particle> F<P, ? extends VelocityProvider> getVelocityProvider() {
        return new F<P, VelocityProvider>() {
            @Override
            public VelocityProvider f(P a) {
                return a.getVelocityProvider();
            }
        };
    }

    public static <P extends Particle> F<P, ? extends PositionProvider> getPositionProvider() {
        return new F<P, PositionProvider>() {
            @Override
            public PositionProvider f(P a) {
                return a.getPositionProvider();
            }
        };
    }

    public static <P extends Particle> F<P, ParticleBehavior> getParticleBehavior() {
        return new F<P, ParticleBehavior>() {
            @Override
            public ParticleBehavior f(P a) {
                return a.getParticleBehavior();
            }
        };
    }

    public static <P extends Particle> F2<ParticleBehavior, P, P> setParticleBehavior() {
        return new F2<ParticleBehavior, P, P>() {
            @Override
            public P f(ParticleBehavior a, P b) {
                b.setParticleBehavior(a);
                return b;
            }
        };
    }
}
