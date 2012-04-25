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
package net.sourceforge.cilib.util.functions;

import fj.F;
import fj.F2;
import net.sourceforge.cilib.entity.Particle;
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
