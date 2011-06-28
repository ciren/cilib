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

import com.google.common.base.Preconditions;
import fj.F;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.entity.Entity;
import net.cilib.entity.HasMemory;
import net.cilib.entity.PartialEntity;

/**
 * this class needs to be better... The implementation is not really all that
 * nice - the instanceof test for example.
 *
 * @author gpampara
 */
public final class PersonalBest extends Guide {

    /**
     * Obtain the memory of the provided {@code Entity}. If the provided
     * {@code Entity} does not maintain a {@linkplain HasMemory memory},
     * a zeroed {@code CandidateSolution} is then returned.
     *
     * @param target
     * @return
     */
    @Override
    public Entity f(Entity target, Topology topology) {
        Preconditions.checkNotNull(target);

        if (target instanceof HasMemory) {
            HasMemory memory = (HasMemory) target;
            return new PartialEntity(memory.memory());
        }

        return new PartialEntity(target.solution().map(new F<Double, Double>() {
            @Override
            public Double f(Double a) {
                return 0.0;
            }
        }));
    }
}
