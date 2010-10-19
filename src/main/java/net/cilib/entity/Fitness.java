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

/**
 * Evaluation representation of a {@code CandidateSolution}.
 * @author gpampara
 */
public interface Fitness extends Comparable<Fitness> {

    /**
     * Get internally maintained value.
     * @return maintained value.
     */
    double value();

    /**
     * Determine if the current {@code Fitness} is more fit or better than
     * the given {@code Fitness}.
     * @param that {@code Fitness} to compare
     * @return {@code true} if the current {@code Fitness} is more fit,
     *         {@code false} otherwise.
     */
    boolean isMoreFitThan(Fitness that);
}
