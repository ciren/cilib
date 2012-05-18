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
package net.sourceforge.cilib.pso.hpso;

import java.util.List;
import net.sourceforge.cilib.measurement.multiple.AdaptiveHPSOBehaviorProfileMeasurement;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Interface that heterogeneous iteration strategies should implement so they can be measured
 * using {@link AdaptiveHPSOBehaviorProfileMeasurement}
 */
public interface HeterogeneousIterationStrategy {
    /**
     * Add a {@link ParticleBehavior} to the behavior pool.
     * @param behavior The {@link ParticleBehavior} to add to the behavior pool.
     */
    public void addBehavior(ParticleBehavior behavior);
    
    /**
     * Set the {@link ParticleBehavior} pool.
     * @param pool A {@link List} of {@link ParticleBehavior} objects.
     */
    public void setBehaviorPool(List<ParticleBehavior> pool);
    
    /**
     * Get the current behavior pool.
     * @return The current {@link List} of {@link ParticleBehavior} objects.
     */
    public List<ParticleBehavior> getBehaviorPool();
}
