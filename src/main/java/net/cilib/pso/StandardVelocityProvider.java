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
import net.cilib.entity.LinearSeq;
import net.cilib.entity.MutableSeq;
import net.cilib.entity.Particle;
import net.cilib.entity.Velocity;

/**
 *
 * @author gpampara
 */
public final class StandardVelocityProvider implements VelocityProvider {

    private final Guide localGuide;
    private final Guide globalGuide;

    @Inject
    public StandardVelocityProvider(@Named("local") Guide localGuide,
        @Named("global") Guide globalGuide) {
        this.localGuide = localGuide;
        this.globalGuide = globalGuide;
    }

    @Override
    public Velocity create(Particle particle) {
        LinearSeq local = localGuide.of(particle);
        LinearSeq global = globalGuide.of(particle);

        MutableSeq cognitive = local.toMutableSeq().subtract(particle.solution()).multiply(2);
        MutableSeq social = global.toMutableSeq().subtract(particle.solution()).multiply(3);

        return Velocity.copyOf(particle.velocity().toMutableSeq().plus(cognitive).plus(social).toArray());
    }
}
