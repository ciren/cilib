/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
