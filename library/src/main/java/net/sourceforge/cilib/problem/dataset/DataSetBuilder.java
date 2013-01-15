/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

public abstract class DataSetBuilder implements Iterable<DataSet>, Cloneable {

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
