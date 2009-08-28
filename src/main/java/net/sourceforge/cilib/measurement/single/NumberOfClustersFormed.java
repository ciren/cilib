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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;

/**
 * This measurement measures the number of clusters that were formed during a particular clustering.
 * For this measurement to work, the following is important:
 * <ol>
 * <li><tt>Algorithm.get()</tt> should not be <tt>null</tt>.</li>
 * <li>The algorithm's best solution (best position) should return a {@linkplain Vector}.</li>
 * <li>The algorithm's problem's {@linkplain DataSetBuilder} should be a
 * {@linkplain ClusterableDataSet}.</li>
 * <li>The <tt>arrangeClustersAndCentroids()</tt> method (defined in
 * {@linkplain ClusterableDataSet}) should be implemented to remove <i>empty clusters</i>.</li>
 * <li>The <tt>getArrangedClusters()</tt> method (defined in {@linkplain ClusterableDataSet})
 * should be implemented to return the list of non-empty clusters.</li>
 * </ol>
 * @author Theuns Cloete
 */
public class NumberOfClustersFormed implements Measurement<Int> {
    private static final long serialVersionUID = 2174807313995885918L;

    /**
     * {@inheritDoc}
     */
    @Override
    public NumberOfClustersFormed getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "Z";
    }

    @Override
    public Int getValue(Algorithm algorithm) {
        ClusteringUtils helper = ClusteringUtils.get();
        Vector centroids = (Vector) algorithm.getBestSolution().getPosition();
        helper.arrangeClustersAndCentroids(centroids);
        return new Int(helper.getArrangedCentroids().size());
    }
}
