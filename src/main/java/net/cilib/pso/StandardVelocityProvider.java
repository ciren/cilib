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

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.cilib.inject.annotation.Global;
import net.cilib.inject.annotation.Local;
import net.cilib.entity.LinearSeq;
import net.cilib.entity.MutableSeq;
import net.cilib.entity.Particle;
import net.cilib.entity.Velocity;
import net.cilib.inject.annotation.Unique;

/**
 *
 * @author gpampara
 */
public final class StandardVelocityProvider implements VelocityProvider {

    private final Guide localGuide;
    private final Guide globalGuide;
    private final Supplier<Double> r1Supplier;
    private final Supplier<Double> r2Supplier;
    private final Supplier<Double> constant1;
    private final Supplier<Double> constant2;

    @Inject
    public StandardVelocityProvider(@Local Guide localGuide,
            @Global Guide globalGuide,
            @Unique Supplier<Double> r1Supplier,
            @Unique Supplier<Double> r2Supplier,
            @Named("acceleration") Supplier<Double> constant1,
            @Named("acceleration") Supplier<Double> constant2) {
        this.localGuide = localGuide;
        this.globalGuide = globalGuide;
        this.r1Supplier = r1Supplier;
        this.r2Supplier = r2Supplier;
        this.constant1 = constant1;
        this.constant2 = constant2;
    }

    @Override
    public Velocity create(Particle particle) {
        LinearSeq local = localGuide.of(particle).solution();
        LinearSeq global = globalGuide.of(particle).solution();

        MutableSeq cognitive = local.toMutableSeq().subtract(particle.solution()).multiply(constant1).multiply(r1Supplier);
        MutableSeq social = global.toMutableSeq().subtract(particle.solution()).multiply(constant2).multiply(r2Supplier);

        return Velocity.copyOf(particle.velocity().toMutableSeq().plus(cognitive).plus(social).toArray());
    }
}
