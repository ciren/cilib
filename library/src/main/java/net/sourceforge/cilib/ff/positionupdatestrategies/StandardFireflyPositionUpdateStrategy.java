/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff.positionupdatestrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.ff.firefly.Firefly;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * <p>
 * An implementation of the standard Firefly position update using:
 * <ul>
 * <li>Alpha (random step size)</li>
 * <li>Beta (brightness factor)</li>
 * <li>Gamma (light absorption coefficient)</li>
 * <li>r (euclidean distance between fireflies)</li>
 * </ul>
 * </p>
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li>Yang, Xin-She. "Firefly algorithms for multimodal optimization."
 * Stochastic algorithms: foundations and applications (2009): 169-178.
 * </li>
 * </ul>
 * </p>
 */
public class StandardFireflyPositionUpdateStrategy implements FireflyPositionUpdateStrategy {

    private DistanceMeasure distanceMeasure;
    private ControlParameter alpha;
    private ControlParameter betaMin;
    private ControlParameter gamma;

    /**
     * Create an new instance of {@code StandardFireflyPositionUpdateStrategy} with
     * default control parameters.
     */
    public StandardFireflyPositionUpdateStrategy() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.alpha = new LinearlyVaryingControlParameter(0.2, 0.0);
        this.betaMin = ConstantControlParameter.of(0.2);
        this.gamma = ConstantControlParameter.of(1.0);
    }

    /**
     * Copy constructor. Copy the provided instance.
     * @param copy The instance to copy.
     */
    public StandardFireflyPositionUpdateStrategy(StandardFireflyPositionUpdateStrategy copy) {
        this.distanceMeasure = copy.distanceMeasure;
        this.alpha = copy.alpha.getClone();
        this.betaMin = copy.betaMin.getClone();
        this.gamma = copy.gamma.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardFireflyPositionUpdateStrategy getClone() {
        return new StandardFireflyPositionUpdateStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector updatePosition(Firefly firefly, Firefly other) {
        return moveToward(firefly, other);
    }

    /**
     * Move a firefly toward another firefly according to the distance
     * between them {@code r}, brightness factor {@code beta},
     * light absorption coefficient {@code gamma} and a random vector
     * scaled by the step size {@code alpha}.
     */
    private Vector moveToward(Firefly i, Firefly j) {

        // determine the cartesian distance between two fireflies
        double r = distanceMeasure.distance(i.getPosition(), j.getPosition());

        // determine the attraction of the other firefly, based
        // on the minimum brightness factor, light absorption and distance
        double attractiveness = (1.0 - betaMin.getParameter()) *
            Math.exp(-gamma.getParameter() * r * r) + betaMin.getParameter();

        Vector secondTerm = j.getPosition().subtract(i.getPosition())
            .multiply(attractiveness);

        // add a random direction by a factor of alpha
        Vector thirdTerm = randomDirection(i).multiply(alpha.getParameter());

        return i.getPosition().plus(secondTerm).plus(thirdTerm);
    }

    /**
     * Create a random vector in [-0.5, 0.5] and scale it
     * according the domain of the problem.
     */
    private Vector randomDirection(Firefly i) {
        double scale = i.getPosition().boundsOf(0).getRange();

        return Vector.newBuilder()
            .repeat(i.getDimension(), Real.valueOf(0.0, new Bounds(-0.5,0.5)))
            .buildRandom().multiply(scale);
    }

    /**
    * Set the {@link DistanceMeasure} to use.
    * @param distanceMeasure The {@link DistanceMeasure} to set.
    */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /**
    * Get the {@link DistanceMeasure} used within the position update strategy
    * @return the {@link DistanceMeasure} used.
    */
    public DistanceMeasure getDistanceMeasure() {
        return this.distanceMeasure;
    }

    /**
    * Set the alpha control parameter to use.
    * @param alpha the control parameter to set.
    */
    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }

    /**
    * Get the alpha control parameter used within the position update strategy
    * @return the {@link ControlParameter} used.
    */
    public ControlParameter getAlpha() {
        return this.alpha;
    }

    /**
    * Set the beta control parameter to use.
    * @param betaMin the control parameter to set.
    */
    public void setBetaMin(ControlParameter betaMin) {
        this.betaMin = betaMin;
    }

    /**
    * Get the beta control parameter used within the position update strategy
    * @return the {@link ControlParameter} used.
    */
    public ControlParameter getBetaMin() {
        return this.betaMin;
    }

    /**
    * Set the gamma control parameter to use.
    * @param gamma the control parameter to set.
    */
    public void setGamma(ControlParameter gamma) {
        this.gamma = gamma;
    }

    /**
    * Get the gamma control parameter used within the position update strategy.
    * @return the {@link ControlParameter} used.
    */
    public ControlParameter getGamma() {
        return this.gamma;
    }
}
