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
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Update the personal best of the particle. Updates are done in a variety
 * of manners, refer to implementations for details.
 *
 */
public interface PersonalBestUpdateStrategy extends Cloneable {

    /**
     * {@inheritDoc}
     */
    PersonalBestUpdateStrategy getClone();

    /**
     * Update the personal best of the provided {@link Particle}.
     * @param particle The particle to update.
     */
    void updatePersonalBest(Particle particle);
    
    void updateParametizedPersonalBest(ParameterizedParticle particle);
}
