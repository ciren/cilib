/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection;

/**
 *
 */
public class WeightedObject implements Comparable<WeightedObject> {

    private final Object object;
    private final double weight;

    public WeightedObject(Object object, double weight) {
        this.object = object;
        this.weight = weight;
    }

    public Object getObject() {
        return object;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(WeightedObject o) {
        return Double.compare(weight, o.weight);
    }
}
