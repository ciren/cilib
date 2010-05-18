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
package net.sourceforge.cilib.io.transform;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * A data operator that efficiently shuffles a datatable.
 * @author andrich
 */
public class ShuffleOperator implements DataOperator {

    /**
     * Modern version of Fisher-Yates shuffle algorithm based on the Richard Durstenfeld
     * implementation as published in:
     * Durstenfeld, Richard (July 1964). "Algorithm 235: Random permutation". Communications of the ACM 7 (7): 420. doi:10.1145/364520.364540.
     * The shuffle in-place (i.e. it doesn not use additional memory).
     * @param dataTable the table to shuffle.
     * @return the same table as given with patterns in a uniform random order.
     * @throws CIlibIOException an IO Exception that might occur.
     */
    @Override
    public DataTable operate(DataTable dataTable) throws CIlibIOException {
        MersenneTwister twister = new MersenneTwister();

        int size = dataTable.size();
        for (int n = size - 1; n > 1; n--) {
            int k = twister.nextInt(n + 1);
            Object tmp = dataTable.getRow(k);
            dataTable.setRow(k, dataTable.getRow(n));
            dataTable.setRow(n, tmp);
        }

        return dataTable;
    }

}
