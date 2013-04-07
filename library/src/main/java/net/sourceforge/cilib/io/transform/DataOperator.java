/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.transform;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface for classes that perform an operation on data in a DataTable.
 */
public interface DataOperator extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    DataOperator getClone();

    /**
     * Apply an operation to the given DataTable.
     * @param dataTable the DataTable to operate on.
     * @return the resulting DataTable.
     * @throws net.sourceforge.cilib.io.exception.CIlibIOException A wrapper exception
     * that occurred during the operation.
     */
    DataTable operate(DataTable dataTable) throws CIlibIOException;
}
