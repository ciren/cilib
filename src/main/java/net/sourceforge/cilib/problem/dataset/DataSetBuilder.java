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
package net.sourceforge.cilib.problem.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author Gary Pampara
 */
public abstract class DataSetBuilder implements Iterable<DataSet>, Cloneable, Serializable {

    private static final long serialVersionUID = 5252204062214206898L;
    protected ArrayList<DataSet> dataSets = null;

    public DataSetBuilder() {
        this.dataSets = new ArrayList<DataSet>();
    }

    public DataSetBuilder(DataSetBuilder rhs) {
        dataSets = new ArrayList<DataSet>();
        for (DataSet dataset : rhs.dataSets) {
            dataSets.add(dataset.getClone());
        }
    }

    public abstract DataSetBuilder getClone();

    public void addDataSet(DataSet dataSet) {
        this.dataSets.add(dataSet);
    }

    public DataSet getDataSet(int index) {
        return this.dataSets.get(index);
    }

    public Iterator<DataSet> iterator() {
        return this.dataSets.iterator();
    }

    public abstract void initialise();

    public void uninitialise(Vector centroids) {
    }
}
