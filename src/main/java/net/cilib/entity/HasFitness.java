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

import fj.data.Option;

/**
 * Indicate that the instance does maintain a fitness value.
 *
 * @author gpampara
 */
public interface HasFitness {

    /**
     * Gets the current fitness value. The fitness value may or may not exist.
     * Valid fitness values are provided by an instance of {@link Option#some(Object)}
     * and invalid fitness values are given by {@link fj.data.Option#none()}.
     *
     * @return the fitness value.
     */
    Option<Double> fitness();
}
