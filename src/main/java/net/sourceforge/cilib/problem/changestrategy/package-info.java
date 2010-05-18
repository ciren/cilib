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
 * Dynamic environments need to know when it should be applicable to change
 * the problem.
 * <p>
 * Various strategies exist to do this, but for CIlib the simple process
 * to enable this is to implement the {@code ChangeStrategy} and attach
 * it to the problem.
 * </p>
 * <p>
 * {@code ChangeStrategy} classes implement a simple boolean function which
 * determines if a change should occur or not.
 * </p>
 * <p>
 * {@code ChangeStrategy} classes are used as follows:
 * </p>
 * <pre>
 * if (changeStrategy.shouldApply(problem))
 *     problem.changeEnvironment();
 * </pre>
 */
package net.sourceforge.cilib.problem.changestrategy;
