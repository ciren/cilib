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
package net.sourceforge.cilib.functions.clustering;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@linkplain ClusteringFitnessFunction} to calculate a parameterised fitness of a particular
 * clustering in the <tt>calculateFitness</tt> method. See:<br/>
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern Recognition
 *             and Image Processing", author = "Mahamed G.H. Omran", institution = "University Of
 *             Pretoria", school = "Computer Science", year = "2004", month = nov, pages = "105"
 *             address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht" }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 * @author Theuns Cloete
 */
public class ParametricClusteringFunction extends ClusteringFitnessFunction {
    private static final long serialVersionUID = 583965930447258179L;
    /** Specifies the weight that influences how much the intra-cluster-distance will contribute to the final fitness. */
    protected ControlParameter w1 = null;
    /** Specifies the weight that influences how much the inter-cluster-distance will contribute to the final fitness. */
    protected ControlParameter w2 = null;
    /** Stores the calculated zMax value. */
    protected double zMax = -1.0;

    public ParametricClusteringFunction() {
        super();
        w1 = new ConstantControlParameter(0.5);
        w2 = new ConstantControlParameter(0.5);
        zMax = -1.0;
    }

    public ParametricClusteringFunction getClone() {
        return new ParametricClusteringFunction();
    }

    @Override
    public double calculateFitness() {
        // make sure the sum of the parameters equal 1.0
        if (getW1() + getW2() != 1.0)
            throw new IllegalArgumentException("The sum of w1 and w2 must equal 1.0");

        // zMax only needs to be calculated once; domain is not supposed to change during a simulation
        if (zMax < 0.0) {
            zMax = zMax();
        }

        return (getW1() * calculateMaximumAverageDistance()) + (getW2() * (zMax - calculateMinimumInterClusterDistance()));
    }

    /**
     * Set the weight that influences how much the intra-cluster-distance will contribute to the final fitness.
     * @param w the {@linkplain ControlParameter} that will control the <tt>w1</tt> weight.
     */
    public void setW1(ControlParameter w) {
        w1 = w;
    }

    /**
     * Get the weight that the intra-cluster-distance contributes to the final fitness.
     * @return the weight that determines how much influence intra-cluster-distance contributes to
     *         the final fitness.
     */
    protected double getW1() {
        return w1.getParameter();
    }

    /**
     * Set the weight that influences how much the inter-cluster-distance will contribute to the
     * final fitness.
     * @param w the {@linkplain ControlParameter} that will control the <tt>w2</tt> weight.
     */
    public void setW2(ControlParameter w) {
        w2 = w;
    }

    /**
     * Get the weight that determines how much influence inter-cluster-distance contributes to the final fitness.
     * @return the weight that determines how much influence intra-cluster-distance contributes to
     *         the final fitness.
     */
    protected double getW2() {
        return w2.getParameter();
    }

    /**
     * Calculate the maximum distance possible between two {@linkplain Vector}s in the specified
     * domain. In other words, calculate the distance between two {@linkplain Vector}s; where all
     * the elements in the first {@linkplain Vector} is set to that element's upper bound and all
     * the elements in the second {@linkplain Vector} is set to that element's lower bound.
     * @return the maximum distance possible between two {@linkplain Vector}s for the specified domain.
     */
    protected double zMax() {
        Vector prototype = (Vector) helper.getClusteringProblem().getDomain().getBuiltRepresenation();
        Vector upperBoundVector = Vectors.upperBoundVector(prototype);
        Vector lowerBoundVector = Vectors.lowerBoundVector(prototype);

        return helper.calculateDistance(upperBoundVector, lowerBoundVector);
    }

    public void updateControlParameters() {
        w1.updateParameter();
        w2.updateParameter();
    }
}
