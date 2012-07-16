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
package net.sourceforge.cilib.pso.hpso.detectionstrategies;

import net.sourceforge.cilib.entity.Particle;

/**
 * An interface to detect when an {@link Entity} should change its behavior.
 */
public interface BehaviorChangeTriggerDetectionStrategy {
    /**
     * Detect whether some condition holds that should prompt an entity to
     * change its behavior.
     *
     * @param entity The entity to inspect.
     * @return True if the entity should change its behavior. False otherwise.
     */
    boolean detect(Particle entity);

    /**
     * Clone the current {@link BehaviorChangeTriggerDetectionStrategy}.
     *
     * @return A clone of this {@link BehaviorChangeTriggerDetectionStrategy}.
     */
    BehaviorChangeTriggerDetectionStrategy getClone();
}
