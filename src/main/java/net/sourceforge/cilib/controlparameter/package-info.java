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
/**
 * <p>A {@linkplain ControlParameter} is a generalised parameter type within CIlib.</p>
 * <p>
 * {@linkplain Algorithm}s require parameters in order to alter and configure the
 * manner in which they operate. These parameters could represent the number of
 * {@linkplain Individual}s within a Genetic Algorithm, or even the degree of mutation
 * which decreases over time as the {@linkplain Algorithm} runs to completion.
 * </p>
 * <p>
 * {@linkplain ControlParameter} instances provide a standard interface for these
 * parameter types, which can be updated over the course of an algorithm run.
 * </p>
 * <p>
 * Substituting differing {@linkplain ControlParameter} instances, therefore, results
 * in a simple modification to the desired {@linkplain Algorithm} without any code
 * changes.
 * </p>
 */
package net.sourceforge.cilib.controlparameter;
