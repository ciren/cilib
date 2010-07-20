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
package net.sourceforge.cilib.moo.archive.solutionweighing;


import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.selection.weighting.Weighting;

/**
 * <p>
 * Weighs an optimisation solution for selection to be either removed from the
 * {@code Archive} or selected to be used as guide during the search process.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public interface SolutionWeighing extends Weighting, Cloneable {

    @Override
    abstract SolutionWeighing getClone();

//    @Override
//    boolean weigh(List<Selection.Entry<OptimisationSolution>> elements);
}
