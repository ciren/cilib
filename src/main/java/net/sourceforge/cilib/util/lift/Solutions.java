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
package net.sourceforge.cilib.util.lift;

import fj.F;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public final class Solutions {
    public static <S extends OptimisationSolution> F<S, Vector> getPosition() {
        return new F<S, Vector>() {
            @Override
            public Vector f(S a) {
                return (Vector) a.getPosition();
            }
        };
    }

    public static <S extends OptimisationSolution> F<S, Fitness> getFitness() {
        return new F<S, Fitness>() {
            @Override
            public Fitness f(S a) {
                return a.getFitness();
            }
        };
    }
}
