/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.util.selection.WeightedObject;

/**
 *
 */
public class FixedWeighting implements Weighting {
    private final double weight;

    public FixedWeighting(double weight) {
        this.weight = weight;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        List<WeightedObject> result = Lists.newArrayList();
        for (T t : iterable) {
            result.add(new WeightedObject(t, weight));
        }
        return result;
    }
}
