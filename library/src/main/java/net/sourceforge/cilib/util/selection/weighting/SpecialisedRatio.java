/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import static com.google.common.base.Preconditions.checkState;
import java.util.List;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

public class SpecialisedRatio implements ParticleBehaviorRatio {
    private List<ParticleBehavior> behaviors;
    private List<Double> weights;

    @Override
    public double getRatio(ParticleBehavior particleBehavior) {
        checkState(behaviors.size() > 0, "You must add particle behaviors to the behavior pool first.");
        checkState(weights.size() == behaviors.size(), "Make sure the behavior pool is the same size as the weights list.");

        return weights.get(behaviors.indexOf(particleBehavior));
    }

    public void setBehaviors(List<ParticleBehavior> behaviors) {
        this.behaviors = behaviors;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }
}
