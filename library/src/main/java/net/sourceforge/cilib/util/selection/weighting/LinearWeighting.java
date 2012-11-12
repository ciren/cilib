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
public class LinearWeighting implements Weighting {

    private double min;
    private double max;

    public LinearWeighting() {
        this(0.0, 1.0);
    }

    public LinearWeighting(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        List<T> elements = Lists.newArrayList(iterable);
        List<WeightedObject> results = Lists.newArrayListWithExpectedSize(elements.size());

        double stepSize = (this.max - this.min) / (elements.size() - 1);
        int objectIndex = 0;
        for (T element : elements) {
            results.add(new WeightedObject(element, objectIndex++ * stepSize + this.min));
        }
        return results;
    }
}
