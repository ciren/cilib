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

import net.cilib.collection.Topology;
import net.cilib.inject.annotation.Global;
import net.cilib.inject.annotation.Local;
import net.cilib.entity.Particle;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.cilib.collection.Seq;
import net.cilib.collection.immutable.Velocity;
import net.cilib.entity.Entity;
import net.cilib.inject.annotation.Unique;
import static net.cilib.collection.SeqView.*;

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

    private final Supplier<Double> r1c1;
    private final Supplier<Double> r2c2;
    private final Guide localGuide;
    private final Guide globalGuide;

    @Inject
    public StandardVelocityProvider(
            @Unique final Supplier<Double> r1Supplier,
            @Unique final Supplier<Double> r2Supplier,
            @Named("acceleration") final Supplier<Double> constant1,
            @Named("acceleration") final Supplier<Double> constant2,
            @Local Guide localGuide, @Global Guide globalGuide) {

        this.r1c1 = new Supplier<Double>() {

            @Override
            public Double get() {
                return constant1.get() * r1Supplier.get();
            }
        };

        this.r2c2 = new Supplier<Double>() {

            @Override
            public Double get() {
                return constant2.get() * r2Supplier.get();
            }
        };
        this.localGuide = localGuide;
        this.globalGuide = globalGuide;
    }

    /**
     * @param particle to base the calculation of the {@link Velocity} on.
     * @return
     * @todo: The guides for the equation should be provided as parameters.
     */
    @Override
    public Velocity f(Particle a, Topology b) {
        Entity n = globalGuide.f(a, b).valueE("Cannot obtain global guide.");
        Entity p = localGuide.f(a, b).valueE("Cannot obtain local guide.");

        Seq cognitive = multiply(r1c1, p.solution().subtract(a.solution()));
        Seq social = multiply(r2c2, n.solution().subtract(a.solution()));

        return Velocity.copyOf(a.velocity().plus(cognitive).plus(social));
    }
}
