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

import com.google.inject.Inject;
import net.cilib.problem.Problem;

/**
 *
 * @author gpampara
 */
public class EntityFinalizer {
    private final EntityFactory factory;
    private final Problem problem;

    @Inject
    public EntityFinalizer(EntityFactory factory, Problem problem) {
        this.factory = factory;
        this.problem = problem;
    }

    public Entity finalize(Entity element) {
        Fitness fitness = problem.fitnessOf(element);
        return factory.create(element.solution(), fitness);
    }
}
