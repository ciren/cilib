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
package net.sourceforge.cilib.functions.clustering;

import java.util.ArrayList;

import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The {@link #evaluate(net.sourceforge.cilib.type.types.container.Vector)} method of this abstract class prepares all
 * the required objects needed in order to call the abstract
 * {@link ClusteringFunction#apply(net.sourceforge.cilib.problem.ClusteringProblem, net.sourceforge.cilib.type.types.container.Set, java.util.ArrayList)}
 * method.<br/>It would be ideal if these required objects could be injected in some way, which will make the evaluate
 * method obsolete and the class more testable. Another advantage of dependency injection would be that the other
 * clustering functions will not have to inherit from this class to minimise code duplication and get the functionality.
 * A {@link ClusterCenterStrategy} is defined to enable the user of this class to specify what is meant by the
 * <i>center</i> of a cluster, because sometimes the <i>centroid</i> is used and other times the <i>mean</i> is used. By
 * default, the cluster center is interpreted as the cluster centroid.
 *
 * @author Theuns Cloete
 */
public abstract class ClusteringErrorFunction implements ClusteringFunction {
    private static final long serialVersionUID = 4834673666638644106L;

    protected ClusterCenterStrategy clusterCenterStrategy;

    public ClusteringErrorFunction() {
        this.clusterCenterStrategy = new ClusterCentroidStrategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClusteringErrorFunction getClone() {
        return this;
    }

    /**
     * This method is responsible for preparing the required objects in order to call the
     * {@link #apply(net.sourceforge.cilib.problem.ClusteringProblem, net.sourceforge.cilib.type.types.container.Set, java.util.ArrayList)}
     * method.
     * @param centroids The {@link Vector} representing the combined centroid vectors
     * @return the clustering error that has been calculated
     */
    @Override
    public Double apply(ArrayList<Cluster> centroids) {
/*
        ClusteringProblem clusteringProblem = ClusteringFunctions.getClusteringProblem();
        StaticDataSetBuilder dataSetBuilder = (StaticDataSetBuilder) clusteringProblem.getDataSetBuilder();
        Set<Pattern<Vector>> patterns = dataSetBuilder.getPatterns();
        ArrayList<Cluster<Vector>> significantClusters = ClusteringFunctions.arrangeClustersAndCentroids(centroids, clusteringProblem, dataSetBuilder);

        return this.apply(significantClusters, patterns, clusteringProblem);
*/
        throw new UnsupportedOperationException("Not supposed to be called");
    }

    @Override
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.clusterCenterStrategy = clusterCenterStrategy;
    }

    @Override
    public ClusterCenterStrategy getClusterCenterStrategy() {
        return this.clusterCenterStrategy;
    }

    /**
     * Retrieve the minimum error that a clustering error function can evaluate to. In most cases, when the error should
     * be minimised, this method will return 0.
     * @return the minimum error that a clustering can achieve
     */
    @Override
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * Retrieve the maximum error that a clustering error function can evaluate to. In most cases, when the error should
     * be minimised, this method will return {@link Double#MAX_VALUE}.
     * @return the maximum error that a clustering can achieve
     */
    @Override
    public Double getMaximum() {
        return Double.MAX_VALUE;
    }

    @Override
    public int getDimension() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDomain() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DomainRegistry getDomainRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDomain(String representation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
