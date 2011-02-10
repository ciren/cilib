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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.util.Cloneable;

/**
 * This is a common abstraction for all problem classes. All problems should extend this interface.
 * All {@linkplain net.sourceforge.cilib.problem.Problem problems} are effectively dynamic problems
 * where non-dynamic problems are essentially problems that are "frozen" at an arbitrary time step.
 *
 * @author  Edwin Peer
 */
public interface Problem extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    Problem getClone();

    /**
     * Change the environment. TODO: this might need to be refactored.
     */
    void changeEnvironment();
}
