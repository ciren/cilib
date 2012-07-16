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
package net.sourceforge.cilib.util.selection.weighting;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.problem.Fitness;

/**
 * Obtain the social best fitness value from a {@link SocialEntity}.
 * @param <E> The type that is both an {@code Entity} and a {@code SocialEntity}.
 *            An example of such an entity is the {@link net.sourceforge.cilib.entity.Particle}.
 */
public class SocialBestFitness<E extends Entity & SocialEntity> implements EntityFitness<E> {

    /**
     * {@inheritDoc}
     * Obtains the social based fitness from the {@link SocialEntity}.
     */
    @Override
    public Fitness getFitness(E entity) {
        return entity.getSocialFitness();
    }

}
