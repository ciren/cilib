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
 * A data operator that does nothing datatable.
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
     * Returns the same data table that was passed to it.
     * @param dataTable the table to return.
     * @return the same table as given.
     */
    @Override
    public DataTable operate(DataTable dataTable) {
        return dataTable;
    }
}
