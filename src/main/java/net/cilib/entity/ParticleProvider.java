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
package net.cilib.entity;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 *
 * @author gpampara
 */
public final class ParticleProvider implements Provider<Particle> {

    private final FitnessProvider fitnessProvider;
    private CandidateSolution position;
    private Velocity velocity;

    @Inject
    public ParticleProvider(FitnessProvider fitnessProvider) {
        this.fitnessProvider = fitnessProvider;
    }

    public ParticleProvider position(CandidateSolution position) {
        this.position = CandidateSolution.copyOf(position);
        return this;
    }

    public ParticleProvider velocity(Velocity velocity) {
        this.velocity = Velocity.copyOf(velocity.toArray());
        return this;
    }

    @Override
    public Particle get() {
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(velocity);

        try {
            return new Particle(position, position, velocity,
                fitnessProvider.finalize(position));
        } finally {
            this.position = null;
            this.velocity = null;
        }
    }
}
