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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.clustering.ClusteringErrorFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.QuantisationErrorFunction;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@linkplain ClusteringFitnessFunction) to calculate an improved parameterised fitness of a
 * particular clustering in the <tt>calculateFitness</tt> method. See:<br/>
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern Recognition
 *             and Image Processing", author = "Mahamed G.H. Omran", institution = "University Of
 *             Pretoria", school = "Computer Science", year = "2004", month = nov, pages = "114 &
 *             115" address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht" }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 * @author Theuns Cloete
 */
public class ParametricWithQuantisationErrorFunction extends ClusteringErrorFunction {
    private static final long serialVersionUID = -2022785065235231801L;
    private static final double DEF_WEIGHT = 0.4;
    private static final double DEF_ZMAX = -1.0;

    // TODO: Inject the instance fields
    private final ParametricClusteringFunction parametricFunction;
    private final ClusteringFunction quantisationError;
    private ControlParameter w3;

    /**
     * Create a new instance of {@literal ParametricWithQuantisationErrorFunction}.
     */
    public ParametricWithQuantisationErrorFunction() {
        this.parametricFunction = new ParametricClusteringFunction();
        this.quantisationError = new QuantisationErrorFunction();
        this.parametricFunction.setW1(new ConstantControlParameter(0.3));
        this.parametricFunction.setW2(new ConstantControlParameter(0.3));
        this.w3 = new ConstantControlParameter(DEF_WEIGHT);
    }

    @Override
    public Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
//        Preconditions.checkState(this.getW1() + this.getW2() + this.getW3() == 1.0, "The sum of w1, w2 and w3 must equal 1.0");

        return this.parametricFunction.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax) + (this.getW3() * this.quantisationError.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax));
    }

    public void setW1(ControlParameter w1) {
        this.parametricFunction.setW1(w1);
    }

    public double getW1() {
        return this.parametricFunction.getW1();
    }

    public void setW2(ControlParameter w2) {
        this.parametricFunction.setW2(w2);
    }

    public double getW2() {
        return this.parametricFunction.getW2();
    }

    /**
     * Set the weight that influences how much the Quantisation Error will contribute to the final fitness.
     * @param w3 the {@linkplain ControlParameter} that will control the <tt>w1</tt> weight
     */
    public void setW3(ControlParameter w3) {
        this.w3 = w3;
    }

    /**
     * Get the weight that the Quantisation Error contributes to the final fitness.
     * @return the weight that determines how much influence the Quantisation Error contributes to the final fitness
     */
    protected double getW3() {
        return this.w3.getParameter();
    }

    /**
     * {@inheritDoc}
     */
    public void updateControlParameters() {
        this.parametricFunction.updateControlParameters();
        this.w3.updateParameter();
    }

    @Override
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.clusterCenterStrategy = clusterCenterStrategy;
        this.parametricFunction.setClusterCenterStrategy(this.clusterCenterStrategy);
        this.quantisationError.setClusterCenterStrategy(this.clusterCenterStrategy);
    }
}
