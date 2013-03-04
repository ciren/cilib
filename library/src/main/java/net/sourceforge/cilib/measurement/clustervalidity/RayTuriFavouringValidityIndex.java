/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 * This class calculates the Favouring Ray Tury Validity Index (which is an adaptation of the Ray Tury Validity index)
 * that can be found in:
 * {@literal@}{Graaff11,
 *  author = {Graaff A. J. and Engelbrecht A. P.},
 *  title = {A local network neighbourhood artificial immune system},
 *  year = {2011},
 *  }
 */

public class RayTuriFavouringValidityIndex extends ValidityIndex{
    private ControlParameter scalingConstant;
    private RayTuriValidityIndex delegate;
    private ControlParameter mean;
    private ControlParameter standardDeviation;

    /*
     * Default constructor for RayTuriFavouringValidityIndex
     */
    public RayTuriFavouringValidityIndex() {
        scalingConstant = ConstantControlParameter.of(20.0);
        delegate = new RayTuriValidityIndex();
        mean = ConstantControlParameter.of(0.0);
        standardDeviation = ConstantControlParameter.of(1.0);
    }

    /*
     * Copy constructor for RayTuriFavouringValidityIndex
     * @param copy The RayTuriFavouringValidityIndex to be copied
     */
    public RayTuriFavouringValidityIndex(RayTuriFavouringValidityIndex copy) {
        scalingConstant = copy.scalingConstant;
        delegate = copy.delegate;
        mean = copy.mean;
        standardDeviation = copy.standardDeviation;
    }

    /*
     * Clone method for RayTuriFavouringValidityIndex
     */
    @Override
    public RayTuriFavouringValidityIndex getClone() {
        return new RayTuriFavouringValidityIndex(this);
    }

    /*
     * Calculates the Favouring Ray Tury Validity Index
     * @param algorithm The algorithm for which the validity index is being calculated
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = delegate.getValue(algorithm).doubleValue() * ((scalingConstant.getParameter() * getGaussianValue(holder)) + 1);

        return Real.valueOf(result);
    }

    /*
     * Calculates a gaussian value
     * @param holder The centroid holder currently in use in the calculation of the validity index
     * @return result The resulting gaussian value
     */
    protected double getGaussianValue(CentroidHolder holder) {
        double power = -1 * (Math.pow((holder.size() - mean.getParameter()), 2) / (2 * Math.pow(standardDeviation.getParameter(), 2)));
        double bottomOfEquation = Math.sqrt(2 * Math.PI * Math.pow(standardDeviation.getParameter(), 2));
        double result = (1 / bottomOfEquation) * Math.exp(power);

        return result;
    }
}
