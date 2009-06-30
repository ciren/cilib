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
 * The <it>Algorithm</it> package provides the generic algorithm foundation
 * for all {@link Algorithm} instances.
 *
 * All {@link Algorithm}s are defined to execute as a separate thread within
 * the Java VM. The the contained components within the algorithm class are
 * plugable, resulting in each algorithm providing a generic skeleton that is
 * filled in during construction of the algorithm instance.
 *
 * <p>
 * The algorithms can be classified into 3 distinct categories:
 * </p>
 * <ul>
 *   <li>{@linkplain net.sourceforge.cilib.algorithm.SingularAlgorithm Singular Algorithms}</li>
 *   <li>{@linkplain net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm Single Population Based Algorithms}</li>
 *   <li>{@linkplain net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm Multi Population Based Algorithms}</li>
 * </ul>
 *
 * <p>
 * Please refer to the individual algorithm classes for more information
 * on what properties are exposed and for what is available.
 * </p>
 */
package net.sourceforge.cilib.algorithm;
