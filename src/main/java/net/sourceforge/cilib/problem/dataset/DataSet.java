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
package net.sourceforge.cilib.problem.dataset;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Set;

import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This interface provides an abstraction for accessing data sets. The underlying data set can be
 * accessed using either an <code>InputStream</code> or a <code>byte[]</code>
 * @author Edwin Peer
 * @author Theuns Cloete
 */
public abstract class DataSet implements Cloneable, Serializable {
    private static final long serialVersionUID = 5190227337412349440L;

    protected String identifier = null;

    public DataSet() {
        this.identifier = "<unknown data set>";
    }

    public DataSet(DataSet rhs) {
        this.identifier = rhs.identifier;
    }

    @Override
    public abstract DataSet getClone();

    public abstract Set<Pattern<Vector>> parseDataSet();

    /**
     * Returns the data set as a byte array.
     * @return the data set as a <code>byte[]</code>
     */
    public abstract byte[] getData();

    /**
     * Returns the data set as an input stream.
     * @return the data set as a <code>InputStream</code>
     */
    public abstract InputStream getInputStream();

    /**
     * Get the unique identifier of this data set.
     *
     * @return the unique identifier of this data set
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Set the identifier of this data set. In the case of a file on disk, i.e. when using
     * {@link LocalDataSet}, the filename will be used as the identifier.
     *
     * @param id the identifier of the data set
     */
    public void setIdentifier(String id) {
        identifier = id;
    }
}
