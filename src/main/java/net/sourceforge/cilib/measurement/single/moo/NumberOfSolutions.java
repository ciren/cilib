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
package net.sourceforge.cilib.measurement.single.moo;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.type.types.Int;

/**
 * <p>
 * Measures the number of non-dominated solutions in an archive.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class NumberOfSolutions implements Measurement {

    private static final long serialVersionUID = 7678968480113931897L;

    public NumberOfSolutions() {
    }

    public NumberOfSolutions(NumberOfSolutions copy) {
    }

    @Override
    public Measurement getClone() {
        return new NumberOfSolutions(this);
    }

    @Override
    public String getDomain() {
        return "Z";
    }

    @Override
    public Int getValue(Algorithm algorithm) {
        Archive archive = Archive.Provider.get();
        return Int.valueOf(archive.size());
    }
}
