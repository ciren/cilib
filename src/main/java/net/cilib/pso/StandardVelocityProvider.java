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
package net.cilib.pso;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fj.P1;
import fj.data.List;
import net.cilib.collection.Topology;
import net.cilib.entity.Entity;
import net.cilib.entity.Particle;
import net.cilib.inject.annotation.Global;
import net.cilib.inject.annotation.Local;
import net.cilib.inject.annotation.Unique;
import static net.cilib.predef.Predef.subtract;
import static net.cilib.predef.Predef.multiply;
import static net.cilib.predef.Predef.plus;

/**
 * Velocity provider implementing the canonical velocity update equation
 * as defined by Kennedy and Eberhart [1].
 * <p/>
 * A particle is defined to always do two simple tasks:
 * 1. Move towards it's own personal best experience, and
 * 2. Move towards the position of the current neighbourhood best
 * <p/>
 * <pre>
 * [1]
 * </pre>
 *
 * @author gpampara
 */
public final class StandardVelocityProvider extends VelocityProvider {

    private final P1<Double> r1c1;
    private final P1<Double> r2c2;
    private final Guide localGuide;
    private final Guide globalGuide;
    private final P1<Double> inertia;

    @Inject
    public StandardVelocityProvider(
            @Named("inertia") final P1<Double> inertia,
            @Unique final P1<Double> r1Supplier,
            @Unique final P1<Double> r2Supplier,
            @Named("acceleration") final P1<Double> constant1,
            @Named("acceleration") final P1<Double> constant2,
            @Local Guide localGuide, @Global Guide globalGuide) {

        this.inertia = inertia;
        this.r1c1 = new P1<Double>() {
            @Override
            public Double _1() {
                return constant1._1() * r1Supplier._1();
            }
        };

        this.r2c2 = new P1<Double>() {
            @Override
            public Double _1() {
                return constant2._1() * r2Supplier._1();
            }
        };
        this.localGuide = localGuide;
        this.globalGuide = globalGuide;
    }

    /**
     * @param a particle to base the calculation of the velocity on.
     * @return a new velocity
     * @todo: The guides for the equation should be provided as parameters.
     */
    @Override
    public List<Double> f(Particle a, Topology b) {
        Entity p = localGuide.f(a, b);
        Entity n = globalGuide.f(a, b);

        List<Double> cognitive = multiply(r1c1, subtract(p.solution(), a.solution()));
        List<Double> social = multiply(r2c2, subtract(n.solution(), a.solution()));

        return plus(multiply(inertia, a.velocity()), plus(cognitive, social));
    }
}
