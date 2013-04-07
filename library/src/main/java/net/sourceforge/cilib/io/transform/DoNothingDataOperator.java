/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.transform;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.math.random.generator.Rand;

/**
 * A data operator that efficiently shuffles a datatable.
 */
public class DoNothingDataOperator implements DataOperator {

    /**
     * {@inheritDoc}
     */
    @Override
    public DoNothingDataOperator getClone() {
        return new DoNothingDataOperator();
    }

    /**
     * Modern version of Fisher-Yates shuffle algorithm based on the Richard Durstenfeld
     * implementation as published in:
     * Durstenfeld, Richard (July 1964). "Algorithm 235: Random permutation". Communications of the ACM 7 (7): 420. doi:10.1145/364520.364540.
     * The shuffle in-place (i.e. it doesn't not use additional memory).
     * @param dataTable the table to shuffle.
     * @return the same table as given with patterns in a uniform random order.
     * @throws CIlibIOException an IO Exception that might occur.
     */
    @Override
    public DataTable operate(DataTable dataTable) {
        return dataTable;
    }
}
