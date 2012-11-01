/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.util.selection.WeightedObject;

/**
 * Weighs ParticleBehaviors mainly for HPSO.
 */
public class ParticleBehaviorWeighting implements Weighting {

    private ParticleBehaviorRatio ratio;

    public ParticleBehaviorWeighting() {
        this.ratio = new SuccessRatio();
    }

    public ParticleBehaviorWeighting(ParticleBehaviorRatio ratio) {
        this.ratio = ratio;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        Preconditions.checkArgument(Iterables.get(iterable, 0) instanceof ParticleBehavior);
        List<WeightedObject> result = Lists.newArrayList();

        for (T t : iterable) {
            double weight = ratio.getRatio((ParticleBehavior) t);
            result.add(new WeightedObject(t, weight));
        }

        return result;
    }

    public void setRatio(ParticleBehaviorRatio ratio) {
        this.ratio = ratio;
    }
}
