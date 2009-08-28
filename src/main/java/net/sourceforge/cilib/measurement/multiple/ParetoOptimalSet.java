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
package net.sourceforge.cilib.measurement.multiple;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Measures the set of non-dominated decision vectors within an archive.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class ParetoOptimalSet implements Measurement<TypeList> {
    private static final long serialVersionUID = 8157352173131734782L;

    @Override
    public ParetoOptimalSet getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "T";
    }

    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList allPositions = new TypeList();
        Collection<OptimisationSolution> solutions = Archive.get();
        for (OptimisationSolution solution : solutions) {
            Vector position = (Vector) solution.getPosition();
            allPositions.add(position);
        }
        return allPositions;
    }
}
