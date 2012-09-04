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
package net.sourceforge.cilib.util.functions;

import fj.F;
import fj.F2;
import fj.Unit;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.MemoryBasedEntity;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.StructuredType;

public final class Entities {
    public static <E extends Entity> F<E, StructuredType> getCandidateSolution() {
        return new F<E, StructuredType>() {
            @Override
            public StructuredType f(E a) {
                return a.getCandidateSolution();
            }
        };
    }

    public static <E extends Entity> F2<StructuredType, E, E> setCandidateSolution() {
        return new F2<StructuredType, E, E>() {
            @Override
            public E f(StructuredType a, E b) {
                b.setCandidateSolution(a);
                return b;
            }
        };
    }

    public static <E extends Entity> F<E, E> evaluate() {
        return new F<E, E>() {
            @Override
            public E f(E a) {
                a.calculateFitness();
                return a;
            }
        };
    }

    public static <E extends Entity> F<E, Fitness> getFitness() {
        return new F<E, Fitness>() {
            @Override
            public Fitness f(E a) {
                return a.getFitness();
            }
        };
    }

    public static <E extends Entity> F<E, Fitness> getBestFitness() {
        return new F<E, Fitness>() {
            @Override
            public Fitness f(E a) {
                return a.getBestFitness();
            }
        };
    }

    public static <E extends Entity> F<E, E> reinitialise() {
        return new F<E, E>() {
            @Override
            public E f(E a) {
                a.reinitialise();
                return a;
            }
        };
    }

    public static <E extends Entity> F2<? extends OptimisationProblem, E, E> initialise() {
        return new F2<OptimisationProblem, E, E>() {
            @Override
            public E f(OptimisationProblem a, E b) {
                b.initialise(a);
                return b;
            }
        };
    }

    public static <E extends MemoryBasedEntity> F<E, StructuredType> getBestPosition() {
        return new F<E, StructuredType>() {
            @Override
            public StructuredType f(E a) {
                return a.getBestPosition();
            }
        };
    }

    public static <E extends SocialEntity> F<E, Fitness> getSocialFitness() {
        return new F<E, Fitness>() {
            @Override
            public Fitness f(E a) {
                return a.getSocialFitness();
            }
        };
    }
}
