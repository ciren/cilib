/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
