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
package net.sourceforge.cilib.io;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Wiehann Matthysen
 */
public class VectorReader implements DataReader<List<Type>> {

    private final Vector vector;
    private final int columnCount;
    private int index;

    public VectorReader(Vector vector, int columnCount) {
        Preconditions.checkArgument(vector != null);
        Preconditions.checkArgument(columnCount > 0);
        Preconditions.checkArgument(vector.size() % columnCount == 0,
                "Vector cannot be split up into rows.");
        this.columnCount = columnCount;
        this.vector = vector;
        this.index = 0;
    }

    @Override
    public void open() throws CIlibIOException {
        this.index = 0;
    }

    @Override
    public List<Type> nextRow() {
        List<Type> row = Lists.newArrayList();
        for (int i = 0; i < this.columnCount; ++i) {
            row.add(Real.valueOf(this.vector.get(this.index + i).doubleValue()));
        }
        this.index += this.columnCount;
        return row;
    }

    @Override
    public boolean hasNextRow() throws CIlibIOException {
        return this.index < this.vector.size();
    }

    @Override
    public void close() throws CIlibIOException {
    }

    @Override
    public List<String> getColumnNames() {
        return Collections.nCopies(this.columnCount, "Column");
    }

    @Override
    public String getSourceURL() {
        return null;
    }

    @Override
    public void setSourceURL(String sourceURL) {
        throw new UnsupportedOperationException("Cannot set source for VectorReader.");
    }
}
