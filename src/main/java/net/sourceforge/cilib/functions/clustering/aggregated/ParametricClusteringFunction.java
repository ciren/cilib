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
package net.sourceforge.cilib.functions.clustering.aggregated;

import java.util.ArrayList;
import java.util.Set;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.clustering.ClusteringErrorFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.MaximumAverageDistanceFunction;
import net.sourceforge.cilib.functions.clustering.MinimumSeparationFunction;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@linkplain ClusteringFitnessFunction} to calculate a parameterised fitness of a particular
 * clustering in the <tt>calculateFitness</tt> method. See:<br/>
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern Recognition
 *             and Image Processing", author = "Mahamed G.H. Omran", institution = "University Of
 *             Pretoria", school = "Computer Science", year = "2004", month = nov, pages = "105"
 *             address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht" }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCentroidStrategy}.
 * @author Theuns Cloete
 */
public class ParametricClusteringFunction extends ClusteringErrorFunction {
    private static final long serialVersionUID = 583965930447258179L;
    private static final double DEF_WEIGHT = 0.5;

    private final ClusteringFunction maximumAverageDistance;
    private final ClusteringFunction minimumSeparation;
    private ControlParameter w1;
    private ControlParameter w2;

    /**
     * TODO: Inject the wrapped functions, w1 and w2
     */
    public ParametricClusteringFunction() {
        this.w1 = new ConstantControlParameter(DEF_WEIGHT);
        this.w2 = new ConstantControlParameter(DEF_WEIGHT);
        this.maximumAverageDistance = new MaximumAverageDistanceFunction();
        this.minimumSeparation = new MinimumSeparationFunction();
    }

    @Override
    public Double apply(ArrayList<Cluster<Vector>> clusters, Set<Pattern<Vector>> patterns, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
//        Preconditions.checkState(this.getW1() + this.getW2() == 1.0, "The sum of w1 and w2 must equal 1.0");

        return (this.getW1() * this.maximumAverageDistance.apply(clusters, patterns, distanceMeasure, dataSetMean, dataSetVariance, zMax)) + (getW2() * (zMax - this.minimumSeparation.apply(clusters, patterns, distanceMeasure, dataSetMean, dataSetVariance, zMax)));
    }

    /**
     * Set the weight that influences how much the intra-cluster-distance will contribute to the final fitness.
     * @param w1 the {@linkplain ControlParameter} that will control the <tt>w1</tt> weight.
     */
    public void setW1(ControlParameter w1) {
        this.w1 = w1;
    }

    /**
     * Get the weight that the intra-cluster-distance contributes to the final fitness.
     * @return the weight that determines how much influence intra-cluster-distance contributes to
     *         the final fitness.
     */
    protected double getW1() {
        return this.w1.getParameter();
    }

    /**
     * Set the weight that influences how much the inter-cluster-distance will contribute to the
     * final fitness.
     * @param w2 the {@linkplain ControlParameter} that will control the <tt>w2</tt> weight.
     */
    public void setW2(ControlParameter w2) {
        this.w2 = w2;
    }

    /**
     * Get the weight that determines how much influence inter-cluster-distance contributes to the final fitness.
     * @return the weight that determines how much influence intra-cluster-distance contributes to
     *         the final fitness.
     */
    protected double getW2() {
        return this.w2.getParameter();
    }

    public void updateControlParameters() {
        this.w1.updateParameter();
        this.w2.updateParameter();
    }

    @Override
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.clusterCenterStrategy = clusterCenterStrategy;
        this.maximumAverageDistance.setClusterCenterStrategy(this.clusterCenterStrategy);
        this.minimumSeparation.setClusterCenterStrategy(this.clusterCenterStrategy);
    }
}
